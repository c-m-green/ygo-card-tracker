package com.cgreen.ygocardtracker.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.cgreen.ygocardtracker.util.AlertHelper;

public class DatabaseManager {
    private static DatabaseManager dbm;
    private static final String DB_PROPS_FILENAME = "db.properties";
    
    private User user;
    private Properties props;
    
    private DatabaseManager() { }
    public static DatabaseManager getDatabaseManager() {
        if (dbm == null) {
            dbm = new DatabaseManager();
        }
        return dbm;
    }
    
    public void init() {
        // TODO: Clean this up.
        if (props == null) {
            try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream(DB_PROPS_FILENAME)) {
                props = new Properties();
                props.load(in);
            } catch (IOException ioe) {
                AlertHelper.raiseAlert(ioe.getMessage());
            }
        }
    }
    
    public boolean testConnection(String username, String password) {
        try {
            Connection testConn = connectToDatabase(username, password);
            testConn.close();
        } catch (Exception e) {
            AlertHelper.raiseAlert(e.getMessage());
            return false;
        }
        return true;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void initializeTables() {
        try {
            createSetCodeTable();
            createCardInfoTable();
            createDeckTable();
            createGroupTable();
            createMyCardCollectionTable();
        } catch (SQLException sqle) {
            AlertHelper.raiseAlert(sqle.getMessage());
            sqle.printStackTrace();
        }
    }
    
    private void createCardInfoTable() throws SQLException {
        // TODO: Insert driver and db url from props file
        Connection conn = connectToDatabase();
        String sqlCreate = Queries.getQuery("create_card_info_table_statement");
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        stmt.close();
        conn.close();
    }
    
    private void createDeckTable() throws SQLException {
        Connection conn = connectToDatabase();
        String sqlCreate = Queries.getQuery("create_deck_table_statement");
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        // TODO: Find a less hacky was to insert this row *if it doesn't exist*.
        String sqlQuery = Queries.getQuery("select_deck_table_query");
        stmt.execute(sqlQuery);
        ResultSet rs = stmt.getResultSet();
        if (!rs.isBeforeFirst()) {
            String deckCreate = Queries.getQuery("insert_default_deck_statement");
            stmt.execute(deckCreate);
        }
        stmt.close();
        conn.close();
    }
    
    private void createMyCardCollectionTable() throws SQLException {
        Connection conn = connectToDatabase();
        String sqlCreate = Queries.getQuery("create_my_card_collection_table_statement");
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        stmt.close();
        conn.close();
    }
    
    private void createSetCodeTable() throws SQLException {
        Connection conn = connectToDatabase();
        String sqlCreate = Queries.getQuery("create_set_code_table_statement");
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        stmt.close();
        conn.close();
    }
    
    private void createGroupTable() throws SQLException {
        Connection conn = connectToDatabase();
        String sqlCreate = Queries.getQuery("create_group_table_statement");
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        // TODO: Find a less hacky was to insert this row *if it doesn't exist*.
        String sqlQuery = Queries.getQuery("select_group_table_query");
        stmt.execute(sqlQuery);
        ResultSet rs = stmt.getResultSet();
        if (!rs.isBeforeFirst()) {
            String groupCreate = Queries.getQuery("insert_default_group_statement");
            stmt.execute(groupCreate);
        }
        stmt.close();
        conn.close();
    }
    
    public Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(props.getProperty("driver") + ":" + props.getProperty("db_url"),
                user.getUsername(), user.getPassword());
    }
    
    private Connection connectToDatabase(String username, String password) throws SQLException {
        return DriverManager.getConnection(props.getProperty("driver") + ":" + props.getProperty("db_url"),
                username, password);
    }
}
