package com.cgreen.ygocardtracker.util;

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;
import com.cgreen.ygocardtracker.card.model.CardModel;
import com.cgreen.ygocardtracker.card.model.CardModelFactory;
import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;

public class CardInfoSaver {
    public CardInfoSaver() { }
    
    public void saveCardInfoFromJson(JSONArray cardData) {
        CardInfoDao dao = new CardInfoDao();
        // We're not doing fuzzy searches, so we should only have one card.
        JSONObject cardInfo = cardData.getJSONObject(0);
        CardModelFactory cardModelFactory = new CardModelFactory();
        Integer cardTypeIndex = CardType.getIndexOf(cardInfo.getString("type"));
        CardType cardType = CardType.getCardType(cardTypeIndex);
        CardModel cardModel = cardModelFactory.getCardModel(cardType);
        String nameColVal = cardInfo.getString("name");
        String descColVal = cardInfo.getString("desc");
        Integer variantColVal = CardVariant.getIndexOf(cardInfo.getString("race"));
        Integer linkValueColVal, atkColVal, defColVal, levelColVal, scaleColVal;
        String linkMarkersColVal, attributeColVal;
        if (cardModel.isLink()) {
            JSONArray linkMarkersArr = cardInfo.getJSONArray("linkmarkers");
            linkMarkersColVal = "";
            for (int linkIndex = 0; linkIndex < linkMarkersArr.length() - 1; linkIndex++) {
                linkMarkersColVal += linkMarkersArr.getString(linkIndex) + ",";
            }
            linkMarkersColVal += linkMarkersArr.getString(linkMarkersArr.length() - 1);
            linkValueColVal = cardInfo.getInt("linkval");
        } else {
            linkValueColVal = null;
            linkMarkersColVal = null;
        }
        atkColVal = (cardModel.isMonster()) ? cardInfo.getInt("atk") : null;
        defColVal = (cardModel.isMonster() && !cardModel.isLink()) ? cardInfo.getInt("def") : null;
        levelColVal = (cardModel.hasLevel()) ? cardInfo.getInt("level") : null;
        scaleColVal = (cardModel.hasScale()) ? cardInfo.getInt("scale") : null;
        attributeColVal = (cardModel.hasAttribute()) ? cardInfo.getString("attribute") : null;
        JSONArray cardImagesArr = cardInfo.getJSONArray("card_images");
        for (int i = 0; i < cardImagesArr.length(); i++) {
            CardInfo dbCardInfo = new CardInfo();
            dbCardInfo.setIsFakeCol(false);
            JSONObject cardImageObj = cardImagesArr.getJSONObject(i);
            Integer passcodeColVal = cardImageObj.getInt("id");
            dbCardInfo.setPasscodeCol(passcodeColVal);
            dbCardInfo.setCardTypeCol(cardTypeIndex);
            dbCardInfo.setNameCol(nameColVal);
            dbCardInfo.setDescriptionCol(descColVal);
            dbCardInfo.setVariantCol(variantColVal);
            dbCardInfo.setAttributeCol(attributeColVal);
            dbCardInfo.setAttackCol(atkColVal);
            dbCardInfo.setLinkMarkersCol(linkMarkersColVal);
            dbCardInfo.setLinkValueCol(linkValueColVal);
            dbCardInfo.setDefenseCol(defColVal);
            dbCardInfo.setLevelCol(levelColVal);
            dbCardInfo.setScaleCol(scaleColVal);
            
            String picUrl = cardImageObj.getString("image_url");
            String picUrlSmall = cardImageObj.getString("image_url_small");
            
            Image image;
            try {
                image = CardImageSaver.getCardImage(picUrl);
                dbCardInfo.setImageLinkCol(CardImageSaver.saveCardImageFile(image, passcodeColVal));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                AlertHelper.raiseAlert("Failed to save card image.");
                dbCardInfo.setImageLinkCol(null);
            }
            Image imageSmall;
            try {
                imageSmall = CardImageSaver.getCardImage(picUrlSmall);
                dbCardInfo.setSmallImageLinkCol(CardImageSaver.saveCardImageFileSmall(imageSmall, passcodeColVal));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                AlertHelper.raiseAlert("Failed to save small card image.");
                dbCardInfo.setSmallImageLinkCol(null);
            }
            try {
                dao.save(dbCardInfo);
            } catch (SQLException e) {
                // TODO Deal with this appropriately
                e.printStackTrace();
                AlertHelper.raiseAlert("Something went wrong saving this card info to the DB.");
            }
        }
    }
    
    public void saveCardInfo(CardInfo cardInfo) {
        
    }
}
