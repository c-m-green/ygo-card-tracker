package com.cgreen.ygocardtracker;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertHelper {
    public static void raiseAlert(String titleText, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public static void raiseAlert(String contentText) {
        raiseAlert("Error", "An error was encountered.", contentText);
    }
}
