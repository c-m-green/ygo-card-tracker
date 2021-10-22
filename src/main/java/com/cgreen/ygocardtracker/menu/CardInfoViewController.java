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
            File imgFile = new File(card.getCardInfo().getImageLink());
            if (imgFile.exists()) {
                Image img = new Image(imgFile.toURI().toString());
                cardImageView.setImage(img);
            } else {
                try {
                    File imgNotFound = new File(CardInfoViewController.class.getClassLoader().getResource("default-card-image-421x614.png").getFile());
                    Image img = new Image(imgNotFound.toURI().toString());
                    cardImageView.setImage(img);
                } catch (NullPointerException npe) {
                    // TODO: Log this
                    cardImageView.setImage(null);
                }
            }
            cardSetText.setText(card.getSetCode());
        }
    }
}
