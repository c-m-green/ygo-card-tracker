package com.cgreen.ygocardtracker.menu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.DeckDao;
import com.cgreen.ygocardtracker.deck.Deck;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button addCardButton, moveCardButton, removeCardButton;
    private List<Button> menuButtons;
    @FXML
    private ChoiceBox<Deck> deckChoiceBox;
    @FXML
    private ListView<Card> unassignedCardsListView, deckCardsListView, sideDeckListView;
    
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
        menuButtons = new ArrayList<Button>();
        menuButtons.add(addCardButton);
        menuButtons.add(moveCardButton);
        menuButtons.add(removeCardButton);
        try {
            ObservableList<Card> allCards = cardDao.getAll();
            cardInfoDao.setCardInfosToCards(allCards);
            alphabetizeCards(allCards);
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
    
    public void handleDeckChoiceAction(ActionEvent event) {
        setButtonDisable(true);
        Deck d = deckChoiceBox.getValue();
        if (d != null && d.getId() > 1) {
            try {
                deckCardsList = cardDao.getCardsByDeckId(d.getId(), false);
                sideDeckList = cardDao.getCardsByDeckId(d.getId(), true);
                cardInfoDao.setCardInfosToCards(deckCardsList);
                cardInfoDao.setCardInfosToCards(sideDeckList);
                alphabetizeCards(deckCardsList);
                alphabetizeCards(sideDeckList);
                deckCardsListView.setItems(deckCardsList);
                sideDeckListView.setItems(sideDeckList);
            } catch (SQLException e) {
                AlertHelper.raiseAlert(e.getClass() + e.getMessage());
            }
        }
        setButtonDisable(false);
    }
    
    public void handleDeleteDeckButtonAction(ActionEvent event) {
        Deck d = deckChoiceBox.getValue();
        if (d == null) {
            AlertHelper.raiseAlert("No deck found!");
        } else if (d.getId() == 1) {
            AlertHelper.raiseAlert("Cannot delete the default deck.");
        } else {
            if (!deckCardsList.isEmpty() || !sideDeckList.isEmpty()) {
                AlertHelper.raiseAlert("Please remove all cards from this deck before attempting to delete it.");
            } else {
                Alert confirm = new Alert(AlertType.CONFIRMATION, "Really delete " + d.getName() + "?", ButtonType.YES, ButtonType.NO);
                confirm.showAndWait();
                if (confirm.getResult() == ButtonType.YES) {
                    try {
                        deckDao.delete(d);
                    } catch (SQLException e) {
                        AlertHelper.raiseAlert("Error deleting deck: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    public void handleAddCardButtonAction(ActionEvent event) {
        setButtonDisable(true);
        Deck d = deckChoiceBox.getValue();
        if (d == null || d.getId() <= 1) {
            AlertHelper.raiseAlert("Please select a deck first.");
        } else if (unassignedCardsListView.getSelectionModel().getSelectedItem() == null) {
            AlertHelper.raiseAlert("No card selected!");
        } else {
            Card c = unassignedCardsListView.getSelectionModel().getSelectedItem();
            try {
                cardDao.updateDeckId(c, d.getId());
                deckCardsList.add(c);
                unassignedCardsList.remove(c);
                alphabetizeCards(deckCardsList);
            } catch (SQLException e) {
                AlertHelper.raiseAlert("Error adding card.");
            }
        }
        setButtonDisable(false);
    }
    
    public void handleMoveCardButtonAction(ActionEvent event) {
        setButtonDisable(true);
        Alert choice = new Alert(AlertType.CONFIRMATION, "\"Yes\" moves to side deck, \"No\" moves from side deck\n\n(This is a temporary thing)", ButtonType.YES, ButtonType.NO);
        choice.showAndWait();
        try {
            if (choice.getResult() == ButtonType.YES) {
                Card c = deckCardsListView.getSelectionModel().getSelectedItem();
                if (c == null) {
                    AlertHelper.raiseAlert("No card is selected!");
                } else {
                    cardDao.updateInSideDeck(c, true);
                    sideDeckList.add(c);
                    deckCardsList.remove(c);
                    alphabetizeCards(sideDeckList);
                }
            } else if (choice.getResult() == ButtonType.NO) {
                Card c = sideDeckListView.getSelectionModel().getSelectedItem();
                if (c == null) {
                    AlertHelper.raiseAlert("No card is selected!");
                } else {
                    cardDao.updateInSideDeck(c, false);
                    deckCardsList.add(c);
                    sideDeckList.remove(c);
                    alphabetizeCards(deckCardsList);
                }
            }
        } catch (SQLException e) {
            AlertHelper.raiseAlert(e.getMessage());
        }
        setButtonDisable(false);
    }
    
    public void handleRemoveCardButtonAction(ActionEvent event) {
        setButtonDisable(true);
        Card c = deckCardsListView.getSelectionModel().getSelectedItem();
        if (c != null) {
            try {
                cardDao.updateDeckId(c, 1);
                deckCardsList.remove(c);
                unassignedCardsList.add(c);
                alphabetizeCards(unassignedCardsList);
            } catch (SQLException e) {
                AlertHelper.raiseAlert(e.getMessage());
            }
        }
        setButtonDisable(false);
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
    
    private void alphabetizeCards(ObservableList<Card> cards) {
        FXCollections.sort(cards, new Comparator<Card>( ) {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.compareTo(c2);
            }
        });
    }
    
    private void setButtonDisable(boolean isDisabled) {
        for (Button button : menuButtons) {
            button.setDisable(isDisabled);
        }
    }
}
