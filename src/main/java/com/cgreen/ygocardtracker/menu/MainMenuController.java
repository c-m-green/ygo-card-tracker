package com.cgreen.ygocardtracker.menu;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.util.Version;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private Text verText;
    public void init() {
        try {
            verText.setText(verText.getText() + " ver. " + Version.getVersion());
        } catch (IOException e) {
            // TODO: Log this
        }
    }
    
    @FXML
    public void handleViewCollectionButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(CollectionMenuController.class.getClassLoader().getResource("collection_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage collectionStage = new Stage();
        collectionStage.setScene(new Scene(parent));
        CollectionMenuController cmc = loader.getController();
        cmc.setStage(collectionStage);
        collectionStage.initModality(Modality.APPLICATION_MODAL);
        collectionStage.showAndWait();
    }
    
    @FXML
    public void handleViewDecksButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(DecksMenuController.class.getClassLoader().getResource("decks_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage decksStage = new Stage();
        decksStage.setScene(new Scene(parent));
        DecksMenuController dmc = loader.getController();
        dmc.init();
        dmc.setStage(decksStage);
        decksStage.initModality(Modality.APPLICATION_MODAL);
        decksStage.showAndWait();
    }
    
    @FXML
    public void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }
}
