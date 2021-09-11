package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.data.Card;
import com.cgreen.ygocardtracker.dao.Dao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardDao implements Dao<Card> {
    
    private ObservableList<Card> collectionItems;
    
    public CardDao() {
        collectionItems = FXCollections.observableArrayList();
    }
    
    // SELECT
    @Override
    public ObservableList<Card> getAll() throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_table_query"));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {                
                Card card = new Card();
                card.setCardInfoIdCol(rs.getInt("card_info_id"));
                card.setDeckIdCol(rs.getInt("deck_id"));
                card.setSetCodeCol(rs.getString("set_code"));
                card.setInSideDeckCol(rs.getBoolean("in_side_deck"));
                card.setIsVirtualCol(rs.getBoolean("is_virtual"));
                card.setId(rs.getInt("ID"));
                collectionItems.add(card);
            }
        } catch (SQLException sqle) {
            throw sqle;
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
    @Override
    public void save(Card card) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_table_statement"));           
            stmt.setObject(1, Objects.requireNonNull(card.getCardInfoIdCol().getValue(), "Card info ID must have a value."));
            stmt.setObject(2, Objects.requireNonNull(card.getDeckIdCol().getValue(), "Deck ID must have a value."));
            stmt.setObject(3, card.getSetCodeCol().getValue());
            stmt.setObject(4, card.getInSideDeckCol().getValue());
            stmt.setObject(5, card.getIsVirtualCol().getValue());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                card.setId(rs.getInt(1));
            }
            collectionItems.add(card);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (IllegalArgumentException iae) {
            AlertHelper.raiseAlert(iae.getMessage());
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
    public void update(Card t, String[] params) throws SQLException {
        // TODO Auto-generated method stub    
    }

    @Override
    public void delete(Card card) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("delete_from_card_table_statement"));           
            stmt.setInt(1, card.getId());
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        collectionItems.remove(card);
    }

}
