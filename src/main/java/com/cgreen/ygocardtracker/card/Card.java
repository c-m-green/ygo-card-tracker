package com.cgreen.ygocardtracker.card;

import com.cgreen.ygocardtracker.card.data.CardInfo;

public class Card {
    private Integer id, cardInfoId, deckId, groupId;
    private String setCode;
    private Boolean inSideDeck, isVirtual;
    private CardInfo cardInfo;
    
    public Card() { }
    
    public Card(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    
    public int compareTo(Card other) {
        if (other.getCardInfo() == null || this.getCardInfo() == null) {
            return 0;
        }
        if (this.getCardInfo().isFake() != other.getCardInfo().isFake()) {
            return this.getCardInfo().isFake() ? 1 : -1;
        } else {
            return this.toString().compareTo(other.toString());
        }
    }

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

    public Boolean inSideDeck() {
        return inSideDeck;
    }

    public Boolean isVirtual() {
        return isVirtual;
    }
    
    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    
    public CardInfo getCardInfo() {
        return cardInfo;
    }
    
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        if (cardInfo == null) {
            return "id:" + id;
        } else {
            return cardInfo.getName() + " (" + setCode + ")";
        }
    }
}
