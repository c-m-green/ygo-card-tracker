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
        JSONObject allCardInfo = cardData.getJSONObject(0);
        // Grab the name now so we can use it later.
        String nameColVal = allCardInfo.getString("name");
        Task<Void> cardSaveTask = new Task<Void>() {
            @Override
            protected Void call() throws SQLException {
                CardInfoDao dao = new CardInfoDao();           
                Integer cardTypeIndex = CardType.getIndexOf(allCardInfo.getString("type"));
                CardType cardType = CardType.getCardType(cardTypeIndex);
                if (cardType == CardType.SKILL) {
                    // TODO: Log this
                    System.out.println("Encountered a Skill card -- skipping this one.");
                    return null;
                }
                CardModel cardModel = CardModelFactory.getCardModel(cardType);
                String descColVal = allCardInfo.getString("desc");
                String cardVarStr = allCardInfo.getString("race");
                // Manually setting card variant in some cases because there are two that
                // say "Normal"
                CardVariant cardVariant;
                if (cardVarStr.equalsIgnoreCase("Normal")) {
                    if (cardType == CardType.SPELL) {
                        cardVariant = CardVariant.SPELL_NORMAL;
                    } else if (cardType == CardType.TRAP) {
                        cardVariant = CardVariant.TRAP_NORMAL;
                    } else {
                        // Dunno what to do in this case...
                        cardVariant = CardVariant.UNKNOWN;
                    }
                } else {
                    cardVariant = CardVariant.getCardVariant(CardVariant.getIndexOf(cardVarStr));
                }
                Integer linkValueColVal, atkColVal, defColVal, levelColVal, scaleColVal;
                String linkMarkersColVal, attributeColVal, setCodesColVal;
                if (cardModel.isLink()) {
                    JSONArray linkMarkersArr = allCardInfo.getJSONArray("linkmarkers");
                    linkMarkersColVal = "";
                    for (int linkIndex = 0; linkIndex < linkMarkersArr.length() - 1; linkIndex++) {
                        linkMarkersColVal += linkMarkersArr.getString(linkIndex) + ",";
                    }
                    linkMarkersColVal += linkMarkersArr.getString(linkMarkersArr.length() - 1);
                    linkValueColVal = allCardInfo.getInt("linkval");
                } else {
                    linkValueColVal = null;
                    linkMarkersColVal = null;
                }
                atkColVal = (cardModel.isMonster()) ? allCardInfo.getInt("atk") : null;
                defColVal = (cardModel.isMonster() && !cardModel.isLink()) ? allCardInfo.getInt("def") : null;
                levelColVal = (cardModel.hasLevel()) ? allCardInfo.getInt("level") : null;
                scaleColVal = (cardModel.hasScale()) ? allCardInfo.getInt("scale") : null;
                attributeColVal = (cardModel.hasAttribute()) ? allCardInfo.getString("attribute") : null;
                JSONArray cardSetsArr = allCardInfo.getJSONArray("card_sets");
                setCodesColVal = "";
                for (int i = 0; i < cardSetsArr.length() - 1; i++) {
                    JSONObject cardSetObj = cardSetsArr.getJSONObject(i);
                    setCodesColVal += cardSetObj.getString("set_code") + ",";
                }
                JSONObject cardSetObjLast = cardSetsArr.getJSONObject(cardSetsArr.length() - 1);
                setCodesColVal += cardSetObjLast.getString("set_code");
                SetCodeDao setCodeDao = new SetCodeDao();
                int setCodeId = setCodeDao.save(setCodesColVal);
                JSONArray cardImagesArr = allCardInfo.getJSONArray("card_images");
                for (int i = 0; i < cardImagesArr.length(); i++) {
                    updateProgress(i, cardImagesArr.length());
                    System.out.println("Saving card info for " + nameColVal + "(" + (i + 1) + ")");
                    JSONObject cardImageObj = cardImagesArr.getJSONObject(i);
                    Integer passcodeColVal = cardImageObj.getInt("id");
                    int numCardsInDBWithThisPasscode = dao.getNumCardInfos(passcodeColVal);
                    if (numCardsInDBWithThisPasscode > 0) {
                        // TODO: Log this
                        System.out.println("Already have card info for " + passcodeColVal + " -- skipping it");
                        continue;
                    } else if (numCardsInDBWithThisPasscode == -1) {
                        // TODO: Log this
                        // TODO: Am I sure I want to throw this exception here?
                        throw new SQLException ("ERROR: Problem checking whether " + passcodeColVal + " is in the DB.");
                    }
                    CardInfo dbCardInfo = new CardInfo();
                    dbCardInfo.setIsFakeCol(false);
                    dbCardInfo.setPasscodeCol(passcodeColVal);
                    dbCardInfo.setCardTypeCol(cardType);
                    dbCardInfo.setNameCol(nameColVal);
                    dbCardInfo.setDescriptionCol(descColVal);
                    dbCardInfo.setVariantCol(cardVariant);
                    dbCardInfo.setAttributeCol(attributeColVal);
                    dbCardInfo.setAttackCol(atkColVal);
                    dbCardInfo.setLinkMarkersCol(linkMarkersColVal);
                    dbCardInfo.setLinkValueCol(linkValueColVal);
                    dbCardInfo.setDefenseCol(defColVal);
                    dbCardInfo.setLevelCol(levelColVal);
                    dbCardInfo.setScaleCol(scaleColVal);
                    dbCardInfo.setSetCodesCol(setCodesColVal);
                    dbCardInfo.setSetCodeId(setCodeId);
                    
                    String picUrl = cardImageObj.getString("image_url");
                    String picUrlSmall = cardImageObj.getString("image_url_small");
                    
                    Image image;
                    try {
                        image = CardImageSaver.getCardImage(picUrl);
                        dbCardInfo.setImageLinkCol(CardImageSaver.saveCardImageFile(image, passcodeColVal));
                    } catch (IOException e) {
                        // TODO Log this
                        System.out.println("ERROR: Problem saving image for " + passcodeColVal);
                        dbCardInfo.setImageLinkCol(null);
                    }
                    Image imageSmall;
                    try {
                        imageSmall = CardImageSaver.getCardImage(picUrlSmall);
                        dbCardInfo.setSmallImageLinkCol(CardImageSaver.saveCardImageFileSmall(imageSmall, passcodeColVal));
                    } catch (IOException e) {
                        // TODO Log this
                        System.out.println("ERROR: Problem saving small image for " + passcodeColVal);
                        dbCardInfo.setSmallImageLinkCol(null);
                    }
                    dao.save(dbCardInfo);
                }
                return null;
            }
        };
        cardSaveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
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
        
        cardSaveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task failed!");
                progress.hide();
                isSaving = false;
                Throwable ex = cardSaveTask.getException();
                AlertHelper.raiseAlert("Error while saving card information:\n\n" + ex.getMessage());
                setButtonDisable(false);
            }           
        });
        
        cardSaveTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task cancelled!");
                progress.hide();
                isSaving = false;
                AlertHelper.raiseAlert("Cancelled saving.");
                setButtonDisable(false);
            }           
        });
        pBar.progressProperty().bind(cardSaveTask.progressProperty());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cardSaveTask.cancel();
                event.consume();
            }
        });
        VBox vbox = new VBox(10, label, pBar, cancelButton);
        vbox.setPadding(new Insets(10));
        progress.setTitle("Searching...");
        progress.setScene(new Scene(vbox));
        progress.show();
        new Thread(cardSaveTask).start();
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
        } else {
            JSONArray data = CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, searchTerm);
            // TODO: Might just want 200, not 201?
            if (data == null) {
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
                JSONArray data = CardInfoFetcher.doOnlineSearch(RemoteDBKey.PASSCODE, searchTerm.toString());
                if (data == null) {
                    AlertHelper.raiseAlert("Could not find id #" + searchTerm);
                    setButtonDisable(false);
                } else {
                    saveNewCardInfoFromJson(CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, data.getJSONObject(0).getString("name")));
                }
            } else {
                if (cardInfo.isFake()) {
                    showConfirmationScreen(dao.getCardInfoByName(cardInfo.getName()));
                    setButtonDisable(false);
                } else {
                    saveNewCardInfoFromJson(CardInfoFetcher.doOnlineSearch(RemoteDBKey.NAME, cardInfo.getName()));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            setButtonDisable(false);
        } catch (NumberFormatException nfe) {
            AlertHelper.raiseAlert("You need to enter an integer here...");
            setButtonDisable(false);
        }
    }
    
    private void setButtonDisable(boolean isDisabled) {
        addByNameButton.setDisable(isDisabled);
        nameSearchField.setDisable(isDisabled);
        addByPasscodeButton.setDisable(isDisabled);
        passcodeSearchField.setDisable(isDisabled);
        addManuallyButton.setDisable(isDisabled);
    }
}
