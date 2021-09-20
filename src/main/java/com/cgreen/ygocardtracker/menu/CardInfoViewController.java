package com.cgreen.ygocardtracker.menu;

import java.io.File;

import com.cgreen.ygocardtracker.card.Card;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardInfoViewController {
    private Stage stage;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Text cardSetText;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Received null value for card");
        } else {
            cardImageView.setImage(new Image(new File(card.getCardInfo().getImageLink()).toURI().toString()));
            cardSetText.setText(card.getSetCode());
        }
    }
}
