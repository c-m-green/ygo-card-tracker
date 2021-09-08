package com.cgreen.ygocardtracker.menu;

import java.sql.SQLException;

import org.json.JSONArray;

import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.util.CardConfirmer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class CardConfirmerController {
    // TODO: Give this a private CardConfirmer to initialize in init()
    private Stage stage;
    private CardConfirmer cardConfirmer;
    @FXML
    private Button saveButton, cancelButton;
    @FXML
    private ChoiceBox idChoiceBox;
    
    public CardConfirmerController() { }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init() throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();        
        cardConfirmer = new CardConfirmer();
    }
    
    public void handleSaveButtonAction(ActionEvent event) {
        
    }
    
    public void handleCancelButtonAction(ActionEvent event) {
        stage.close();
    }
}
