package com.cgreen.ygocardtracker.card.data;

import javafx.beans.property.SimpleObjectProperty;

public class Card {
    private SimpleObjectProperty<Integer> cardInfoIdCol, deckIdCol;
    private SimpleObjectProperty<String> setCodeCol;
    private SimpleObjectProperty<Boolean> inSideDeckCol, isVirtualCol;
    private int id;
    
    public Card() {
        cardInfoIdCol = new SimpleObjectProperty<Integer>();
        deckIdCol = new SimpleObjectProperty<Integer>();
        
        setCodeCol = new SimpleObjectProperty<String>();
        
        inSideDeckCol = new SimpleObjectProperty<Boolean>();
        isVirtualCol = new SimpleObjectProperty<Boolean>();
    }

    public SimpleObjectProperty<Integer> getCardInfoIdCol() {
        return cardInfoIdCol;
    }

    public void setCardInfoIdCol(Integer cardInfoId) {
        cardInfoIdCol.set(cardInfoId);
    }

    public SimpleObjectProperty<Integer> getDeckIdCol() {
        return deckIdCol;
    }

    public void setDeckIdCol(Integer deckId) {
        deckIdCol.set(deckId);
    }

    public SimpleObjectProperty<String> getSetCodeCol() {
        return setCodeCol;
    }

    public void setSetCodeCol(String setCode) {
        setCodeCol.set(setCode);
    }

    public SimpleObjectProperty<Boolean> getInSideDeckCol() {
        return inSideDeckCol;
    }

    public void setInSideDeckCol(Boolean inSideDeck) {
        inSideDeckCol.set(inSideDeck);
    }

    public SimpleObjectProperty<Boolean> getIsVirtualCol() {
        return isVirtualCol;
    }

    public void setIsVirtualCol(Boolean isVirtual) {
        isVirtualCol.set(isVirtual);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getCardInfoId() {
        return cardInfoIdCol.getValue();
    }

    public Integer getDeckId() {
        return deckIdCol.getValue();
    }

    public String getSetCode() {
        return setCodeCol.getValue();
    }

    public Boolean getInSideDeck() {
        return inSideDeckCol.getValue();
    }

    public Boolean getIsVirtual() {
        return isVirtualCol.getValue();
    }
}
