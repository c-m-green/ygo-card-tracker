package com.cgreen.ygocardtracker.card.data;

import javafx.beans.property.SimpleObjectProperty;

public class CardInfo {
    private SimpleObjectProperty<String> nameCol, descriptionCol, attributeCol, linkMarkersCol, imageLinkCol, smallImageLinkCol, setCodesCol;
    private SimpleObjectProperty<Integer> passcodeCol, attackCol, defenseCol, levelCol, scaleCol, linkValueCol, variantCol, cardTypeCol;
    private SimpleObjectProperty<Boolean> isFakeCol;
    private int id;
    
    public CardInfo() { 
        nameCol = new SimpleObjectProperty<String>();
        descriptionCol = new SimpleObjectProperty<String>();
        attributeCol = new SimpleObjectProperty<String>();
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
        cardTypeCol = new SimpleObjectProperty<Integer>();
        variantCol = new SimpleObjectProperty<Integer>();
        
        isFakeCol = new SimpleObjectProperty<Boolean>();
    }

    public SimpleObjectProperty<String> getNameCol() {
        return nameCol;
    }

    public void setNameCol(String name) {
        nameCol.set(name);
    }

    public SimpleObjectProperty<Integer> getCardTypeCol() {
        return cardTypeCol;
    }

    public void setCardTypeCol(Integer cardType) {
        cardTypeCol.set(cardType);;
    }

    public SimpleObjectProperty<String> getDescriptionCol() {
        return descriptionCol;
    }

    public void setDescriptionCol(String description) {
        descriptionCol.set(description);
    }

    public SimpleObjectProperty<Integer> getVariantCol() {
        return variantCol;
    }

    public void setVariantCol(Integer variant) {
        variantCol.set(variant);
    }

    public SimpleObjectProperty<String> getAttributeCol() {
        return attributeCol;
    }

    public void setAttributeCol(String attribute) {
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

    public Integer getCardType() {
        return cardTypeCol.getValue();
    }

    public String getDescription() {
        return descriptionCol.getValue();
    }

    public Integer getVariant() {
        return variantCol.getValue();
    }

    public String getAttribute() {
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

    public Boolean getIsFake() {
        return isFakeCol.getValue();
    }

    public String getSetCodes() {
        return setCodesCol.getValue();
    }
    
}
