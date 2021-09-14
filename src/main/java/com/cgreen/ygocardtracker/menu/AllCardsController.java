package com.cgreen.ygocardtracker.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AllCardsController {

    private Stage stage;
    private CardDao cardDao;
    private CardInfoDao cardInfoDao;
    @FXML
    private ListView<Card> listView;
    @FXML
    private Button addCardButton, viewInfoButton, deleteButton, backButton;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        try {
            cardDao = new CardDao();
            cardInfoDao = new CardInfoDao();
            ObservableList<Card> allCards = cardDao.getAll();
            for (Card card : allCards) {
                card.setCardInfo(cardInfoDao.getCardInfoById(card.getCardInfoId()));
            }
            listView.setItems(allCards);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        Card card = listView.getSelectionModel().getSelectedItem();
        try {
            cardDao.delete(card);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleAddCardButtonAction() {
        
    }
    
    public void handleViewInfoButtonAction() {
        
    }
    
    public void handleBackButtonAction() {
        
    }
}