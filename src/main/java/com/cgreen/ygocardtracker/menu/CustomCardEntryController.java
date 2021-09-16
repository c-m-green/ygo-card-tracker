package com.cgreen.ygocardtracker.menu;

import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class CustomCardEntryController {
    private Stage stage;
    @FXML
    private ChoiceBox<CardType> typeChoice;
    @FXML
    private ChoiceBox<CardVariant> variantChoice;
    @FXML
    private ChoiceBox<String> attributeChoice;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init() {
        for (int i = 0; i < CardType.getNumberOfCardTypes(); i++) {
            typeChoice.getItems().add(CardType.getCardType(i));
        }
        typeChoice.setValue(CardType.UNKNOWN);
        for (int i = 0; i < CardVariant.getNumberOfCardVariants(); i++) {
            variantChoice.getItems().add(CardVariant.getCardVariant(i));
        }
        variantChoice.setValue(CardVariant.UNKNOWN);
        // TODO: Maybe hardcode these somewhere else?
        ObservableList<String> attributes = FXCollections.observableArrayList();
        attributes.add("DARK");
        attributes.add("DIVINE");
        attributes.add("EARTH");
        attributes.add("FIRE");
        attributes.add("LAUGH");
        attributes.add("LIGHT");
        attributes.add("WATER");
        attributes.add("WIND");
        attributeChoice.setItems(attributes);
    }
}
