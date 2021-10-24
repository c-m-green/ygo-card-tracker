package com.cgreen.ygocardtracker.card.data;

import com.cgreen.ygocardtracker.card.model.CardModel;
import com.cgreen.ygocardtracker.card.model.CardModelFactory;

import javafx.beans.property.SimpleObjectProperty;

public class CardInfo {
    private final SimpleObjectProperty<String> nameCol, descriptionCol, linkMarkersCol, imageLinkCol, smallImageLinkCol, setCodesCol;
    private final SimpleObjectProperty<Integer> passcodeCol, attackCol, defenseCol, levelCol, scaleCol, linkValueCol;
    private final SimpleObjectProperty<Boolean> isFakeCol;
    private final SimpleObjectProperty<CardType> cardTypeCol;
    private final SimpleObjectProperty<CardVariant> variantCol;
    private final SimpleObjectProperty<Attribute> attributeCol;
    private int id, setCodeId;
    private CardModel cardModel;
    
    public CardInfo() { 
        nameCol = new SimpleObjectProperty<String>();
        descriptionCol = new SimpleObjectProperty<String>();
        linkMarkersCol = new SimpleObjectProperty<String>();
        imageLinkCol = new SimpleObjectProperty<String>();
        smallImageLinkCol = new SimpleObjectProperty<String>();
        setCodesCol = new SimpleObjectProperty<String>();
        
        passcodeCol = new SimpleObjectProperty<Integer>();
        attackCol = new SimpleObjectProperty<Integer>();
        defenseCol = new SimpleObjectProperty<Integer>();
        levelCol = new SimpleObjectProperty<Integer>();
        scaleCol = new SimpleObjectProperty<Integer>();
        linkValueCol = new SimpleObjectProperty<Integer>();
        
        cardTypeCol = new SimpleObjectProperty<CardType>();
        variantCol = new SimpleObjectProperty<CardVariant>();
        attributeCol = new SimpleObjectProperty<Attribute>();

        isFakeCol = new SimpleObjectProperty<Boolean>();
        cardModel = null;
    }
    
    public boolean initCardModel() {
        if (this.getCardType() == null) {
            cardModel = CardModelFactory.getCardModel(CardType.UNKNOWN);
            return false;
        } else {
            cardModel = CardModelFactory.getCardModel(this.getCardType());
            return true;
        }
    }
    
    public CardModel getCardModel() {
        return cardModel;
    }

    public SimpleObjectProperty<String> getNameCol() {
        return nameCol;
    }

    public void setNameCol(String name) {
        nameCol.set(name);
    }

    public SimpleObjectProperty<CardType> getCardTypeCol() {
        return cardTypeCol;
    }

    public void setCardTypeCol(CardType cardType) {
        cardTypeCol.set(cardType);;
    }

    public SimpleObjectProperty<String> getDescriptionCol() {
        return descriptionCol;
    }

    public void setDescriptionCol(String description) {
        descriptionCol.set(description);
    }

    public SimpleObjectProperty<CardVariant> getVariantCol() {
        return variantCol;
    }

    public void setVariantCol(CardVariant variant) {
        variantCol.set(variant);
    }

    public SimpleObjectProperty<Attribute> getAttributeCol() {
        return attributeCol;
    }

    public void setAttributeCol(Attribute attribute) {
        attributeCol.set(attribute);
    }

    public SimpleObjectProperty<String> getLinkMarkersCol() {
        return linkMarkersCol;
    }

    public void setLinkMarkersCol(String linkMarkers) {
        linkMarkersCol.set(linkMarkers);
    }

    public SimpleObjectProperty<String> getImageLinkCol() {
        return imageLinkCol;
    }

    public void setImageLinkCol(String imageLink) {
        imageLinkCol.set(imageLink);
    }
    
    public SimpleObjectProperty<String> getSmallImageLinkCol() {
        return smallImageLinkCol;
    }
    
    public void setSmallImageLinkCol(String smallImageLink) {
        smallImageLinkCol.set(smallImageLink);
    }

    public SimpleObjectProperty<Integer> getPasscodeCol() {
        return passcodeCol;
    }

    public void setPasscodeCol(Integer passcode) {
        passcodeCol.set(passcode);
    }

    public SimpleObjectProperty<Integer> getAttackCol() {
        return attackCol;
    }

    public void setAttackCol(Integer atk) {
        attackCol.set(atk);
    }

    public SimpleObjectProperty<Integer> getDefenseCol() {
        return defenseCol;
    }

    public void setDefenseCol(Integer def) {
        defenseCol.set(def);
    }

    public SimpleObjectProperty<Integer> getLevelCol() {
        return levelCol;
    }

    public void setLevelCol(Integer level) {
        levelCol.set(level);
    }

    public SimpleObjectProperty<Integer> getScaleCol() {
        return scaleCol;
    }

    public void setScaleCol(Integer scale) {
        scaleCol.set(scale);
    }

    public SimpleObjectProperty<Integer> getLinkValueCol() {
        return linkValueCol;
    }

    public void setLinkValueCol(Integer linkValue) {
        linkValueCol.set(linkValue);
    }

    public SimpleObjectProperty<Boolean> getIsFakeCol() {
        return isFakeCol;
    }

    public void setIsFakeCol(boolean isFake) {
        isFakeCol.set(isFake);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public SimpleObjectProperty<String> getSetCodesCol() {
        return setCodesCol;
    }
    
    public void setSetCodesCol(String setCodes) {
        setCodesCol.set(setCodes);
    }
    
    public String getName() {
        return nameCol.getValue();
    }

    public CardType getCardType() {
        return cardTypeCol.getValue();
    }

    public String getDescription() {
        return descriptionCol.getValue();
    }

    public CardVariant getVariant() {
        return variantCol.getValue();
    }

    public Attribute getAttribute() {
        return attributeCol.getValue();
    }

    public String getLinkMarkers() {
        return linkMarkersCol.getValue();
    }

    public String getImageLink() {
        return imageLinkCol.getValue();
    }

    public String getSmallImageLink() {
        return smallImageLinkCol.getValue();
    }
    
    public Integer getPasscode() {
        return passcodeCol.getValue();
    }

    public Integer getAttack() {
        return attackCol.getValue();
    }

    public Integer getDefense() {
        return defenseCol.getValue();
    }

    public Integer getLevel() {
        return levelCol.getValue();
    }

    public Integer getScale() {
        return scaleCol.getValue();
    }

    public Integer getLinkValue() {
        return linkValueCol.getValue();
    }

    public Boolean isFake() {
        return isFakeCol.getValue();
    }

    public String getSetCodes() {
        return setCodesCol.getValue();
    }

    public int getSetCodeId() {
        return setCodeId;
    }

    public void setSetCodeId(int setCodeId) {
        this.setCodeId = setCodeId;
    }
    
    // TODO: Override toString()
}
