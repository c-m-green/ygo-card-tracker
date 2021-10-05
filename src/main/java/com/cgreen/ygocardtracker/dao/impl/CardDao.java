package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.Card;
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
                card.setCardInfoId(rs.getInt("card_info_id"));
                card.setDeckId(rs.getInt("deck_id"));
                card.setGroupId(rs.getInt("group_id"));
                card.setSetCode(rs.getString("set_code"));
                card.setInSideDeck(rs.getBoolean("in_side_deck"));
                card.setIsVirtual(rs.getBoolean("is_virtual"));
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
    
    // SELECT
    public Card getCardByPasscode(Integer passcode) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        Card card = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_by_passcode_query"));
            stmt.setObject(1, Objects.requireNonNull(passcode, "Passcode must have a value."));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();                
            card = new Card();
            card.setCardInfoId(rs.getInt("card_info_id"));
            card.setDeckId(rs.getInt("deck_id"));
            card.setGroupId(rs.getInt("group_id"));
            card.setSetCode(rs.getString("set_code"));
            card.setInSideDeck(rs.getBoolean("in_side_deck"));
            card.setIsVirtual(rs.getBoolean("is_virtual"));
            card.setId(rs.getInt("ID"));
            collectionItems.add(card);
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
        return card; 
    }
    
    // SELECT
    public ObservableList<Card> getCardsByDeckId(Integer deckId, boolean inSideDeck) throws SQLException {
        ObservableList<Card> out = FXCollections.observableArrayList();
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        Card card = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_by_deck_query"));
            stmt.setObject(1, Objects.requireNonNull(deckId, "A deck must be indicated."));
            stmt.setObject(2, inSideDeck);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {                
                card = new Card();
                card.setCardInfoId(rs.getInt("card_info_id"));
                card.setDeckId(rs.getInt("deck_id"));
                card.setGroupId(rs.getInt("group_id"));
                card.setSetCode(rs.getString("set_code"));
                card.setInSideDeck(rs.getBoolean("in_side_deck"));
                card.setIsVirtual(rs.getBoolean("is_virtual"));
                card.setId(rs.getInt("ID"));
                out.add(card);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return out; 
    }

    // INSERT
    @Override
    public void save(Card card) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_table_statement"), Statement.RETURN_GENERATED_KEYS);           
            stmt.setObject(1, Objects.requireNonNull(card.getCardInfoId(), "Card info ID must have a value."));
            stmt.setObject(2, Objects.requireNonNull(card.getDeckId(), "Deck ID must have a value."));
            stmt.setObject(3, Objects.requireNonNull(card.getGroupId(), "Group ID must have a value."));
            stmt.setObject(4, card.getSetCode());
            stmt.setObject(5, card.inSideDeck());
            stmt.setObject(6, card.isVirtual());
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
    
    // INSERT
    public void save(Card card, Integer passcode) throws SQLException {
        if (passcode == 100000000) {
            // TODO: Maybe handle this more gracefully?
            throw new IllegalArgumentException("Invalid passcode " + passcode);
        }
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_table_by_passcode_statement"), Statement.RETURN_GENERATED_KEYS);           
            stmt.setObject(1, Objects.requireNonNull(passcode, "Passcode must have a value."));
            stmt.setObject(2, Objects.requireNonNull(card.getDeckId(), "Deck ID must have a value."));
            stmt.setObject(3, Objects.requireNonNull(card.getGroupId(), "Group ID must have a value."));
            stmt.setObject(4, card.getSetCode());
            stmt.setObject(5, card.inSideDeck());
            stmt.setObject(6, card.isVirtual());
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
    
    // UPDATE
    public void updateDeckId(Card c, int deckId) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("update_card_deck_id_statement"));           
            stmt.setInt(1, deckId);
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            c.setDeckId(deckId);
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
    }
    
    // UPDATE
    public void updateGroupId(Card c, int groupId) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("update_card_group_id_statement"));
        ){
            stmt.setInt(1, groupId);
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            c.setGroupId(groupId);
        }
    }
    
 // UPDATE
    public void updateInSideDeck(Card c, boolean inSideDeck) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("update_card_side_deck_statement"));           
            stmt.setBoolean(1, inSideDeck);
            stmt.setInt(2, c.getId());
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
    }
    
    // DELETE
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
