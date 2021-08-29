package com.cgreen.ygocardtracker;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    public void handleViewTableButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(MyTableController.class.getClassLoader().getResource("my_table.fxml"));
        Parent parent = loader.load();        
        stage.hide();
        
        Stage myTableStage = new Stage();
        myTableStage.setScene(new Scene(parent));
        MyTableController mtc = loader.getController();
        mtc.init();
        mtc.setStage(myTableStage);
        myTableStage.show();
    }
    
    @FXML
    public void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}
