package com.cgreen.ygocardtracker.util;

import java.util.Arrays;
import java.util.List;

import com.cgreen.ygocardtracker.card.data.CardInfo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardConfirmer {
    private List<CardInfo> cardInfos;
    public CardConfirmer(List<CardInfo> cardInfos) {
        this.cardInfos = cardInfos;
    }
    
    public ObservableList<String> getCardSetChoicesForPasscode(String passcode) {
        ObservableList<String> cardSetChoices = FXCollections.observableArrayList();
        for (CardInfo cardInfo : cardInfos) {
            if (String.format("%08d", cardInfo.getPasscode()).equals(passcode)) {
                String[] cardSetArr = cardInfo.getSetCodes().split(",");
                cardSetChoices.addAll(Arrays.asList(cardSetArr));
                break;
            }
        }
        return cardSetChoices;
    }
    
    public String getImageLinkForPasscode(String passcode) {
        for (CardInfo cardInfo : cardInfos) {
            if (String.format("%08d", cardInfo.getPasscode()).equals(passcode)) {
                return cardInfo.getImageLink();
            }
        }
        return "";
    }
}
