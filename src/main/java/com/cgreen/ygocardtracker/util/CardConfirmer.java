package com.cgreen.ygocardtracker.util;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.cgreen.ygocardtracker.card.Group;
import com.cgreen.ygocardtracker.card.data.CardInfo;

import com.cgreen.ygocardtracker.dao.impl.GroupDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardConfirmer {
    private final List<CardInfo> cardInfos;
    private GroupDao groupDao;
    public CardConfirmer(List<CardInfo> cardInfos) {
        this.cardInfos = cardInfos;
        this.groupDao = new GroupDao();
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

    public ObservableList<Group> getAllGroups() {
        try {
            return groupDao.getAll();
        } catch (SQLException e) {
            AlertHelper.raiseAlert(e.getMessage());
            // TODO Do something better than this
            return null;
        }
    }
}
