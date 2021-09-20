package com.cgreen.ygocardtracker.menu;

import java.sql.SQLException;
import java.util.Comparator;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.DeckDao;
import com.cgreen.ygocardtracker.deck.Deck;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DecksMenuController {
    private Stage stage;
    private CardDao cardDao;
    private CardInfoDao cardInfoDao;
    private DeckDao deckDao;
    private ObservableList<Deck> allDecks;
    @FXML
    ListView<Card> unassignedCardsList, deckCardsList, sideDeckList;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init() {
        cardDao = new CardDao();
        cardInfoDao = new CardInfoDao();
        deckDao = new DeckDao();
        try {
            ObservableList<Card> allCards = cardDao.getAll();
            FXCollections.sort(allCards, new Comparator<Card>( ) {
                @Override
                public int compare(Card c1, Card c2) {
                    return c1.compareTo(c2);
                }
            });
            cardInfoDao.setCardInfosToCards(allCards);
            unassignedCardsList.setItems(allCards);
            
        } catch (SQLException e) {
            AlertHelper.raiseAlert(e.getMessage());
        }
    }
}
