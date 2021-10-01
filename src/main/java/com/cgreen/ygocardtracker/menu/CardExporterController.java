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

public class CardExporterController {
    private Stage stage;
    @FXML
    private CheckBox jsonCheckbox, csvCheckbox, txtCheckbox;
    public void setStage(Stage stage) {
        this.stage = stage;
        // TODO: Remove this line once CSV export is ready
        csvCheckbox.setDisable(true);
    }
    
    @FXML
    public void handleOkButtonAction(ActionEvent event) {
        if (jsonCheckbox.isSelected() || csvCheckbox.isSelected() || txtCheckbox.isSelected()) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select a save location");
            File dir = chooser.showDialog(stage);
            if (dir != null) {
                if (dir.isDirectory()) {
                    try {
                        CardExporter.exportCollection(dir, jsonCheckbox.isSelected(), csvCheckbox.isSelected(), txtCheckbox.isSelected());
                        Alert complete = new Alert(AlertType.INFORMATION, "Card export complete!");
                        complete.showAndWait();
                        stage.hide();
                    } catch (SQLException e) {
                        AlertHelper.raiseAlert("Export failed due to a database error.");
                    } catch (IOException e) {
                        AlertHelper.raiseAlert("Error exporting collection: " + e.getMessage());
                    }
                } else {
                    AlertHelper.raiseAlert("Please select a directory.");
                }
            }
            event.consume();
        } else {
            event.consume();
            stage.hide();
        }
    }
    
    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        event.consume();
        stage.close();
    }

}
