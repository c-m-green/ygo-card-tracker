package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;
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
    public ObservableList<CardInfo> getCardInfoByName(String cardName) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        ObservableList<CardInfo> cardInfos = FXCollections.observableArrayList();
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_name_query"));
            stmt.setString(1, cardName);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {                
                CardInfo cardInfo = new CardInfo();
                cardInfo.setPasscodeCol(rs.getInt("passcode"));
                cardInfo.setNameCol(rs.getString("name"));
                cardInfo.setCardTypeCol(CardType.getCardType(rs.getInt("card_type")));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(CardVariant.getCardVariant(rs.getInt("variant")));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setSetCodesCol(rs.getString("set_codes"));
                cardInfo.setImageLinkCol(rs.getString("image"));
                cardInfo.setSmallImageLinkCol(rs.getString("image_small"));
                cardInfo.setIsFakeCol(rs.getBoolean("is_fake"));
                cardInfo.setId(rs.getInt("ID"));
                cardInfos.add(cardInfo);
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
        return cardInfos;
    }
    
    // SELECT
    public CardInfo getCardInfoById(Integer infoId) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_id_query"));
            stmt.setInt(1, infoId);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {                
                CardInfo cardInfo = new CardInfo();
                cardInfo.setPasscodeCol(rs.getInt("passcode"));
                cardInfo.setNameCol(rs.getString("name"));
                cardInfo.setCardTypeCol(CardType.getCardType(rs.getInt("card_type")));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(CardVariant.getCardVariant(rs.getInt("variant")));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setSetCodesCol(rs.getString("set_codes"));
                cardInfo.setImageLinkCol(rs.getString("image"));
                cardInfo.setSmallImageLinkCol(rs.getString("image_small"));
                cardInfo.setIsFakeCol(rs.getBoolean("is_fake"));
                cardInfo.setId(rs.getInt("ID"));
                return cardInfo;
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
        return null;
    }
    
    // SELECT
    public CardInfo getCardInfoByPasscode(Integer passcode) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_passcode_query"));
            stmt.setInt(1, passcode);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {                
                CardInfo cardInfo = new CardInfo();
                cardInfo.setPasscodeCol(rs.getInt("passcode"));
                cardInfo.setNameCol(rs.getString("name"));
                cardInfo.setCardTypeCol(CardType.getCardType(rs.getInt("card_type")));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(CardVariant.getCardVariant(rs.getInt("variant")));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setSetCodesCol(rs.getString("set_codes"));
                cardInfo.setImageLinkCol(rs.getString("image"));
                cardInfo.setSmallImageLinkCol(rs.getString("image_small"));
                cardInfo.setIsFakeCol(rs.getBoolean("is_fake"));
                cardInfo.setId(rs.getInt("ID"));
                return cardInfo;
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
        return null;
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
                cardInfo.setCardTypeCol(CardType.getCardType(rs.getInt("card_type")));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(CardVariant.getCardVariant(rs.getInt("variant")));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setSetCodesCol(rs.getString("set_codes"));
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
    
    // SELECT
    public int getNumCardInfos(String name) {
        if (name.isBlank() || name.isEmpty() || name == null) {
            throw new IllegalArgumentException("Card name cannot be blank in query.");
        }
        int numberOfHits = 0;
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_count_card_info_by_name_query"));
            stmt.setObject(1, Objects.requireNonNull(name, "Card name must have a value."));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {                
                numberOfHits = rs.getInt("num_rows");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            numberOfHits = -1;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                return -1;
            }
        }
        return numberOfHits;
    }
    
    // SELECT
    public int getNumCardInfos(Integer passcode) {
        if (passcode > 99999999 || passcode < 0 || passcode == null) {
            throw new IllegalArgumentException("Received invalid passcode value.");
        }
        int numberOfHits = 0;
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_count_card_info_by_passcode_query"));
            stmt.setObject(1, Objects.requireNonNull(passcode, "Passcode must have a value."));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                numberOfHits = rs.getInt("num_rows");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            numberOfHits = -1;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                return -1;
            }
        }
        return numberOfHits;
    }
    
    // SELECT
    public void setCardInfosToCards(ObservableList<Card> cards) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_id_query"));
            for (Card card : cards) {
                stmt.setInt(1, card.getCardInfoId());
                stmt.executeQuery();
                ResultSet rs = stmt.getResultSet();
                rs.next();                
                CardInfo cardInfo = new CardInfo();
                cardInfo.setPasscodeCol(rs.getInt("passcode"));
                cardInfo.setNameCol(rs.getString("name"));
                cardInfo.setCardTypeCol(CardType.getCardType(rs.getInt("card_type")));
                cardInfo.setDescriptionCol(rs.getString("description"));
                cardInfo.setAttackCol(rs.getInt("attack"));
                cardInfo.setDefenseCol(rs.getInt("defense"));
                cardInfo.setLevelCol(rs.getInt("level"));
                cardInfo.setVariantCol(CardVariant.getCardVariant(rs.getInt("variant")));
                cardInfo.setAttributeCol(rs.getString("attribute"));
                cardInfo.setScaleCol(rs.getInt("scale"));
                cardInfo.setLinkValueCol(rs.getInt("link_value"));
                cardInfo.setLinkMarkersCol(rs.getString("link_markers"));
                cardInfo.setSetCodesCol(rs.getString("set_codes"));
                cardInfo.setImageLinkCol(rs.getString("image"));
                cardInfo.setSmallImageLinkCol(rs.getString("image_small"));
                cardInfo.setIsFakeCol(rs.getBoolean("is_fake"));
                cardInfo.setId(rs.getInt("ID"));
                card.setCardInfo(cardInfo);
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
    }
    
    // INSERT
    @Override
    public void save(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            boolean hasInvalidPasscode = false;
            hasInvalidPasscode = c.getPasscode() < 0;
            hasInvalidPasscode = hasInvalidPasscode || (c.isFake() && c.getPasscode() > 999999999);
            hasInvalidPasscode = hasInvalidPasscode || (!c.isFake() && c.getPasscode() > 99999999);
            if (hasInvalidPasscode) {
                throw new IllegalArgumentException("Passcode out of range");
            }
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_info_table_statement"), Statement.RETURN_GENERATED_KEYS);           
            stmt.setObject(1, Objects.requireNonNull(c.getPasscodeCol().getValue(), "Passcode must have a value."));
            stmt.setObject(2, Objects.requireNonNull(c.getNameCol().getValue(), "Card name must have a value."));
            stmt.setObject(3, c.getDescriptionCol().getValue());
            stmt.setObject(4, c.getCardTypeCol().getValue().getIndex());
            stmt.setObject(5, c.getVariantCol().getValue().getIndex());
            stmt.setObject(6, c.getAttributeCol().getValue());
            stmt.setObject(7, c.getAttackCol().getValue());
            stmt.setObject(8, c.getDefenseCol().getValue());
            stmt.setObject(9, c.getLevelCol().getValue());
            stmt.setObject(10, c.getScaleCol().getValue());
            stmt.setObject(11, c.getLinkValueCol().getValue());
            stmt.setObject(12, c.getLinkMarkersCol().getValue());
            stmt.setObject(13, c.getSetCodesCol().getValue());
            stmt.setObject(14, c.getImageLinkCol().getValue());
            stmt.setObject(15, c.getSmallImageLinkCol().getValue());
            stmt.setObject(16, c.getIsFakeCol().getValue());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId((int) rs.getLong(1));
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
    public void updateFakeCardInfoPasscode(CardInfo c, Integer passcode) throws SQLException {
        if (!c.isFake()) {
            throw new IllegalArgumentException("Can only update the passcode of fake card info");
        }
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (c.getPasscodeCol().getValue() < 0) {
                throw new IllegalArgumentException("Card passcode out of range.");
            }
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("update_card_info_passcode"));           
            stmt.setInt(1, passcode);
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
    
    // UPDATE
    public void updateCardInfoSetCodes(CardInfo c, String setCodes) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("update_card_info_set_codes"));           
            stmt.setString(1, setCodes);
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
    public void delete(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            if (c.getPasscodeCol().getValue() < 0 || c.getPasscodeCol().getValue() > 999999999) {
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
