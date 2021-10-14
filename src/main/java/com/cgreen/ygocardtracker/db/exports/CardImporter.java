package com.cgreen.ygocardtracker.db.exports;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.remote.CardInfoFetcher;
import com.cgreen.ygocardtracker.remote.CardInfoSaveTask;
import com.cgreen.ygocardtracker.remote.RemoteDBKey;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cgreen.ygocardtracker.card.Group;
import com.cgreen.ygocardtracker.dao.impl.DeckDao;
import com.cgreen.ygocardtracker.dao.impl.GroupDao;
import com.cgreen.ygocardtracker.deck.Deck;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CardImporter {
    private static final String BREAKING_VER = "0.5.0";
    private static Map<Integer, Integer> groupIds, deckIds;
    private static List<CardImportEntry> importedCards;
    public static void importJson(File inputFile) {
        /*
         * Step 1: Read contents of file into memory. Probably store
         * into Objects.
         * Step 2: Fetch card information for each card. Don't go above twenty requests
         * per second.
         * Step 3: Store card information for each card.
         * Step 4: Store each card (insert by passcode).
         */
        if (inputFile == null) {
            throw new IllegalArgumentException("File for import was null!");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject inputData = new JSONObject(sb.toString());
            readJson(inputData);
        } catch (IOException e) {
            AlertHelper.raiseAlert("Error reading input file.");
        }
    }

    public static void importCsv(File inputFile, String groupName) {
        if (inputFile == null) {
            throw new IllegalArgumentException("File for import was null!");
        }
        List<List<String>> entries = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(inputFile))) {
            String[] values = null;
            while ((values= csvReader.readNext()) != null) {
                entries.add(Arrays.asList(values));
            }
            readCsv(entries, groupName);
        } catch (IOException | CsvValidationException e) {
            AlertHelper.raiseAlert("Error reading input file.");
        }
    }

    private static void readJson(JSONObject inputData) {
        ComparableVersion inVer = new ComparableVersion(inputData.getString("app_version"));
        ComparableVersion brkVer = new ComparableVersion(BREAKING_VER);
        if (inVer.compareTo(brkVer) < 0) {
            Alert verErr = new Alert(AlertType.ERROR, "This file was saved with an older version of YGO Card Tracker that is no longer readable.");
            verErr.showAndWait();
        } else {
            groupIds = new HashMap<Integer, Integer>();
            deckIds = new HashMap<Integer, Integer>();
            groupIds.put(1, 1);
            deckIds.put(1, 1);
            Set<String> cardNames = new HashSet<String>();
            Task<Void> importFromFileTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    JSONArray groupArr = inputData.getJSONArray("groups");
                    GroupDao grpDao = new GroupDao();
                    for (int i = 0; i < groupArr.length(); i++) {
                        JSONObject grpObj = groupArr.getJSONObject(i);
                        String grpName = grpObj.getString("name");
                        int grpId = grpObj.getInt("id");
                        if (grpId != 1) {
                            Group grp = new Group();
                            grp.setName(grpName);
                            grpDao.save(grp);
                            groupIds.put(grpId, grp.getId());
                        }
                    }
                    JSONArray decksArr = inputData.getJSONArray("decks");
                    DeckDao deckDao = new DeckDao();
                    for (int i = 0; i < decksArr.length(); i++) {
                        JSONObject deckObj = decksArr.getJSONObject(i);
                        String deckName = deckObj.getString("name");
                        int deckId = deckObj.getInt("id");
                        String notes = "";
                        if (deckObj.has("notes")) {
                            notes = deckObj.getString("notes");
                        }
                        if (deckId != 1) {
                            Deck deck = new Deck();
                            deck.setName(deckName);
                            deck.setNote(notes);
                            deckDao.save(deck);
                            deckIds.put(deckId, deck.getId());
                        }
                    }
                    JSONArray cardsArr = inputData.getJSONArray("cards");
                    importedCards = new ArrayList<CardImportEntry>();
                    for (int i = 0; i < cardsArr.length(); i++) {
                        updateProgress(i, cardsArr.length());
                        JSONObject card = cardsArr.getJSONObject(i);
                        CardImportEntry cie = new CardImportEntry();
                        cie.setDeckId(card.getInt("deck_id"));
                        cie.setGroupId(card.getInt("group_id"));
                        cie.setName(card.getString("name"));
                        cie.setPasscode(card.getInt("passcode"));
                        cie.setSetCode(card.getString("set_code"));
                        cardNames.add(cie.getName());
                        importedCards.add(cie);
                    }
                    return null;
                }
            };
            Stage progress = new Stage(StageStyle.UTILITY);
            importFromFileTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("Task succeeded!");
                    progress.hide();
                    fetchNewCardInfo(cardNames);
                }
            });

            importFromFileTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("Task failed!");
                    progress.hide();
                    AlertHelper.raiseAlert("There was a problem reading the input file.");
                    importedCards.clear();
                }
            });

            importFromFileTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("Task cancelled!");
                    progress.hide();
                    AlertHelper.raiseAlert("Import was interrupted.");
                    importedCards.clear();
                }
            });
            Label label = new Label("Fetching card information...");
            label.setPrefWidth(350);
            ProgressBar pBar = new ProgressBar();
            pBar.setPrefWidth(350);
            Button cancelButton = new Button("Cancel");
            VBox vbox = new VBox(10, label, pBar, cancelButton);
            vbox.setPadding(new Insets(10));
            progress.setTitle("Importing...");
            progress.setScene(new Scene(vbox));
            pBar.progressProperty().bind(importFromFileTask.progressProperty());
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    importFromFileTask.cancel();
                    event.consume();
                }
            });
            progress.show();
            new Thread(importFromFileTask).start();
        }
    }

    private static void readCsv(List<List<String>> inputData, String groupName) {
        try {
            Set<String> cardNames = new HashSet<String>();
            importedCards = new ArrayList<CardImportEntry>();
            groupIds = new HashMap<Integer, Integer>();
            int groupId = 1;
            if (groupName != null && !groupName.isBlank()) {
                GroupDao grpDao = new GroupDao();
                Group grp = new Group();
                grp.setName(groupName);
                grpDao.save(grp);
                groupId = grp.getId();
            }
            groupIds.put(1, groupId);
            deckIds = new HashMap<Integer, Integer>();
            deckIds.put(1, 1);
            for (int i = 0; i < inputData.size(); i++) {
                List<String> cols = inputData.get(i);
                if (i > 0) {
                    int qty = Integer.parseInt(cols.get(1));
                    for (int cardNum = 0; cardNum < qty; cardNum++) {
                        CardImportEntry cie = new CardImportEntry();
                        cardNames.add(cols.get(0));
                        cie.setName(cols.get(0));
                        cie.setPasscode(Integer.parseInt(cols.get(2)));
                        cie.setGroupId(1);
                        cie.setDeckId(1);
                        cie.setSetCode(cols.get(7));
                        importedCards.add(cie);
                    }
                } else {
                    assert(cols.get(0).equals("cardname"));
                    assert(cols.get(1).equals("cardq"));
                    assert(cols.get(2).equals("cardid"));
                    assert(cols.get(3).equals("cardrarity"));
                    assert(cols.get(4).equals("cardcondition"));
                    assert(cols.get(5).equals("card_edition"));
                    assert(cols.get(6).equals("cardset"));
                    assert(cols.get(7).equals("cardcode"));
                }
            }
            fetchNewCardInfo(cardNames);
        } catch (SQLException e) {
            AlertHelper.raiseAlert("There was a problem connecting to the database.");
        } catch (NullPointerException npe) {
            AlertHelper.raiseAlert("ERROR: Missing data for one or more cards in the input file.");
        } catch (NumberFormatException nfe) {
            AlertHelper.raiseAlert("ERROR: Missing numerical value in input file where one was expected.");
        }
    }
    
    private static void fetchNewCardInfo(Set<String> cardNames) {
        JSONArray fetchedCardInfos = new JSONArray();
        Task<Void> cardInfoFetchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int index = 0;
                for (String name : cardNames) {
                    updateProgress(index, cardNames.size());
                    TimeUnit.MILLISECONDS.sleep(65);
                    JSONObject returnedCardInfo = CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, name);
                    if (returnedCardInfo != null) {
                        // We're not using fuzzy search, so we should get one result.
                        fetchedCardInfos.put(returnedCardInfo.getJSONArray("data").getJSONObject(0));
                    }
                    index++;
                }
                return null;
            }
        };
        Stage progress = new Stage(StageStyle.UTILITY);
        cardInfoFetchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task succeeded!");
                progress.hide();
                saveNewCardInfo(fetchedCardInfos);
            }
        });

        cardInfoFetchTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task failed!");
                progress.hide();
                AlertHelper.raiseAlert("There was a problem fetching card data from the remote database.");
                importedCards.clear();
            }
        });

        cardInfoFetchTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task cancelled!");
                progress.hide();
                AlertHelper.raiseAlert("Import was interrupted.");
                importedCards.clear();
            }
        });
        Label label = new Label("Fetching card information...");
        label.setPrefWidth(350);
        ProgressBar pBar = new ProgressBar();
        pBar.setPrefWidth(350);
        Button cancelButton = new Button("Cancel");
        VBox vbox = new VBox(10, label, pBar, cancelButton);
        vbox.setPadding(new Insets(10));
        progress.setTitle("Importing...");
        progress.setScene(new Scene(vbox));
        pBar.progressProperty().bind(cardInfoFetchTask.progressProperty());
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cardInfoFetchTask.cancel();
                event.consume();
            }
        });
        progress.show();
        new Thread(cardInfoFetchTask).start();
    }

    private static void saveNewCardInfo(JSONArray fetchedCardInfos) {
        CardInfoSaveTask saveTask = new CardInfoSaveTask(fetchedCardInfos);
        Stage progress = new Stage(StageStyle.UTILITY);
        Label label = new Label("Saving card information...");
        label.setPrefWidth(350);
        ProgressBar pBar = new ProgressBar();
        pBar.setPrefWidth(350);
        Button cancelButton = new Button("Cancel");
        VBox vbox = new VBox(10, label, pBar, cancelButton);
        vbox.setPadding(new Insets(10));
        progress.setTitle("Importing...");
        progress.setScene(new Scene(vbox));
        saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task succeeded!");
                progress.hide();
                saveCards();
            }
        });

        saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task failed!");
                progress.hide();
                AlertHelper.raiseAlert("There was a problem saving card information.");
                importedCards.clear();
            }
        });

        saveTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task cancelled!");
                progress.hide();
                AlertHelper.raiseAlert("Import was interrupted.");
                importedCards.clear();
            }
        });

        pBar.progressProperty().bind(saveTask.progressProperty());
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveTask.cancel();
                event.consume();
            }
        });
        progress.show();
        new Thread(saveTask).start();
    }

    private static void saveCards() {
        Task<Void> saveCardsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                CardDao cardDao = new CardDao();
                CardInfoDao cardInfoDao = new CardInfoDao();
                for (int i = 0; i < importedCards.size(); i++) {
                    updateProgress(i, importedCards.size());
                    CardImportEntry cardImportEntry = importedCards.get(i);
                    Card card = new Card();
                    card.setDeckId(deckIds.get(cardImportEntry.getDeckId()));
                    card.setGroupId(groupIds.get(cardImportEntry.getGroupId()));
                    card.setInSideDeck(false);
                    card.setSetCode(cardImportEntry.getSetCode());
                    card.setIsVirtual(false);
                    try {
                        cardDao.save(card, cardImportEntry.getPasscode());
                    } catch (SQLException e) {
                        Platform.runLater(
                                () -> {
                                    Alert warn = new Alert(AlertType.WARNING, "Could not find card information for \""
                                            + cardImportEntry.getPasscode() + ".\" Check for mismatch with card name \""
                                            + cardImportEntry.getName() + ".\"\n\nThis card will be skipped.");
                                    warn.showAndWait();
                                }
                        );
                    }
                }
                return null;
            }
        };
        Stage progress = new Stage(StageStyle.UTILITY);
        Label label = new Label("Saving " + importedCards.size() + " card(s)...");
        label.setPrefWidth(350);
        ProgressBar pBar = new ProgressBar();
        pBar.setPrefWidth(350);
        Button cancelButton = new Button("Cancel");
        VBox vbox = new VBox(10, label, pBar, cancelButton);
        vbox.setPadding(new Insets(10));
        progress.setTitle("Importing...");
        progress.setScene(new Scene(vbox));
        saveCardsTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task succeeded!");
                progress.hide();
                importedCards.clear();
                Alert success = new Alert(AlertType.INFORMATION, "Import complete!");
                success.showAndWait();
            }
        });

        saveCardsTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task failed!");
                progress.hide();
                Throwable throwable = saveCardsTask.getException();
                throwable.printStackTrace();
                AlertHelper.raiseAlert("There was a problem saving cards.");
                importedCards.clear();
            }
        });

        saveCardsTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task cancelled!");
                progress.hide();
                AlertHelper.raiseAlert("Import was interrupted.");
                importedCards.clear();
            }
        });

        pBar.progressProperty().bind(saveCardsTask.progressProperty());
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveCardsTask.cancel();
                event.consume();
            }
        });
        progress.show();
        new Thread(saveCardsTask).start();
    }
}
