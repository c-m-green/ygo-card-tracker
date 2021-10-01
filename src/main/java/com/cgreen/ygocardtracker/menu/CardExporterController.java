package com.cgreen.ygocardtracker.menu;

import java.sql.SQLException;

import com.cgreen.ygocardtracker.db.exports.CardExporter;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class CardExporterController {
    private Stage stage;
    @FXML
    private CheckBox jsonCheckbox, csvCheckbox, txtCheckbox;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    public void handleOkButtonAction(ActionEvent event) {
        try {
            CardExporter.exportCollection(stage, jsonCheckbox.isSelected(), csvCheckbox.isSelected(), txtCheckbox.isSelected());
            event.consume();
            stage.close();
        } catch (SQLException e) {
            AlertHelper.raiseAlert("Export failed due to a database error.");
        }
        event.consume();
    }
    
    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        event.consume();
        stage.close();
    }

}
