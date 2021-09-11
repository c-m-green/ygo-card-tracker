package com.cgreen.ygocardtracker.menu;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CollectionMenuController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    public void handleViewAllCardInfoButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AllCardInfoController.class.getClassLoader().getResource("all_card_info.fxml"));
        Parent parent = loader.load();        
        
        Stage allCardsStage = new Stage();
        allCardsStage.setScene(new Scene(parent));
        AllCardInfoController acc = loader.getController();
        acc.init();
        acc.setStage(allCardsStage);
        allCardsStage.initModality(Modality.APPLICATION_MODAL);
        allCardsStage.showAndWait();
    }
    
    @FXML
    public void handleViewAllCardsButtonAction(ActionEvent event) {
        System.out.println("This doesn't do anything yet.");
    }
    
    @FXML
    public void handleAddCardsToCollectionButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AddCardsMenuController.class.getClassLoader().getResource("add_cards_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage addCardsStage = new Stage();
        addCardsStage.setScene(new Scene(parent));
        AddCardsMenuController acc = loader.getController();
        acc.setStage(addCardsStage);
        addCardsStage.initModality(Modality.APPLICATION_MODAL);
        addCardsStage.showAndWait();
    }
}