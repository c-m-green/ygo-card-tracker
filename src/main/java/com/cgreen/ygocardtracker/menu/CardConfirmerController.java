package com.cgreen.ygocardtracker.menu;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.Group;
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
    private Stage stage;
    private CardConfirmer cardConfirmer;
    @FXML
    private Button saveButton, cancelButton;
    @FXML
    private ChoiceBox<String> passcodeChoiceBox, cardSetChoiceBox;
    @FXML
    private ChoiceBox<Group> groupChoiceBox;
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
        groupChoiceBox.setItems(cardConfirmer.getAllGroups());
        String imgLink = cardConfirmer.getImageLinkForPasscode(passcodeChoices.get(0));
        if (imgLink == null || imgLink.isBlank()) {
            displayDefaultImage();
        } else {
            File imgFile = new File(imgLink);
            if (imgFile.exists()) {
                imageView.setImage(new Image(new File(imgLink).toURI().toString()));
            } else {
                displayDefaultImage();
            }
        }
    }
    
    public void handlePasscodeSelectAction(ActionEvent event) {
        cardSetChoiceBox.setItems(cardConfirmer.getCardSetChoicesForPasscode(passcodeChoiceBox.getValue()));
        String imgLink = cardConfirmer.getImageLinkForPasscode(passcodeChoiceBox.getValue());
        if (imgLink == null || imgLink.isBlank()) {
            displayDefaultImage();
        } else {
            File imgFile = new File(imgLink);
            if (imgFile.exists()) {
                imageView.setImage(new Image(new File(imgLink).toURI().toString()));
            } else {
                displayDefaultImage();
            }
        }
    }
    
    public void handleSaveButtonAction(ActionEvent event) {
        if (cardSetChoiceBox.getValue() == null || cardSetChoiceBox.getValue().isEmpty()) {
            AlertHelper.raiseAlert("Please select a set code for this card before saving.");
        } else {
            saveButton.setDisable(true);
            Integer passcode = Integer.parseInt(passcodeChoiceBox.getValue());
            Card card = new Card();
            card.setDeckId(1);
            if (groupChoiceBox.getValue() == null) {
                card.setGroupId(1);
            } else {
                card.setGroupId(groupChoiceBox.getValue().getId());
            }
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

    private void displayDefaultImage() {
        InputStream is = CardConfirmerController.class.getClassLoader().getResourceAsStream("default-card-image-421x614.png");
        if (is == null) {
            // TODO: Log that a resource was not found!!
            imageView.setImage(null);
        } else {
            Image img = new Image(is);
            imageView.setImage(img);
        }
    }
}
