package com.cgreen.ygocardtracker.menu;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.util.AlertHelper;
import com.cgreen.ygocardtracker.util.CardConfirmer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CardConfirmerController {
    // TODO: Give this a private CardConfirmer to initialize in init()
    private Stage stage;
    private CardConfirmer cardConfirmer;
    @FXML
    private Button saveButton, cancelButton;
    @FXML
    private ChoiceBox<String> passcodeChoiceBox, cardSetChoiceBox;
    @FXML
    private ImageView imageView;
    
    public CardConfirmerController() { }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init(List<CardInfo> cardInfos) throws SQLException {
        if (cardInfos.isEmpty()) {
            throw new IllegalArgumentException("No card information passed to confirmation screen!");
        }
        cardConfirmer = new CardConfirmer(cardInfos);
        ObservableList<String> passcodeChoices = FXCollections.observableArrayList();
        for (CardInfo info : cardInfos) {
            passcodeChoices.add(String.format("%08d", info.getPasscode()));            
        }
        passcodeChoiceBox.setItems(passcodeChoices);
        passcodeChoiceBox.setValue(passcodeChoices.get(0));
        cardSetChoiceBox.setItems(cardConfirmer.getCardSetChoicesForPasscode(passcodeChoices.get(0)));
        // TODO: Handle case where image URL is empty, null, or links to nothing.
        imageView.setImage(new Image(new File(cardConfirmer.getImageLinkForPasscode(passcodeChoices.get(0))).toURI().toString()));
    }
    
    public void handlePasscodeSelectAction(ActionEvent event) {
        cardSetChoiceBox.setItems(cardConfirmer.getCardSetChoicesForPasscode(passcodeChoiceBox.getValue()));
        // TODO: Handle case where image URL is empty, null, or links to nothing.
        imageView.setImage(new Image(new File(cardConfirmer.getImageLinkForPasscode(passcodeChoiceBox.getValue())).toURI().toString()));
    }
    
    public void handleSaveButtonAction(ActionEvent event) {
        if (cardSetChoiceBox.getValue() == null || cardSetChoiceBox.getValue().isEmpty()) {
            AlertHelper.raiseAlert("Please select a set code for this card before saving.");
        } else {
            saveButton.setDisable(true);
            Integer passcode = Integer.parseInt(passcodeChoiceBox.getValue());
            Card card = new Card();
            card.setDeckId(1);
            card.setInSideDeck(false);
            card.setIsVirtual(false);
            card.setSetCode(cardSetChoiceBox.getValue());
            CardDao dao = new CardDao();
            try {
                dao.save(card, passcode);
                AlertHelper.raiseAlert("Success", "Card saved successfully", "Saved 1 of " + passcode + " to collection.");
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                AlertHelper.raiseAlert("Error saving to the database.");
            } finally {
                saveButton.setDisable(false);
            }
        }
    }
    
    public void handleCancelButtonAction(ActionEvent event) {
        stage.close();
    }
}
