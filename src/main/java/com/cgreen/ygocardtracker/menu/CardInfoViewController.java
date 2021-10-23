package com.cgreen.ygocardtracker.menu;

import java.io.File;
import java.io.InputStream;

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
                InputStream is = AllCardsController.class.getClassLoader().getResourceAsStream("default-card-image-421x614.png");
                if (is == null) {
                    // TODO: Log that a resource was not found!!
                    cardImageView.setImage(null);
                } else {
                    Image img = new Image(is);
                    cardImageView.setImage(img);
                }
            }
            cardSetText.setText(card.getSetCode());
        }
    }
}
