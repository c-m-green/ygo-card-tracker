package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.dao.Dao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardInfoDao implements Dao<CardInfo> {
    
    private ObservableList<CardInfo> collectionItems;
    
    public CardInfoDao() {
        collectionItems = FXCollections.observableArrayList();
    }
    // SELECT
    @Override
    public ObservableList<CardInfo> getAll() throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_info_table_query"));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {                
                CardInfo cardInfo = new CardInfo();
                cardInfo.setPasscodeCol(rs.getInt("passcode"));
                cardInfo.setNameCol(rs.getString("name"));
                cardInfo.setCardTypeCol(rs.getInt("card_type"));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(rs.getInt("variant"));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setImageLinkCol(rs.getString("image"));
                cardInfo.setSmallImageLinkCol(rs.getString("image_small"));
                cardInfo.setIsFakeCol(rs.getBoolean("is_fake"));
                cardInfo.setId(rs.getInt("ID"));
                collectionItems.add(cardInfo);
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
    public void save(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (c.getPasscodeCol().getValue() < 0 || c.getPasscodeCol().getValue() > 99999999) {
                throw new IllegalArgumentException("Card passcode out of range.");
            }
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_info_table_statement"));           
            stmt.setObject(1, Objects.requireNonNull(c.getPasscodeCol().getValue(), "Passcode must have a value."));
            stmt.setObject(2, Objects.requireNonNull(c.getNameCol().getValue(), "Card name must have a value."));
            stmt.setObject(3, c.getDescriptionCol().getValue());
            stmt.setObject(4, c.getCardTypeCol().getValue());
            stmt.setObject(5, c.getVariantCol().getValue());
            stmt.setObject(6, c.getAttributeCol().getValue());
            stmt.setObject(7, c.getAttackCol().getValue());
            stmt.setObject(8, c.getDefenseCol().getValue());
            stmt.setObject(9, c.getLevelCol().getValue());
            stmt.setObject(10, c.getScaleCol().getValue());
            stmt.setObject(11, c.getLinkValueCol().getValue());
            stmt.setObject(12, c.getLinkMarkersCol().getValue());
            stmt.setObject(13, c.getImageLinkCol().getValue());
            stmt.setObject(14, c.getSmallImageLinkCol().getValue());
            stmt.setObject(15, c.getIsFakeCol().getValue());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
            }
            collectionItems.add(c);
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
    @Override
    public void update(CardInfo c, String[] params) {
        /*c.setCol1(Objects.requireNonNull(params[0], "Column 1 must not be null."));
        c.setCol2(Objects.requireNonNull(Integer.parseInt(params[1]), "Column 2 must not be null."));
        c.setCol3(Objects.requireNonNull(params[2], "Column 3 must not be null."));
        c.setCol4(Objects.requireNonNull(params[3], "Column 4 must not be null."));*/
        collectionItems.add(c);
    }
    // DELETE
    @Override
    public void delete(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (c.getPasscodeCol().getValue() < 0 || c.getPasscodeCol().getValue() > 99999999) {
                throw new IllegalArgumentException("Card passcode out of range.");
            }
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("delete_from_card_info_table_statement"));           
            stmt.setInt(1, c.getId());
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
        collectionItems.remove(c);
    }

}
