package com.cgreen.ygocardtracker;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    
    protected void init() {
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
    
    protected boolean testConnection(String username, String password) {
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
    
    public boolean initializeTables() {
        try {
            createTable();
        } catch (SQLException sqle) {
            return false;
        }
        return true;
    }
    
    private void createTable() throws SQLException {
        // TODO: Insert driver and db url from props file
        Connection conn = DriverManager.getConnection("jdbc:h2:./ygodb", user.getUsername(), user.getPassword());
        String sqlCreate = "CREATE TABLE IF NOT EXISTS MyTable"
                + " (id int NOT NULL AUTO_INCREMENT,"
                + " col1 tinytext,"
                + " col2 smallint(3),"
                + " col3 tinytext,"
                + " col4 tinytext,"
                + " PRIMARY KEY (id))";
        Statement stmt = conn.createStatement();
        stmt.execute(sqlCreate);
        stmt.close();
        conn.close();
    }
    
    protected Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(props.getProperty("driver") + ":" + props.getProperty("db_url"),
                user.getUsername(), user.getPassword());
    }
    
    private Connection connectToDatabase(String username, String password) throws SQLException {
        return DriverManager.getConnection(props.getProperty("driver") + ":" + props.getProperty("db_url"),
                username, password);
    }
}
