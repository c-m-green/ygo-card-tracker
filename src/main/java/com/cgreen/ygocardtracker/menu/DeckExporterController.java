package com.cgreen.ygocardtracker.menu;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.cgreen.ygocardtracker.db.exports.CardExporter;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DeckExporterController {
    private Stage stage;
    @FXML
    private CheckBox jsonCheckbox, ydkCheckbox;
    public void setStage(Stage stage) {
        this.stage = stage;
        // TODO: Remove this line once YDK export is ready
        ydkCheckbox.setDisable(true);
    }
    
    @FXML
    public void handleOkButtonAction(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a save location");
        File dir = chooser.showDialog(stage);
        if (jsonCheckbox.isSelected()) {
            try {
                CardExporter.exportCollection(dir, true, false, false);
                Alert complete = new Alert(AlertType.INFORMATION, "Successfully exported decks to JSON.");
                complete.showAndWait();
            } catch (SQLException e) {
                AlertHelper.raiseAlert("Export failed due to a database error.");
            } catch (IOException e) {
                AlertHelper.raiseAlert("Error exporting collection: " + e.getMessage());
            }
        }
        if (ydkCheckbox.isSelected()) {
            // TODO
        }
        event.consume();
        stage.hide();
    }
    
    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        event.consume();
        stage.close();
    }
}
