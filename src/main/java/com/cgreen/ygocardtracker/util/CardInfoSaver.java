package com.cgreen.ygocardtracker.util;

import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    public List<CardInfo> saveCardInfoFromJson(JSONArray cardData) {
        List<CardInfo> out = new ArrayList<CardInfo>();
        CardInfoDao dao = new CardInfoDao();
        // We're not doing fuzzy searches, so we should only have one card.
        JSONObject allCardInfo = cardData.getJSONObject(0);
        CardModelFactory cardModelFactory = new CardModelFactory();
        Integer cardTypeIndex = CardType.getIndexOf(allCardInfo.getString("type"));
        CardType cardType = CardType.getCardType(cardTypeIndex);
        CardModel cardModel = cardModelFactory.getCardModel(cardType);
        String nameColVal = allCardInfo.getString("name");
        String descColVal = allCardInfo.getString("desc");
        Integer variantIndex = CardVariant.getIndexOf(allCardInfo.getString("race"));
        CardVariant cardVariant = CardVariant.getCardVariant(variantIndex);
        Integer linkValueColVal, atkColVal, defColVal, levelColVal, scaleColVal;
        String linkMarkersColVal, attributeColVal, setCodesColVal;
        if (cardModel.isLink()) {
            JSONArray linkMarkersArr = allCardInfo.getJSONArray("linkmarkers");
            linkMarkersColVal = "";
            for (int linkIndex = 0; linkIndex < linkMarkersArr.length() - 1; linkIndex++) {
                linkMarkersColVal += linkMarkersArr.getString(linkIndex) + ",";
            }
            linkMarkersColVal += linkMarkersArr.getString(linkMarkersArr.length() - 1);
            linkValueColVal = allCardInfo.getInt("linkval");
        } else {
            linkValueColVal = null;
            linkMarkersColVal = null;
        }
        atkColVal = (cardModel.isMonster()) ? allCardInfo.getInt("atk") : null;
        defColVal = (cardModel.isMonster() && !cardModel.isLink()) ? allCardInfo.getInt("def") : null;
        levelColVal = (cardModel.hasLevel()) ? allCardInfo.getInt("level") : null;
        scaleColVal = (cardModel.hasScale()) ? allCardInfo.getInt("scale") : null;
        attributeColVal = (cardModel.hasAttribute()) ? allCardInfo.getString("attribute") : null;
        JSONArray cardSetsArr = allCardInfo.getJSONArray("card_sets");
        setCodesColVal = "";
        for (int i = 0; i < cardSetsArr.length() - 1; i++) {
            JSONObject cardSetObj = cardSetsArr.getJSONObject(i);
            setCodesColVal += cardSetObj.getString("set_code") + ",";
        }
        JSONObject cardSetObjLast = cardSetsArr.getJSONObject(cardSetsArr.length() - 1);
        setCodesColVal += cardSetObjLast.getString("set_code");
        JSONArray cardImagesArr = allCardInfo.getJSONArray("card_images");
        for (int i = 0; i < cardImagesArr.length(); i++) {
            JSONObject cardImageObj = cardImagesArr.getJSONObject(i);
            Integer passcodeColVal = cardImageObj.getInt("id");
            int numCardsInDBWithThisPasscode = dao.getNumCardInfos(passcodeColVal);
            if (numCardsInDBWithThisPasscode > 0) {
                // TODO: Log this
                continue;
            } else if (numCardsInDBWithThisPasscode == -1) {
                // TODO: Log this
                continue;
            }
            CardInfo dbCardInfo = new CardInfo();
            dbCardInfo.setIsFakeCol(false);
            dbCardInfo.setPasscodeCol(passcodeColVal);
            dbCardInfo.setCardTypeCol(cardType);
            dbCardInfo.setNameCol(nameColVal);
            dbCardInfo.setDescriptionCol(descColVal);
            dbCardInfo.setVariantCol(cardVariant);
            dbCardInfo.setAttributeCol(attributeColVal);
            dbCardInfo.setAttackCol(atkColVal);
            dbCardInfo.setLinkMarkersCol(linkMarkersColVal);
            dbCardInfo.setLinkValueCol(linkValueColVal);
            dbCardInfo.setDefenseCol(defColVal);
            dbCardInfo.setLevelCol(levelColVal);
            dbCardInfo.setScaleCol(scaleColVal);
            dbCardInfo.setSetCodesCol(setCodesColVal);
            
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
                out.add(dbCardInfo);
            } catch (SQLException e) {
                // TODO Deal with this appropriately
                e.printStackTrace();
                AlertHelper.raiseAlert("Something went wrong saving this card info to the DB.");
            }
        }
        return out;
    }
}
