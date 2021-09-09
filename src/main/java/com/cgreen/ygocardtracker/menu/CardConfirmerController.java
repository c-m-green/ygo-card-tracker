package com.cgreen.ygocardtracker.menu;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;

import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.db.DatabaseManager;
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
            passcodeChoices.add(String.format("%08d", info.getPasscodeCol().getValue()));            
        }
        passcodeChoiceBox.setItems(passcodeChoices);
        passcodeChoiceBox.setValue(passcodeChoices.get(0));
        cardSetChoiceBox.setItems(cardConfirmer.getCardSetChoicesForPasscode(passcodeChoices.get(0)));
        imageView.setImage(new Image(new File(cardConfirmer.getImageLinkForPasscode(passcodeChoices.get(0))).toURI().toString()));
    }
    
    public void handlePasscodeSelectAction(ActionEvent event) {
        cardSetChoiceBox.setItems(cardConfirmer.getCardSetChoicesForPasscode(passcodeChoiceBox.getValue()));
        imageView.setImage(new Image(new File(cardConfirmer.getImageLinkForPasscode(passcodeChoiceBox.getValue())).toURI().toString()));
    }
    
    public void handleSaveButtonAction(ActionEvent event) {
        
    }
    
    public void handleCancelButtonAction(ActionEvent event) {
        stage.close();
    }
}
