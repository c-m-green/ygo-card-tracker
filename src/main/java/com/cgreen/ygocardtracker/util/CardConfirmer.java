package com.cgreen.ygocardtracker.util;

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
                for (String cardSet : cardSetArr) {
                    cardSetChoices.add(cardSet);
                }
                break;
            }
        }
        return cardSetChoices;
    }
    
    public String getImageLinkForPasscode(String passcode) {
        // TODO: Set this link to the default card image.
        String defaultImageLink = "";
        for (CardInfo cardInfo : cardInfos) {
            if (String.format("%08d", cardInfo.getPasscode()).equals(passcode)) {
                String imgLink = cardInfo.getImageLink();
                if (imgLink == null) {
                    return defaultImageLink;
                } else {
                    return imgLink;
                }
            }
        }
        return defaultImageLink;
    }
}
