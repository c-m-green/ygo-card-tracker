package com.cgreen.ygocardtracker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AllCardsController {

    private Stage stage;
    private MyTableItemDao myTable;
    @FXML
    private TableView<MyTableItem> tableView;
    @FXML
    private Button addButton, deleteButton;
    @FXML
    private TextField newCol1Text, newCol2Text, newCol3Text, newCol4Text;
    @FXML
    private TableColumn col1, col2, col3, col4;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        // TODO: Clean this up.
        try {
            DatabaseManager dbm = DatabaseManager.getDatabaseManager();
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_table_query"));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            myTable = new MyTableItemDao();
            while(rs.next()) {
                String[] params = new String[4];
                params[0] = rs.getString("COL1");
                params[1] = rs.getInt("COL2") + "";
                params[2] = rs.getString("COL3");
                params[3] = rs.getString("COL4");
               MyTableItem c = new MyTableItem();
                c.setId(rs.getInt("ID"));
                myTable.update(c, params);
            }
            
            stmt.close();
            conn.close();
            col1.setCellValueFactory(new PropertyValueFactory<MyTableItem, String>("col1"));
            col2.setCellValueFactory(new PropertyValueFactory<MyTableItem, Integer>("col2"));
            col3.setCellValueFactory(new PropertyValueFactory<MyTableItem, String>("col3"));
            col4.setCellValueFactory(new PropertyValueFactory<MyTableItem, String>("col4"));            
            tableView.setItems(myTable.getAll());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @FXML
    public void handleAddButtonAction(ActionEvent event) {
        addButton.setDisable(true);
        MyTableItem c = new MyTableItem();
        myTable.update(c, new String[]{newCol1Text.getText(), newCol2Text.getText(), newCol3Text.getText(), newCol4Text.getText()});
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn;
        try {
            conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("insert_into_table_statement"), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, c.getCol1());
            stmt.setInt(2, c.getCol2());
            stmt.setString(3, c.getCol3());
            stmt.setString(4, c.getCol4());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }
            newCol1Text.clear();
            newCol2Text.clear();
            newCol3Text.clear();
            newCol4Text.clear();
            stmt.close();
            conn.close();
        } catch (SQLException | NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        addButton.setDisable(false);
    }
    
    @FXML
    public void handleDeleteButtonAction(ActionEvent event) {
        deleteButton.setDisable(true);
        MyTableItem c = tableView.getSelectionModel().getSelectedItem();
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn;
        try {
            conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("delete_from_table_statement"));
            stmt.setInt(1,  c.getId());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            myTable.delete(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        deleteButton.setDisable(false);
    }
}