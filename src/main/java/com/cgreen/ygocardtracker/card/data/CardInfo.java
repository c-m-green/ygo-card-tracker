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
        this.nameCol.set(name);
    }

    public SimpleObjectProperty<Integer> getCardTypeCol() {
        return cardTypeCol;
    }

    public void setCardTypeCol(Integer cardType) {
        this.cardTypeCol.set(cardType);;
    }

    public SimpleObjectProperty<String> getDescriptionCol() {
        return descriptionCol;
    }

    public void setDescriptionCol(String description) {
        this.descriptionCol.set(description);
    }

    public SimpleObjectProperty<Integer> getVariantCol() {
        return variantCol;
    }

    public void setVariantCol(Integer variant) {
        this.variantCol.set(variant);
    }

    public SimpleObjectProperty<String> getAttributeCol() {
        return attributeCol;
    }

    public void setAttributeCol(String attribute) {
        this.attributeCol.set(attribute);
    }

    public SimpleObjectProperty<String> getLinkMarkersCol() {
        return linkMarkersCol;
    }

    public void setLinkMarkersCol(String linkMarkers) {
        this.linkMarkersCol.set(linkMarkers);
    }

    public SimpleObjectProperty<String> getImageLinkCol() {
        return imageLinkCol;
    }

    public void setImageLinkCol(String imageLink) {
        this.imageLinkCol.set(imageLink);
    }
    
    public SimpleObjectProperty<String> getSmallImageLinkCol() {
        return smallImageLinkCol;
    }
    
    public void setSmallImageLinkCol(String smallImageLinkCol) {
        this.smallImageLinkCol.set(smallImageLinkCol);
    }

    public SimpleObjectProperty<Integer> getPasscodeCol() {
        return passcodeCol;
    }

    public void setPasscodeCol(Integer passcodeCol) {
        this.passcodeCol.set(passcodeCol);
    }

    public SimpleObjectProperty<Integer> getAttackCol() {
        return attackCol;
    }

    public void setAttackCol(Integer atk) {
        this.attackCol.set(atk);
    }

    public SimpleObjectProperty<Integer> getDefenseCol() {
        return defenseCol;
    }

    public void setDefenseCol(Integer def) {
        this.defenseCol.set(def);
    }

    public SimpleObjectProperty<Integer> getLevelCol() {
        return levelCol;
    }

    public void setLevelCol(Integer level) {
        this.levelCol.set(level);
    }

    public SimpleObjectProperty<Integer> getScaleCol() {
        return scaleCol;
    }

    public void setScaleCol(Integer scale) {
        this.scaleCol.set(scale);
    }

    public SimpleObjectProperty<Integer> getLinkValueCol() {
        return linkValueCol;
    }

    public void setLinkValueCol(Integer linkValue) {
        this.linkValueCol.set(linkValue);
    }

    public SimpleObjectProperty<Boolean> getIsFakeCol() {
        return isFakeCol;
    }

    public void setIsFakeCol(boolean isFake) {
        this.isFakeCol.set(isFake);
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
        this.setCodesCol.set(setCodes);
    }
}
