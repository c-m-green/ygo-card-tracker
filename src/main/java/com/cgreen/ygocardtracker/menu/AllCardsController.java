package com.cgreen.ygocardtracker.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cgreen.ygocardtracker.dao.impl.DBCardInfo;
import com.cgreen.ygocardtracker.dao.impl.DBCardInfoDao;
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
    private DBCardInfoDao myTable;
    @FXML
    private TableView<DBCardInfo> tableView;
    @FXML
    private TableColumn<DBCardInfo, Integer> passcodeCol, cardTypeCol, variantCol, atkCol, defCol, levelCol, scaleCol, linkValCol;
    @FXML
    private TableColumn<DBCardInfo, String> nameCol, descCol, attributeCol, linkMarkersCol, imageCol, imageSmallCol;
    @FXML
    private TableColumn<DBCardInfo, Boolean> isFakeCol;
    //TEMP
    @FXML
    private Button deleteButton;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        try {
            myTable = new DBCardInfoDao();
            passcodeCol.setCellValueFactory(cellData -> cellData.getValue().getPasscodeCol());
            nameCol.setCellValueFactory(cellData -> cellData.getValue().getNameCol());
            descCol.setCellValueFactory(cellData -> cellData.getValue().getDescriptionCol());
            cardTypeCol.setCellValueFactory(cellData -> cellData.getValue().getCardTypeCol());
            variantCol.setCellValueFactory(cellData -> cellData.getValue().getVariantCol());
            attributeCol.setCellValueFactory(cellData -> cellData.getValue().getAttributeCol());
            atkCol.setCellValueFactory(cellData -> cellData.getValue().getAttackCol());
            defCol.setCellValueFactory(cellData -> cellData.getValue().getDefenseCol());
            levelCol.setCellValueFactory(cellData -> cellData.getValue().getLevelCol());
            scaleCol.setCellValueFactory(cellData -> cellData.getValue().getScaleCol());
            linkValCol.setCellValueFactory(cellData -> cellData.getValue().getLinkValueCol());
            linkMarkersCol.setCellValueFactory(cellData -> cellData.getValue().getLinkMarkersCol());
            imageCol.setCellValueFactory(cellData -> cellData.getValue().getImageLinkCol());
            imageSmallCol.setCellValueFactory(cellData -> cellData.getValue().getSmallImageLinkCol());
            isFakeCol.setCellValueFactory(cellData -> cellData.getValue().getIsFakeCol());
            tableView.setItems(myTable.getAll());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        DBCardInfo cardInfo = tableView.getSelectionModel().getSelectedItem();
        try {
            myTable.delete(cardInfo);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}