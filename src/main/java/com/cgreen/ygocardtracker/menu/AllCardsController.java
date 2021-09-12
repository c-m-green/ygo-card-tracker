package com.cgreen.ygocardtracker.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cgreen.ygocardtracker.card.data.Card;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AllCardsController {

    private Stage stage;
    private CardDao myTable;
    @FXML
    private TableView<Card> tableView;
    @FXML
    private TableColumn<Card, Integer> cardInfoIdCol, deckIdCol;
    @FXML
    private TableColumn<Card, String> setCodeCol;
    @FXML
    private TableColumn<Card, Boolean> isInSideDeckCol, isVirtualCol;
    //TEMP
    @FXML
    private Button deleteButton;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        try {
            myTable = new CardDao();
            cardInfoIdCol.setCellValueFactory(cellData -> cellData.getValue().getCardInfoIdCol());
            deckIdCol.setCellValueFactory(cellData -> cellData.getValue().getDeckIdCol());
            setCodeCol.setCellValueFactory(cellData -> cellData.getValue().getSetCodeCol());
            isInSideDeckCol.setCellValueFactory(cellData -> cellData.getValue().getInSideDeckCol());
            isVirtualCol.setCellValueFactory(cellData -> cellData.getValue().getIsVirtualCol());
            tableView.setItems(myTable.getAll());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        Card card = tableView.getSelectionModel().getSelectedItem();
        try {
            myTable.delete(card);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}