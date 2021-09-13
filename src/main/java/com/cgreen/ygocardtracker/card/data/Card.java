package com.cgreen.ygocardtracker.card.data;

public class Card {
    private Integer id, cardInfoId, deckId;
    private String setCode;
    private Boolean inSideDeck, isVirtual;
    
    public Card() { }

    public void setCardInfoId(Integer cardInfoId) {
        this.cardInfoId = cardInfoId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }

    public void setInSideDeck(Boolean inSideDeck) {
        this.inSideDeck = inSideDeck;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Integer getCardInfoId() {
        return cardInfoId;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public String getSetCode() {
        return setCode;
    }

    public Boolean getInSideDeck() {
        return inSideDeck;
    }

    public Boolean getIsVirtual() {
        return isVirtual;
    }
}
