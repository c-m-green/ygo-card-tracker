package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.dao.Dao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.deck.Deck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeckDao implements Dao<Deck> {
    private ObservableList<Deck> collectionItems;
    
    public DeckDao() {
        collectionItems = FXCollections.observableArrayList();
    }
    
    @Override
    public ObservableList<Deck> getAll() throws SQLException {
        // TODO Auto-generated method stub
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_deck_table_query"));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {                
                Deck deck = new Deck();
                deck.setName(rs.getString("name"));
                deck.setNote(rs.getString("note"));
                deck.setId(rs.getInt("ID"));
                collectionItems.add(deck);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return collectionItems;
    }

 // INSERT
    public void save(Deck deck) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_deck_table_statement"), Statement.RETURN_GENERATED_KEYS);           
            stmt.setObject(1, Objects.requireNonNull(deck.getName(), "Deck must have a name."));
            stmt.setObject(2, Objects.requireNonNull(deck.getNote(), "Note must have a value."));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                deck.setId(rs.getInt(1));
            }
            collectionItems.add(deck);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void delete(Deck deck) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("delete_from_deck_table_statement"));           
            stmt.setInt(1, deck.getId());
            stmt.executeUpdate();
            collectionItems.remove(deck);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
