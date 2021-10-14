package com.cgreen.ygocardtracker.menu;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.Group;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.GroupDao;
import com.cgreen.ygocardtracker.db.exports.CardImporter;
import com.cgreen.ygocardtracker.util.AlertHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllCardsController {

    private Stage stage;
    private CardDao cardDao;
    private CardInfoDao cardInfoDao;
    private GroupDao grpDao;
    private ObservableList<Card> cardList;
    private ObservableList<Group> allGroups;
    @FXML
    private ChoiceBox<Group> groupChoicebox;
    @FXML
    private ListView<Card> listView;
    @FXML
    private Button addCardButton, deleteButton, backButton, groupButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Text cardNameText, setCodeText, deckText;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        try {
            cardDao = new CardDao();
            cardInfoDao = new CardInfoDao();
            grpDao = new GroupDao();
            cardList = cardDao.getAll();
            cardInfoDao.setCardInfosToCards(cardList);
            Comparator<Card> comparator = new Comparator<Card>() {
                @Override
                public int compare(Card c1, Card c2) {
                    return c1.compareTo(c2);
                }
            };
            FXCollections.sort(cardList, comparator);
            listView.setItems(cardList);
            allGroups = grpDao.getAll();
            groupChoicebox.setItems(allGroups);
            wipeCurrentCardView();
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Card>() {
                @Override
                public void changed(ObservableValue<? extends Card> observable, Card oldValue, Card newValue) {
                    updateCurrentCardView(newValue);
                }
            });
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handleImportButtonAction(ActionEvent event) throws IOException {
        Alert info = new Alert(AlertType.INFORMATION, "Select a YGO Card Tracker JSON or YGOProDeck CSV file.");
        info.showAndWait();
        FileChooser fc = new FileChooser();
        fc.setTitle("Select card collection file");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Card Collection File Formats", Arrays.asList("*.json", "*.csv"))
        );
        File file = fc.showOpenDialog(stage);
        if (file == null) {
            AlertHelper.raiseAlert("No file was provided.");
        } else {
            String ext = "";
            int index = file.toString().lastIndexOf(".");
            if (index > 0) {
                ext = file.toString().substring(index + 1).toLowerCase();
                switch(ext) {
                case "json":
                    CardImporter.importJson(file);
                    break;
                case "csv":
                    FXMLLoader loader = new FXMLLoader(AddCardsMenuController.class.getClassLoader().getResource("csv_import_menu.fxml"));
                    Parent parent = loader.load();

                    Stage csvImportStage = new Stage();
                    csvImportStage.setScene(new Scene(parent));
                    CsvImportController cic = loader.getController();
                    cic.setStage(csvImportStage);
                    cic.init(file);
                    csvImportStage.initModality(Modality.APPLICATION_MODAL);
                    csvImportStage.showAndWait();
                    break;
                default:
                    AlertHelper.raiseAlert("Invalid file format.");
                }
            } else {
                AlertHelper.raiseAlert("There was a problem verifying the input file.");
            }
        }
    }
    
    public void handleExportButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(CardExporterController.class.getClassLoader().getResource("card_export_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage cardExportStage = new Stage();
        cardExportStage.setScene(new Scene(parent));
        CardExporterController cec = loader.getController();
        cec.setStage(cardExportStage);
        cardExportStage.initModality(Modality.APPLICATION_MODAL);
        cardExportStage.showAndWait();
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        Card card = listView.getSelectionModel().getSelectedItem();
        try {
            cardDao.delete(card);
            wipeCurrentCardView();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        event.consume();
    }
    
    public void handleAddCardButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AddCardsMenuController.class.getClassLoader().getResource("add_cards_menu.fxml"));
        Parent parent = loader.load();        
        
        Stage addCardsStage = new Stage();
        addCardsStage.setScene(new Scene(parent));
        AddCardsMenuController acc = loader.getController();
        acc.setStage(addCardsStage);
        addCardsStage.initModality(Modality.APPLICATION_MODAL);
        addCardsStage.showAndWait();
        init();
        event.consume();
    }
    
    public void handleBackButtonAction(ActionEvent event) {
        stage.close();
    }
    
    public void handleGroupChoiceAction(ActionEvent event) {
        Group currentGroup = groupChoicebox.getSelectionModel().getSelectedItem();
        if (currentGroup != null) {
            try {
                ObservableList<Card> cards = cardDao.getCardsByGroupId(currentGroup.getId());
                cardInfoDao.setCardInfosToCards(cards);
                listView.setItems(cards);
                wipeCurrentCardView();
            } catch (SQLException e) {
                AlertHelper.raiseAlert("Error loading cards belonging to the group \"" + currentGroup + ".\"");
            }
        }
    }
    
    public void handleGroupButtonAction(ActionEvent event) throws IOException {
        Card c = listView.getSelectionModel().getSelectedItem();
        if (c != null) {
            FXMLLoader loader = new FXMLLoader(GroupMenuController.class.getClassLoader().getResource("group_select_menu.fxml"));
            Parent parent = loader.load();        
            
            Stage groupStage = new Stage();
            groupStage.setScene(new Scene(parent));
            GroupMenuController gmc = loader.getController();
            gmc.setStage(groupStage);
            gmc.init(grpDao, cardDao, allGroups, c);
            groupStage.initModality(Modality.APPLICATION_MODAL);
            groupStage.showAndWait();
            init();
        }
        event.consume();
    }
    
    public void handleListClickEvent() {
        Card card = listView.getSelectionModel().getSelectedItem();
        updateCurrentCardView(card);
    }
    
    private void updateCurrentCardView(Card newValue) {
        if (newValue == null || newValue.getCardInfo() == null) {
            // TODO: Display default card image
        } else {
            imageView.setImage(new Image(new File(newValue.getCardInfo().getImageLink()).toURI().toString()));
            cardNameText.setText(newValue.getCardInfo().getName());
            setCodeText.setText(newValue.getSetCode());
            if (newValue.getDeckId() > 1) {
                deckText.setText("Assigned to deck");
            } else {
                deckText.setText("Unassigned");
            }
        }
    }
    
    private void wipeCurrentCardView() {
        cardNameText.setText("");
        setCodeText.setText("");
        deckText.setText("");
        imageView.setImage(null);
        
    }
    
    
}