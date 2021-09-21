package com.cgreen.ygocardtracker.menu;

import java.sql.SQLException;

import com.cgreen.ygocardtracker.dao.impl.DeckDao;
import com.cgreen.ygocardtracker.deck.Deck;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DeckNameMenuController {
    private Stage stage;
    private DeckDao deckDao;
    @FXML
    private TextField nameField;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init(DeckDao deckDao) {
        this.deckDao = deckDao;
    }
    
    public void handleOKButtonAction() {
        String deckName = nameField.getText();
        if (deckName == null || deckName.isBlank()) {
            AlertHelper.raiseAlert("Please enter something...");
        } else {
            Deck deck = new Deck();
            deck.setName(deckName);
            try {
                deckDao.save(deck);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                AlertHelper.raiseAlert("Failed to save this deck for some reason.");
            }
        }
        stage.close();
    }
    
    public void handleCancelButtonAction() {
        stage.close();
    }
}
