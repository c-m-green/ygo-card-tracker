package com.cgreen.ygocardtracker.menu;

import java.sql.SQLException;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.Group;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.GroupDao;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GroupMenuController {
    private GroupDao grpDao;
    private CardDao cardDao;
    private Stage stage;
    private Card currentCard;
    @FXML
    private TextField nameField;
    @FXML
    private ListView<Group> groupList;
    public void init(GroupDao grpDao, CardDao cardDao, ObservableList<Group> allGroups, Card currentCard) {
        this.grpDao = grpDao;
        this.cardDao = cardDao;
        this.currentCard = currentCard;
        groupList.setItems(allGroups);
    }
    // TODO: Can we combine this with init()?
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void handleAddButtonAction(ActionEvent event) {
        if (nameField.getText().isBlank()) {
            AlertHelper.raiseAlert("Please enter a new group name first.");
        } else {
            Group grp = new Group();
            grp.setName(nameField.getText());
            try {
                grpDao.save(grp);
            } catch (SQLException e) {
                AlertHelper.raiseAlert("There was a problem creating a new group.");
            }
        }
        event.consume();
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        Group delGrp = groupList.getSelectionModel().getSelectedItem();
        if (delGrp != null && delGrp.getId() > 1) {
            try {
                grpDao.delete(delGrp);
            } catch (SQLException e) {
                AlertHelper.raiseAlert("Error deleting card group.");
            }
        }
    }
    
    public void handleSelectButtonAction(ActionEvent event) {
        Group selGroup = groupList.getSelectionModel().getSelectedItem();
        if (selGroup != null) {
            if (selGroup.getId() == currentCard.getGroupId()) {
                AlertHelper.raiseAlert("Card already belongs to the selected group.");
            } else {
                try {
                    cardDao.updateGroupId(currentCard, selGroup.getId());
                    stage.hide();
                } catch (SQLException e) {
                    AlertHelper.raiseAlert("Error moving card.");
                }
            }
        }
    }
    
    public void handleCancelButtonAction(ActionEvent event) {
        event.consume();
        stage.hide();
    }
}
