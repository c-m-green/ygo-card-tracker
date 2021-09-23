package com.cgreen.ygocardtracker.menu;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllCardsController {

    private Stage stage;
    private CardDao cardDao;
    private CardInfoDao cardInfoDao;
    @FXML
    private ListView<Card> listView;
    @FXML
    private Button addCardButton, deleteButton, backButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Text cardNameText, setCodeText, deckText;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        try {
            cardDao = new CardDao();
            cardInfoDao = new CardInfoDao();
            ObservableList<Card> allCards = cardDao.getAll();
            cardInfoDao.setCardInfosToCards(allCards);
            Comparator<Card> comparator = new Comparator<Card>() {
                @Override
                public int compare(Card c1, Card c2) {
                    return c1.compareTo(c2);
                }
            };
            FXCollections.sort(allCards, comparator);
            listView.setItems(allCards);
            wipeCurrentCardView();
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Card>() {
                @Override
                public void changed(ObservableValue<? extends Card> observable, Card oldValue, Card newValue) {
                    updateCurrentCardView(newValue);
                }
            });
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        Card card = listView.getSelectionModel().getSelectedItem();
        try {
            cardDao.delete(card);
            wipeCurrentCardView();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        event.consume();
    }
    
    public void handleAddCardButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AddCardsMenuController.class.getClassLoader().getResource("add_cards_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage addCardsStage = new Stage();
        addCardsStage.setScene(new Scene(parent));
        AddCardsMenuController acc = loader.getController();
        acc.setStage(addCardsStage);
        addCardsStage.initModality(Modality.APPLICATION_MODAL);
        addCardsStage.showAndWait();
        init();
        event.consume();
    }
    
    public void handleBackButtonAction(ActionEvent event) {
        
    }
    
    public void handleListClickEvent() {
        Card card = listView.getSelectionModel().getSelectedItem();
        updateCurrentCardView(card);
    }
    
    private void updateCurrentCardView(Card newValue) {
        if (newValue == null || newValue.getCardInfo() == null) {
            // TODO: Display default card image
        } else {
            imageView.setImage(new Image(new File(newValue.getCardInfo().getImageLink()).toURI().toString()));
            cardNameText.setText(newValue.getCardInfo().getName());
            setCodeText.setText(newValue.getSetCode());
            if (newValue.getDeckId() > 1) {
                deckText.setText("Assigned to deck");
            } else {
                deckText.setText("Unassigned");
            }
        }
    }
    
    private void wipeCurrentCardView() {
        cardNameText.setText("");
        setCodeText.setText("");
        deckText.setText("");
        imageView.setImage(null);
    }
    
    
}