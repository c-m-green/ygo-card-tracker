package com.cgreen.ygocardtracker.menu;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import com.cgreen.ygocardtracker.remote.CardInfoSaveTask;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;
import com.cgreen.ygocardtracker.card.model.CardModel;
import com.cgreen.ygocardtracker.card.model.CardModelFactory;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.SetCodeDao;
import com.cgreen.ygocardtracker.remote.CardInfoFetcher;
import com.cgreen.ygocardtracker.remote.RemoteDBKey;
import com.cgreen.ygocardtracker.util.AlertHelper;
import com.cgreen.ygocardtracker.util.CardImageSaver;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class AddCardsMenuController {
    private Stage stage;
    @FXML
    private TextField nameSearchField, passcodeSearchField;
    @FXML
    private Button addByNameButton, addByPasscodeButton, addManuallyButton;
    private boolean isSaving;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (isSaving) {
                    event.consume();
                }
            }            
        });
        nameSearchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addByName();
            }
            event.consume();
        });
        passcodeSearchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addByPasscode();
            }
            event.consume();
        });
    }
    
    public void handleAddByNameButtonAction(ActionEvent event) {
        addByName();
        event.consume();
    }
    
    public void handleAddByPasscodeButtonAction(ActionEvent event) {
        addByPasscode();
        event.consume();
    }
    
    public void handleAddManuallyButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(CustomCardEntryController.class.getClassLoader().getResource("custom_card_menu.fxml"));
            Parent parent = loader.load();        
            
            Stage customCardEntryStage = new Stage();
            customCardEntryStage.setScene(new Scene(parent));
            CustomCardEntryController ccec = loader.getController();
            ccec.setStage(customCardEntryStage);
            ccec.init();
            customCardEntryStage.initModality(Modality.APPLICATION_MODAL);
            customCardEntryStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void saveNewCardInfoFromJson(JSONArray cardData) {
        isSaving = true;
        Stage progress = new Stage(StageStyle.UTILITY);
        Label label = new Label("Checking card info...");
        label.setPrefWidth(350);
        ProgressBar pBar = new ProgressBar();
        pBar.setPrefWidth(350);
        // We're not doing fuzzy searches, so we should only have one card.
        if (cardData.length() != 1) {
            throw new IllegalArgumentException("Expected exactly one card name.");
        }
        JSONObject allCardInfo = cardData.getJSONObject(0);
        // Grab the name now so we can use it later.
        String nameColVal = allCardInfo.getString("name");
        CardInfoSaveTask cardInfoSaveTask = new CardInfoSaveTask(cardData);
        cardInfoSaveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task succeeded!");
                progress.hide();
                try {
                    CardInfoDao cardInfoDao = new CardInfoDao();
                    ObservableList<CardInfo> infos = cardInfoDao.getCardInfoByName(nameColVal);
                    showConfirmationScreen(infos);
                } catch (SQLException e) {
                    AlertHelper.raiseAlert("Error while saving card information:\n\n" + e.getMessage());
                }
                isSaving = false;
                setButtonDisable(false);
            }           
        });
        
        cardInfoSaveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task failed!");
                progress.hide();
                isSaving = false;
                Throwable ex = cardInfoSaveTask.getException();
                AlertHelper.raiseAlert("Error while saving card information:\n\n" + ex.getMessage());
                setButtonDisable(false);
            }           
        });
        
        cardInfoSaveTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task cancelled!");
                progress.hide();
                isSaving = false;
                AlertHelper.raiseAlert("Cancelled saving.");
                setButtonDisable(false);
            }           
        });
        pBar.progressProperty().bind(cardInfoSaveTask.progressProperty());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cardInfoSaveTask.cancel();
                event.consume();
            }
        });
        VBox vbox = new VBox(10, label, pBar, cancelButton);
        vbox.setPadding(new Insets(10));
        progress.setTitle("Searching...");
        progress.setScene(new Scene(vbox));
        progress.show();
        new Thread(cardInfoSaveTask).start();
    }
    
    private void showConfirmationScreen(List<CardInfo> savedCardInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(CardConfirmerController.class.getClassLoader().getResource("card_confirmation_menu.fxml"));
            Parent parent = loader.load();        
            
            Stage cardConfirmerStage = new Stage();
            cardConfirmerStage.setScene(new Scene(parent));
            CardConfirmerController ccc = loader.getController();
            ccc.setStage(cardConfirmerStage);
            ccc.init(savedCardInfo);
            cardConfirmerStage.initModality(Modality.APPLICATION_MODAL);
            cardConfirmerStage.show();
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AlertHelper.raiseAlert("An error has occurred during card saving.");
        } catch (IllegalArgumentException iae) {
            AlertHelper.raiseAlert("No valid cards were found matching this term.\n\nPlease note that Skill cards are currently not supported. Otherwise, this could be due to an error while saving card information.");
        }
    }
    
    private void addByName() {
        setButtonDisable(true);
        String searchTerm = nameSearchField.getText();
        if (searchTerm.isBlank()) {
            AlertHelper.raiseAlert("Please enter something!");
            setButtonDisable(false);
        } else {
            JSONObject fetchedInfo = CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, searchTerm);
            // TODO: Might just want 200, not 201?
            if (fetchedInfo == null) {
                System.out.println("Online search failed -- resorting to local DB");
                try {
                    CardInfoDao dao = new CardInfoDao();
                    ObservableList<CardInfo> cardInfos = dao.getCardInfoByName(searchTerm);
                    if (cardInfos.isEmpty()) {
                        AlertHelper.raiseAlert("No card info by the name \"" + searchTerm + "\" was found.");
                    } else {
                        showConfirmationScreen(cardInfos);
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    AlertHelper.raiseAlert(e.getStackTrace().toString());
                    
                }
                setButtonDisable(false);
            } else {
                JSONArray data = fetchedInfo.getJSONArray("data");
                // Save info for this card (may include multiple IDs)
                saveNewCardInfoFromJson(data);
            }
        }
    }
    
    private void addByPasscode() {
        setButtonDisable(true);
        try {
            Integer searchTerm = Integer.parseInt(passcodeSearchField.getText());
            CardInfoDao dao = new CardInfoDao();
            CardInfo cardInfo = dao.getCardInfoByPasscode(searchTerm);
            if (cardInfo == null) {
                JSONObject fetchedInfo = CardInfoFetcher.doOnlineSearch(RemoteDBKey.PASSCODE, searchTerm.toString());
                if (fetchedInfo == null) {
                    AlertHelper.raiseAlert("Could not find id #" + searchTerm);
                } else {
                    saveNewCardInfoFromJson(CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, fetchedInfo.getJSONArray("data").getJSONObject(0).getString("name")).getJSONArray("data"));
                }
            } else {
                if (cardInfo.isFake()) {
                    showConfirmationScreen(dao.getCardInfoByName(cardInfo.getName()));
                } else {
                    JSONObject cardInfoWithThisName = CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, cardInfo.getName());
                    if (cardInfoWithThisName == null) {
                        Alert warn = new Alert(AlertType.WARNING, "Unsuccessfully attempted to fetch additional entries under the name \"" + cardInfo.getName() + "\". Only known information will be displayed.");
                        warn.showAndWait();
                        showConfirmationScreen(dao.getCardInfoByName(cardInfo.getName()));
                    } else {
                        saveNewCardInfoFromJson(cardInfoWithThisName.getJSONArray("data"));
                    }
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException nfe) {
            AlertHelper.raiseAlert("You need to enter an integer here...");
        }
        setButtonDisable(false);
    }
    
    private void setButtonDisable(boolean isDisabled) {
        addByNameButton.setDisable(isDisabled);
        nameSearchField.setDisable(isDisabled);
        addByPasscodeButton.setDisable(isDisabled);
        passcodeSearchField.setDisable(isDisabled);
        addManuallyButton.setDisable(isDisabled);
    }
}
