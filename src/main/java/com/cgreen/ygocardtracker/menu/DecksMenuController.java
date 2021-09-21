package com.cgreen.ygocardtracker.menu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.DeckDao;
import com.cgreen.ygocardtracker.deck.Deck;
import com.cgreen.ygocardtracker.util.AlertHelper;
import com.sun.javafx.scene.control.LabeledText;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DecksMenuController {
    private Stage stage;
    private CardDao cardDao;
    private CardInfoDao cardInfoDao;
    private DeckDao deckDao;
    private ObservableList<Card> unassignedCardsList, deckCardsList, sideDeckList; 
    @FXML
    private ChoiceBox<Deck> deckChoiceBox;
    @FXML
    ListView<Card> unassignedCardsListView, deckCardsListView, sideDeckListView;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init() {
        cardDao = new CardDao();
        cardInfoDao = new CardInfoDao();
        deckDao = new DeckDao();
        unassignedCardsList = FXCollections.observableArrayList();
        deckCardsList = FXCollections.observableArrayList();
        sideDeckList = FXCollections.observableArrayList();
        try {
            ObservableList<Card> allCards = cardDao.getAll();
            cardInfoDao.setCardInfosToCards(allCards);
            FXCollections.sort(allCards, new Comparator<Card>( ) {
                @Override
                public int compare(Card c1, Card c2) {
                    return c1.compareTo(c2);
                }
            });
            for (Card card : allCards) {
                if (card.getDeckId() == 1) {
                    unassignedCardsList.add(card);
                }
            }
            unassignedCardsListView.setItems(unassignedCardsList);
            deckDao = new DeckDao();
            deckChoiceBox.setItems(deckDao.getAll());
        } catch (SQLException e) {
            AlertHelper.raiseAlert(e.getMessage());
            stage.close();
        }
    }
    public void handleAddDeckButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(CardInfoViewController.class.getClassLoader().getResource("deck_name_menu.fxml"));
        Parent parent;
        try {
            parent = loader.load();
            Stage cardInfoStage = new Stage();
            cardInfoStage.setScene(new Scene(parent));
            DeckNameMenuController dnmc = loader.getController();
            dnmc.setStage(cardInfoStage);
            dnmc.init(deckDao);
            cardInfoStage.initModality(Modality.APPLICATION_MODAL);
            cardInfoStage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AlertHelper.raiseAlert("Error displaying card information.");
        } 
    }
    
    @FXML
    public void handleViewUnassignedCardInfoAction(MouseEvent event) throws IOException {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Card c = unassignedCardsListView.getSelectionModel().getSelectedItem();
            if (c != null) {
                showCardInfoView(c);
            }
        }
    }
    
    @FXML
    public void handleViewDeckCardInfoAction(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Card c = deckCardsListView.getSelectionModel().getSelectedItem();
            if (c != null) {
                showCardInfoView(c);
            }
        }
    }
    
    @FXML
    public void handleViewSideDeckCardInfoAction(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            Card c = sideDeckListView.getSelectionModel().getSelectedItem();
            if (c != null) {
                showCardInfoView(c);
            }
        }
    }
    
    private void showCardInfoView(Card c) {
        FXMLLoader loader = new FXMLLoader(CardInfoViewController.class.getClassLoader().getResource("cardinfo_popup.fxml"));
        Parent parent;
        try {
            parent = loader.load();
            Stage cardInfoStage = new Stage();
            cardInfoStage.setScene(new Scene(parent));
            CardInfoViewController civc = loader.getController();
            civc.init(c);
            civc.setStage(cardInfoStage);
            cardInfoStage.initModality(Modality.APPLICATION_MODAL);
            cardInfoStage.showAndWait();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AlertHelper.raiseAlert("Error displaying card information.");
        }        
        
    }
}
