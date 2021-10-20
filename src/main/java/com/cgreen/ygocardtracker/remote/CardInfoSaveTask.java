package com.cgreen.ygocardtracker.remote;

import com.cgreen.ygocardtracker.card.data.Attribute;
import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;
import com.cgreen.ygocardtracker.card.model.CardModel;
import com.cgreen.ygocardtracker.card.model.CardModelFactory;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.SetCodeDao;
import com.cgreen.ygocardtracker.util.CardImageSaver;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class CardInfoSaveTask extends Task<Void> {
    private final JSONArray allCardInfo;
    public CardInfoSaveTask(JSONArray allCardInfo) {
        this.allCardInfo = allCardInfo;
    }
    @Override
    protected Void call() throws SQLException {
        CardInfoDao dao = new CardInfoDao();
        SetCodeDao setCodeDao = new SetCodeDao();
        for (int cardIndex = 0; cardIndex < allCardInfo.length(); cardIndex++) {
            updateProgress(cardIndex - 1, allCardInfo.length());
            JSONObject currentCardInfo = allCardInfo.getJSONObject(cardIndex);
            Integer cardTypeIndex = CardType.getIndexOf(currentCardInfo.getString("type"));
            CardType cardType = CardType.getCardType(cardTypeIndex);
            if (cardType == CardType.SKILL) {
                // TODO: Log this
                System.out.println("Encountered a Skill card -- skipping this one.");
                return null;
            }
            CardModel cardModel = CardModelFactory.getCardModel(cardType);
            String nameColVal = currentCardInfo.getString("name");
            String descColVal = currentCardInfo.getString("desc");
            String cardVarStr = currentCardInfo.getString("race");
            // Manually setting card variant in some cases because there are two that
            // say "Normal"
            CardVariant cardVariant;
            if (cardVarStr.equalsIgnoreCase("Normal")) {
                cardVariant = switch (cardType) {
                    case SPELL -> CardVariant.SPELL_NORMAL;
                    case TRAP -> CardVariant.TRAP_NORMAL;
                    default -> CardVariant.UNKNOWN;
                };
            } else {
                cardVariant = CardVariant.getCardVariant(CardVariant.getIndexOf(cardVarStr));
            }
            Integer linkValueColVal, atkColVal, defColVal, levelColVal, scaleColVal;
            String linkMarkersColVal, setCodesColVal;
            Attribute attributeColVal;
            if (cardModel.isLink()) {
                JSONArray linkMarkersArr = currentCardInfo.getJSONArray("linkmarkers");
                linkMarkersColVal = "";
                for (int linkIndex = 0; linkIndex < linkMarkersArr.length() - 1; linkIndex++) {
                    linkMarkersColVal += linkMarkersArr.getString(linkIndex) + ",";
                }
                linkMarkersColVal += linkMarkersArr.getString(linkMarkersArr.length() - 1);
                linkValueColVal = currentCardInfo.getInt("linkval");
            } else {
                linkValueColVal = null;
                linkMarkersColVal = null;
            }
            atkColVal = (cardModel.isMonster()) ? currentCardInfo.getInt("atk") : null;
            defColVal = (cardModel.isMonster() && !cardModel.isLink()) ? currentCardInfo.getInt("def") : null;
            levelColVal = (cardModel.hasLevel()) ? currentCardInfo.getInt("level") : null;
            scaleColVal = (cardModel.hasScale()) ? currentCardInfo.getInt("scale") : null;
            attributeColVal = (cardModel.hasAttribute()) ? Attribute.getAttributeByName(currentCardInfo.getString("attribute")) : Attribute.NONE;
            JSONArray cardSetsArr = currentCardInfo.getJSONArray("card_sets");
            setCodesColVal = "";
            for (int i = 0; i < cardSetsArr.length() - 1; i++) {
                JSONObject cardSetObj = cardSetsArr.getJSONObject(i);
                setCodesColVal += cardSetObj.getString("set_code") + ",";
            }
            JSONObject cardSetObjLast = cardSetsArr.getJSONObject(cardSetsArr.length() - 1);
            setCodesColVal += cardSetObjLast.getString("set_code");
            int setCodeId = setCodeDao.save(setCodesColVal);
            JSONArray cardImagesArr = currentCardInfo.getJSONArray("card_images");
            for (int i = 0; i < cardImagesArr.length(); i++) {
                System.out.println("Saving card info for " + nameColVal + "(" + (i + 1) + ")");
                JSONObject cardImageObj = cardImagesArr.getJSONObject(i);
                Integer passcodeColVal = cardImageObj.getInt("id");
                int numCardsInDBWithThisPasscode = dao.getNumCardInfos(passcodeColVal);
                if (numCardsInDBWithThisPasscode > 0) {
                    // TODO: Log this
                    System.out.println("Already have card info for " + passcodeColVal + " -- skipping it");
                    continue;
                } else if (numCardsInDBWithThisPasscode == -1) {
                    // TODO: Log this
                    // TODO: Am I sure I want to throw this exception here?
                    throw new SQLException("ERROR: Problem checking whether " + passcodeColVal + " is in the DB.");
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
                dbCardInfo.setSetCodeId(setCodeId);

                String picUrl = cardImageObj.getString("image_url");
                String picUrlSmall = cardImageObj.getString("image_url_small");

                Image image;
                try {
                    image = CardImageSaver.getCardImage(picUrl);
                    dbCardInfo.setImageLinkCol(CardImageSaver.saveCardImageFile(image, passcodeColVal, "jpg", false));
                } catch (IOException e) {
                    // TODO Log this
                    System.out.println("ERROR: Problem saving image for " + passcodeColVal);
                    dbCardInfo.setImageLinkCol(null);
                }
                Image imageSmall;
                try {
                    imageSmall = CardImageSaver.getCardImage(picUrlSmall);
                    dbCardInfo.setSmallImageLinkCol(CardImageSaver.saveCardImageFileSmall(imageSmall, passcodeColVal, "jpg", false));
                } catch (IOException e) {
                    // TODO Log this
                    System.out.println("ERROR: Problem saving small image for " + passcodeColVal);
                    dbCardInfo.setSmallImageLinkCol(null);
                }
                dao.save(dbCardInfo);
            }
        }
        return null;
    }
}
