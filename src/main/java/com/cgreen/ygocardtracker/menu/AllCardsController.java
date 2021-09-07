package com.cgreen.ygocardtracker.menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cgreen.ygocardtracker.dao.impl.DBCardInfo;
import com.cgreen.ygocardtracker.dao.impl.DBCardInfoDao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AllCardsController {

    private Stage stage;
    private DBCardInfoDao myTable;
    @FXML
    private TableView<DBCardInfo> tableView;
    @FXML
    private TextField newCol1Text, newCol2Text, newCol3Text, newCol4Text;
    @FXML
    //private TableColumn col1, col2, col3, col4;
    
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
            myTable = new DBCardInfoDao();
            while(rs.next()) {
                String[] params = new String[4];
                params[0] = rs.getString("COL1");
                params[1] = rs.getInt("COL2") + "";
                params[2] = rs.getString("COL3");
                params[3] = rs.getString("COL4");
               DBCardInfo c = new DBCardInfo();
                c.setId(rs.getInt("ID"));
                myTable.update(c, params);
            }
            
            stmt.close();
            conn.close();
            /*col1.setCellValueFactory(new PropertyValueFactory<DBCardInfo, String>("col1"));
            col2.setCellValueFactory(new PropertyValueFactory<DBCardInfo, Integer>("col2"));
            col3.setCellValueFactory(new PropertyValueFactory<DBCardInfo, String>("col3"));
            col4.setCellValueFactory(new PropertyValueFactory<DBCardInfo, String>("col4"));*/            
            tableView.setItems(myTable.getAll());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}