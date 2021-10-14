package com.cgreen.ygocardtracker.menu;

import com.cgreen.ygocardtracker.db.exports.CardImporter;
import com.cgreen.ygocardtracker.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class CsvImportController {
    private Stage stage;
    private File file;
    @FXML
    private Button okButton, cancelButton;
    @FXML
    private VBox vbox;
    @FXML
    private RadioButton noGroupRadioButton, newGroupRadioButton;
    @FXML
    private TextField groupNameTextField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(File file) {
        this.file = file;
        ToggleGroup group = new ToggleGroup();
        noGroupRadioButton.setToggleGroup(group);
        newGroupRadioButton.setToggleGroup(group);
        noGroupRadioButton.setSelected(true);
    }

    public void handleOkButtonAction(ActionEvent event) {
        vbox.setDisable(true);
        if (newGroupRadioButton.isSelected()) {
            String groupName = groupNameTextField.getText();
            if (groupName == null || groupName.isBlank()) {
                AlertHelper.raiseAlert("No group name was entered.");
            } else {
                CardImporter.importCsv(file, groupName);
            }
        } else {
            CardImporter.importCsv(file, null);
        }
        vbox.setDisable(false);
        event.consume();
        stage.close();
    }

    public void handleCancelButtonAction(ActionEvent event) {
        event.consume();
        stage.close();
    }
}
