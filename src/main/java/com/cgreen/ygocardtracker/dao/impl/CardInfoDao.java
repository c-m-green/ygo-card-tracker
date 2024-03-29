package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.data.Attribute;
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
    
    private final ObservableList<CardInfo> collectionItems;
    
    public CardInfoDao() {
        collectionItems = FXCollections.observableArrayList();
    }
    // SELECT
    public ObservableList<CardInfo> getCardInfoByName(String cardName) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        ObservableList<CardInfo> cardInfos = FXCollections.observableArrayList();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_name_query"));
                ){
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
                cardInfo.setAttributeCol(Attribute.getAttributeByName(rs.getString("attribute")));
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
        }
        return cardInfos;
    }
    
    // SELECT
    public CardInfo getCardInfoById(Integer infoId) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_id_query"));
                ){
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
                cardInfo.setAttributeCol(Attribute.getAttributeByName(rs.getString("attribute")));
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
        }
        return null;
    }
    
    // SELECT
    public CardInfo getCardInfoByPasscode(Integer passcode) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_passcode_query"));
                ){
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
                cardInfo.setAttributeCol(Attribute.getAttributeByName(rs.getString("attribute")));
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
        }
        return null;
    }
    
    // SELECT
    @Override
    public ObservableList<CardInfo> getAll() throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_info_table_query"));
                ){
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
                cardInfo.setAttributeCol(Attribute.getAttributeByName(rs.getString("attribute")));
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
        }
        return collectionItems;
    }
    
    // SELECT
    public int getNumCardInfos(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be blank in query.");
        }
        int numberOfHits = 0;
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_count_card_info_by_name_query"));
                ){
            stmt.setObject(1, Objects.requireNonNull(name, "Card name must have a value."));
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {                
                numberOfHits = rs.getInt("num_rows");
            }
        } catch (SQLException sqle) {
            // TODO: Log this instead
            sqle.printStackTrace();
            numberOfHits = -1;
        }
        return numberOfHits;
    }
    
    // SELECT
    public int getNumCardInfos(Integer passcode) {
        // TODO: Reorder this check
        if (passcode == null || passcode > 99999999 || passcode < 0) {
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
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_info_by_id_query"));
                ){
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
                cardInfo.setAttributeCol(Attribute.getAttributeByName(rs.getString("attribute")));
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
        }
    }
    
    // INSERT
    @Override
    public void save(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("insert_into_card_info_table_statement"), Statement.RETURN_GENERATED_KEYS);
                ){
            boolean hasInvalidPasscode = false;
            hasInvalidPasscode = c.getPasscode() < 0;
            hasInvalidPasscode = hasInvalidPasscode || (c.isFake() && c.getPasscode() > 999999999);
            hasInvalidPasscode = hasInvalidPasscode || (!c.isFake() && c.getPasscode() > 99999999);
            if (hasInvalidPasscode) {
                throw new IllegalArgumentException("Passcode out of range");
            }
            stmt.setObject(1, Objects.requireNonNull(c.getPasscodeCol().getValue(), "Passcode must have a value."));
            stmt.setObject(2, Objects.requireNonNull(c.getNameCol().getValue(), "Card name must have a value."));
            stmt.setObject(3, c.getDescriptionCol().getValue());
            stmt.setObject(4, c.getCardTypeCol().getValue().getIndex());
            stmt.setObject(5, c.getVariantCol().getValue().getIndex());
            stmt.setObject(6, c.getAttributeCol().getValue().toString());
            stmt.setObject(7, c.getAttackCol().getValue());
            stmt.setObject(8, c.getDefenseCol().getValue());
            stmt.setObject(9, c.getLevelCol().getValue());
            stmt.setObject(10, c.getScaleCol().getValue());
            stmt.setObject(11, c.getLinkValueCol().getValue());
            stmt.setObject(12, c.getLinkMarkersCol().getValue());
            stmt.setObject(13, c.getSetCodeId());
            stmt.setObject(14, c.getImageLinkCol().getValue());
            stmt.setObject(15, c.getSmallImageLinkCol().getValue());
            stmt.setObject(16, c.getIsFakeCol().getValue());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                c.setId((int) rs.getLong(1));
            }
            collectionItems.add(c);
        }
    }

    // UPDATE
    public void updateFakeCardInfoPasscode(CardInfo c, Integer passcode) throws SQLException {
        if (!c.isFake()) {
            throw new IllegalArgumentException("Can only update the passcode of fake card info");
        }
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("update_card_info_passcode"));
                ){
            if (c.getPasscodeCol().getValue() < 0) {
                throw new IllegalArgumentException("Card passcode out of range.");
            }
            stmt.setInt(1, passcode);
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            c.setPasscodeCol(passcode);
        }
    }

    // UPDATE
    public void updateCardInfoImage(CardInfo c, String pathToImage) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("update_card_info_image"));
        ){
            stmt.setString(1, pathToImage);
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            c.setImageLinkCol(pathToImage);
        }
    }

    // UPDATE
    public void updateCardInfoImageSmall(CardInfo c, String pathToImage) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("update_card_info_small_image"));
        ){
            stmt.setString(1, pathToImage);
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            c.setSmallImageLinkCol(pathToImage);
        }
    }
    
    // DELETE
    @Override
    public void delete(CardInfo c) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
                Connection conn = dbm.connectToDatabase();
                PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("delete_from_card_info_table_statement"));
        ){
            if (c.getPasscodeCol().getValue() < 0 || c.getPasscodeCol().getValue() > 999999999) {
                throw new IllegalArgumentException("Card passcode out of range.");
            }
            stmt.setInt(1, c.getId());
            stmt.executeUpdate();
        }
        collectionItems.remove(c);
    }

}
