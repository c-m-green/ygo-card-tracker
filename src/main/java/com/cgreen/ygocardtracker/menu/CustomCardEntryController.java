package com.cgreen.ygocardtracker.menu;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.cgreen.ygocardtracker.card.Card;
import com.cgreen.ygocardtracker.card.data.Attribute;
import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.card.data.CardType;
import com.cgreen.ygocardtracker.card.data.CardVariant;
import com.cgreen.ygocardtracker.card.model.CardModel;
import com.cgreen.ygocardtracker.card.model.CardModelFactory;
import com.cgreen.ygocardtracker.dao.impl.CardDao;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.dao.impl.SetCodeDao;
import com.cgreen.ygocardtracker.util.AlertHelper;

import com.cgreen.ygocardtracker.util.CardImageSaver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class CustomCardEntryController {
    private Stage stage;
    private ObservableList<CardType> cardTypeOptions;
    private ObservableList<CardVariant> cardVariantOptions;
    private List<TextInputControl> textInputs;
    private List<CheckBox> selectedLinkArrowBoxes;
    @FXML
    private ChoiceBox<CardType> typeChoice;
    @FXML
    private ChoiceBox<CardVariant> variantChoice;
    @FXML
    private ChoiceBox<Attribute> attributeChoice;
    @FXML
    private TextField nameField, passcodeField, atkField, defField, setCodeField, imagePathField, smallImagePathField;
    @FXML
    private TextArea descArea, pendulumDescArea;
    @FXML
    private Button imageBrowseButton, smallImageBrowseButton, saveButton, cancelButton;
    @FXML
    private CheckBox fakeCheck, topLeftCheck, topCheck, topRightCheck, rightCheck, bottomRightCheck, bottomCheck, bottomLeftCheck, leftCheck;
    @FXML
    private Slider levelSlider, scaleSlider;
    @FXML
    private GridPane linkArrowPane; 
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void init() {
        cardTypeOptions = FXCollections.observableArrayList();
        cardVariantOptions = FXCollections.observableArrayList();
        for (int i = 0; i < CardType.getNumberOfCardTypes(); i++) {
            cardTypeOptions.add(CardType.getCardType(i));
        }
        FXCollections.sort(cardTypeOptions, new Comparator<CardType>() {
            @Override
            public int compare(CardType ct1, CardType ct2) {
                return ct1.toString().compareTo(ct2.toString());
            }
        });
        typeChoice.setItems(cardTypeOptions);
        typeChoice.setValue(CardType.UNKNOWN);
        for (int i = 0; i < CardVariant.getNumberOfCardVariants(); i++) {
            cardVariantOptions.add(CardVariant.getCardVariant(i));
        }
        variantChoice.setItems(cardVariantOptions);
        variantChoice.setValue(CardVariant.UNKNOWN);

        ObservableList<Attribute> attributes = FXCollections.observableArrayList();
        attributes.addAll(Arrays.asList(Attribute.values()));
        attributeChoice.setItems(attributes);
        attributeChoice.setValue(Attribute.NONE);
        
        imagePathField.setEditable(false);
        smallImagePathField.setEditable(false);
        
        textInputs = new ArrayList<TextInputControl>();
        textInputs.add(nameField);
        textInputs.add(passcodeField);
        textInputs.add(atkField);
        textInputs.add(defField);
        textInputs.add(setCodeField);
        textInputs.add(imagePathField);
        textInputs.add(smallImagePathField);
        textInputs.add(descArea);
        textInputs.add(pendulumDescArea);
        
        selectedLinkArrowBoxes = new ArrayList<CheckBox>();
    }
    
    public void handleTypeChoiceAction() {
        CardModel cardModel = CardModelFactory.getCardModel(typeChoice.getValue());
        atkField.setDisable(!cardModel.isMonster());
        defField.setDisable(!cardModel.isMonster() || cardModel.isLink());
        levelSlider.setDisable(!cardModel.hasLevel() && !cardModel.hasRank());
        scaleSlider.setDisable(!cardModel.isPendulum());
        pendulumDescArea.setDisable(!cardModel.isPendulum());
        linkArrowPane.setDisable(!cardModel.isLink());
        if (cardModel.isSpell() || cardModel.isTrap()) {
            attributeChoice.setValue(Attribute.NONE);
            attributeChoice.setDisable(true);
        } else {
            attributeChoice.setDisable(false);
        }
    }
    
    public void handleFakeCheckAction(ActionEvent event) {
        passcodeField.setDisable(fakeCheck.isSelected());
    }
    
    public void handleImageBrowseButtonAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select card image");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Compatible Images", Arrays.asList("*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.png", "*.PNG"))
        );
        File imgFile = fc.showOpenDialog(stage);
        if (imgFile != null) {
            imagePathField.setText(imgFile.toString());
        }
    }
    
    public void handleSmallImageBrowseButtonAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select small card image");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Compatible Images", Arrays.asList("*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.png", "*.PNG"))
        );
        File imgFile = fc.showOpenDialog(stage);
        if (imgFile != null) {
            smallImagePathField.setText(imgFile.toString());
        }
    }
    
    public void handleLinkArrowCheckBoxAction(ActionEvent event) {
        CheckBox box = ((CheckBox) event.getSource());
        if (box.isSelected()) {
            selectedLinkArrowBoxes.add(box);
        } else {
            selectedLinkArrowBoxes.remove(box);
        }
    }
    
    // TODO: This method is a prime candidate for refactoring.
    public void handleSaveButtonAction(ActionEvent event) {
        // TODO: Indeterminate progress box
        // TODO: Disable buttonbar at this point and reenable it later
        if (hasValidSelections() && !hasEmptyFields()) {
            // TODO: Copy image files into card_images and set the correct image links
            if (!fakeCheck.isSelected()) {
                Alert areYouSure = new Alert(AlertType.WARNING, "Manually entering card information for a real card may interfere with future downloads of info for cards with this name or passcode. This can be remedied by deleting card information."
                        + "\n"
                        + "\nAre you sure you wish to proceed?", ButtonType.YES, ButtonType.NO);
                areYouSure.showAndWait();
                if (areYouSure.getResult() != ButtonType.YES) {
                    return;
                }
            }
            CardInfoDao cardInfoDao = new CardInfoDao();
            CardInfo cardInfo = new CardInfo();
            cardInfo.setNameCol(nameField.getText());
            cardInfo.setIsFakeCol(fakeCheck.isSelected());
            cardInfo.setCardTypeCol(typeChoice.getValue());
            cardInfo.setVariantCol(variantChoice.getValue());
            if (pendulumDescArea.isDisabled()) {
                cardInfo.setDescriptionCol(descArea.getText());
            } else {
                String fullDesc = "[ Pendulum Effect ]\\r\\n" + pendulumDescArea.getText();
                CardModel cardModel = CardModelFactory.getCardModel(typeChoice.getValue());
                if (cardModel.hasEffect()) {
                    fullDesc += "\\r\\n----------------------------------------\\r\\n[ Monster Effect ]\\r\\n" + descArea.getText();
                } else {
                    fullDesc += "\\r\\n----------------------------------------\\r\\n[ Flavor Text ]\\r\\n" + descArea.getText();
                }
                cardInfo.setDescriptionCol(fullDesc);
            }
            cardInfo.setAttributeCol(attributeChoice.getValue());
            if (!atkField.isDisabled()) {
                cardInfo.setAttackCol(Integer.parseInt(atkField.getText()));
            }
            if (!defField.isDisabled()) {
                cardInfo.setDefenseCol(Integer.parseInt(defField.getText()));
            }
            if (!levelSlider.isDisabled()) {
                cardInfo.setLevelCol((int) levelSlider.getValue());
            }
            if (!scaleSlider.isDisabled()) {
                cardInfo.setScaleCol((int) scaleSlider.getValue());
            }
            if (!linkArrowPane.isDisabled()) {
                cardInfo.setLinkValueCol(selectedLinkArrowBoxes.size());
                String[] linkMarkers = new String[selectedLinkArrowBoxes.size()];
                int index = 0;
                if (topLeftCheck.isSelected()) {
                    linkMarkers[index] = "Top-Left";
                    index++;
                }
                if (topCheck.isSelected()) {
                    linkMarkers[index] = "Top";
                    index++;
                }
                if (topRightCheck.isSelected()) {
                    linkMarkers[index] = "Top-Right";
                    index++;
                }
                if (rightCheck.isSelected()) {
                    linkMarkers[index] = "Right";
                    index++;
                }
                if (bottomRightCheck.isSelected()) {
                    linkMarkers[index] = "Bottom-Right";
                    index++;
                }
                if (bottomCheck.isSelected()) {
                    linkMarkers[index] = "Bottom";
                    index++;
                }
                if (bottomLeftCheck.isSelected()) {
                    linkMarkers[index] = "Bottom-Left";
                    index++;
                }
                if (leftCheck.isSelected()) {
                    linkMarkers[index] = "Left";
                }
                String linkMarkersText = "";
                for (index = 0; index < linkMarkers.length - 1; index++) {
                    linkMarkersText += linkMarkers[index] + ",";
                }
                linkMarkersText += linkMarkers[index];
                cardInfo.setLinkMarkersCol(linkMarkersText);
            }
            cardInfo.setSetCodesCol(setCodeField.getText());
            if (cardInfo.isFake()) {
                final int tempPasscode = 100000000;
                try {
                    SetCodeDao setCodeDao = new SetCodeDao();
                    cardInfo.setSetCodeId(setCodeDao.save(cardInfo.getSetCodes()));
                    CardInfo temp = cardInfoDao.getCardInfoByPasscode(tempPasscode);
                    if (temp != null) {
                        cardInfoDao.delete(temp);
                    }
                    cardInfo.setImageLinkCol(imagePathField.getText());
                    cardInfo.setSmallImageLinkCol(smallImagePathField.getText());
                    cardInfo.setPasscodeCol(tempPasscode);
                    cardInfoDao.save(cardInfo);
                } catch (SQLException e) {
                    AlertHelper.raiseAlert("Error saving card information.\n\n" + e.getMessage());
                    return;
                }
                // TODO: Log what we're doing here
                int newPasscode = tempPasscode + cardInfo.getId();
                try {
                    cardInfoDao.updateFakeCardInfoPasscode(cardInfo, newPasscode);
                    // Try saving images
                    try {
                        String imgPath = imagePathField.getText();
                        Image img = ImageIO.read(new File(imgPath));
                        String resavedPath = CardImageSaver.saveCardImageFile(img, cardInfo.getPasscode(), imgPath.substring(imgPath.lastIndexOf('.') + 1), true);
                        cardInfoDao.updateCardInfoImage(cardInfo, resavedPath);
                    } catch (IOException e) {
                        // TODO Log that the image could not be saved
                        System.out.println("Problem saving image: " + e.getMessage());
                    }
                    try {
                        String smallImgPath = smallImagePathField.getText();
                        Image img = ImageIO.read(new File(smallImgPath));
                        String resavedPath = CardImageSaver.saveCardImageFileSmall(img, cardInfo.getPasscode(), smallImgPath.substring(smallImgPath.lastIndexOf('.') + 1), true);
                        cardInfoDao.updateCardInfoImageSmall(cardInfo, resavedPath);
                    } catch (IOException e) {
                        // TODO Log that the image could not be saved
                    }
                } catch (SQLException e) {
                    AlertHelper.raiseAlert("Error saving card information.\n\n" + e.getMessage());
                    return;
                }
            } else {
                cardInfo.setPasscodeCol(Integer.parseInt(passcodeField.getText()));
                int numCardInfoWithSamePasscode = cardInfoDao.getNumCardInfos(cardInfo.getPasscode());
                if (numCardInfoWithSamePasscode > 0) {
                    try {
                        CardInfo otherInfo = cardInfoDao.getCardInfoByPasscode(cardInfo.getPasscode());
                        if (otherInfo.getName().equals(cardInfo.getName())) {
                            Alert confirmMerge = new Alert(AlertType.CONFIRMATION, "Card information already exists for this name and passcode. Merge this set code into the existing entry? (This will discard all new entries except for the set code).");
                            confirmMerge.showAndWait();
                            if (confirmMerge.getResult() == ButtonType.OK) {
                                try {
                                    boolean alreadyHasSetCode = false;
                                    String[] setCodes = otherInfo.getSetCodes().split(",");
                                    for (String setCode : setCodes) {
                                        alreadyHasSetCode = alreadyHasSetCode || setCode.equals(cardInfo.getSetCodes());
                                    }
                                    if (!alreadyHasSetCode) {
                                        SetCodeDao setCodeDao = new SetCodeDao();
                                        setCodeDao.update(otherInfo.getSetCodeId(), otherInfo.getSetCodes() + "," + cardInfo.getSetCodes());
                                    }
                                } catch (SQLException sqle) {
                                    sqle.printStackTrace();
                                    AlertHelper.raiseAlert("Error updating set code information. Updates may not be complete.");
                                    return;
                                }
                                Card card = new Card();
                                card.setCardInfoId(otherInfo.getId());
                                card.setDeckId(1);
                                card.setGroupId(1);
                                card.setSetCode(cardInfo.getSetCodes());
                                card.setInSideDeck(false);
                                card.setIsVirtual(false);
                                CardDao cardDao = new CardDao();
                                try {
                                    cardDao.save(card);
                                } catch (SQLException | RuntimeException e) {
                                    e.printStackTrace();
                                    AlertHelper.raiseAlert("Error saving a copy of this card to the collection."
                                            + "\n"
                                            + "\nTo retry, search by this card's name (\"" + otherInfo.getName() + "\") or by passcode #" + otherInfo.getPasscode() + ".");
                                    return;
                                }
                                Alert successAgain = new Alert(AlertType.INFORMATION, "Copy of \"" + cardInfo.getName() + "\" saved to collection.");
                                successAgain.showAndWait();
                                return;
                            } else {
                                AlertHelper.raiseAlert("New card information was not saved.");
                                return;
                            }
                        } else {
                            AlertHelper.raiseAlert("Card information already exists for the passcode " + cardInfo.getPasscode() + " under the name \"" + otherInfo.getName() + ".\"");
                            return;
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        AlertHelper.raiseAlert("Error reading from the database.");
                        return;
                    }
                } else if (numCardInfoWithSamePasscode == -1) {
                    AlertHelper.raiseAlert("Error reading from the database.");
                    return;
                } else {
                    try {
                        SetCodeDao setCodeDao = new SetCodeDao();
                        cardInfo.setSetCodeId(setCodeDao.save(setCodeField.getText()));
                        // Try saving images
                        try {
                            String imgPath = imagePathField.getText();
                            Image img = ImageIO.read(new File(imgPath));
                            String resavedPath = CardImageSaver.saveCardImageFile(img, cardInfo.getPasscode(), imgPath.substring(imgPath.lastIndexOf('.') + 1), true);
                            cardInfo.setImageLinkCol(resavedPath);
                        } catch (IOException e) {
                            // TODO Log that the image could not be saved
                            System.out.println("Problem saving image: " + e.getMessage());
                            cardInfo.setImageLinkCol(imagePathField.getText());
                        }
                        try {
                            String smallImgPath = smallImagePathField.getText();
                            Image img = ImageIO.read(new File(smallImgPath));
                            String resavedPath = CardImageSaver.saveCardImageFileSmall(img, cardInfo.getPasscode(), smallImgPath.substring(smallImgPath.lastIndexOf('.') + 1), true);
                            cardInfo.setSmallImageLinkCol(resavedPath);
                        } catch (IOException e) {
                            // TODO Log that the image could not be saved
                            cardInfo.setSmallImageLinkCol(smallImagePathField.getText());
                        }
                        cardInfoDao.save(cardInfo);
                    } catch (SQLException e) {
                        AlertHelper.raiseAlert("Error saving card information to the database.\n" + e.getMessage());
                        return;
                    }
                }
            }
            Alert success = new Alert(AlertType.INFORMATION, "Card information successfully saved for \"" + cardInfo.getName() + ".\"");
            success.showAndWait();
            Card card = new Card();
            card.setCardInfoId(cardInfo.getId());
            card.setDeckId(1);
            card.setGroupId(1);
            // There's only one set code in cardInfo at the moment
            card.setSetCode(cardInfo.getSetCodes());
            card.setIsVirtual(cardInfo.isFake());
            card.setInSideDeck(false);
            CardDao cardDao = new CardDao();
            try {
                cardDao.save(card);
            } catch (SQLException | RuntimeException e) {
                e.printStackTrace();
                AlertHelper.raiseAlert("Error saving a copy of this card to the collection."
                        + "\n"
                        + "\nTo retry, search by this card's name (\"" + cardInfo.getName() + "\") or by passcode #" + cardInfo.getPasscode() + ".");
                return;
            }
            Alert successAgain = new Alert(AlertType.INFORMATION, "Copy of \"" + cardInfo.getName() + "\" saved to collection." +
                    "\n" +
                    "\nAdditional copies of this card may be added by searching by name or by passcode #" + cardInfo.getPasscode() + ".");
            successAgain.showAndWait();
        }
    }
    
    public void handleCancelButtonAction(ActionEvent event) {
        
    }
    
    private boolean hasValidSelections() {
        CardType selectedType = typeChoice.getValue();
        if (selectedType == CardType.SKILL) {
            AlertHelper.raiseAlert("Sorry, Skill cards are not supported.");
            return false;
        }
        CardVariant selectedVariant = variantChoice.getValue();
        if (selectedType == CardType.UNKNOWN || selectedVariant == CardVariant.UNKNOWN) {
            AlertHelper.raiseAlert("Please select appropriate categories for this card.");
            return false;
        }
        Attribute selectedAttribute = attributeChoice.getValue();
        if (!attributeChoice.isDisabled() && selectedAttribute == Attribute.NONE) {
            AlertHelper.raiseAlert("Select an attribute for this card.");
            return false;
        }
        boolean hasTypeVariantConflict = false;
        switch(selectedType) {
        case SPELL:
            hasTypeVariantConflict = !(selectedVariant == CardVariant.SPELL_NORMAL ||
                selectedVariant == CardVariant.SPELL_FIELD || 
                selectedVariant == CardVariant.SPELL_EQUIP ||
                selectedVariant == CardVariant.SPELL_CONTINUOUS || 
                selectedVariant == CardVariant.SPELL_QUICK_PLAY ||
                selectedVariant == CardVariant.SPELL_RITUAL);
            break;
        case TRAP:
            hasTypeVariantConflict = !(selectedVariant == CardVariant.TRAP_NORMAL ||
                selectedVariant == CardVariant.TRAP_CONTINUOUS ||
                selectedVariant == CardVariant.TRAP_COUNTER);
            break;
        default:
            hasTypeVariantConflict = (selectedVariant == CardVariant.SPELL_NORMAL ||
                selectedVariant == CardVariant.SPELL_FIELD || 
                selectedVariant == CardVariant.SPELL_EQUIP ||
                selectedVariant == CardVariant.SPELL_CONTINUOUS || 
                selectedVariant == CardVariant.SPELL_QUICK_PLAY ||
                selectedVariant == CardVariant.SPELL_RITUAL ||
                selectedVariant == CardVariant.TRAP_NORMAL ||
                selectedVariant == CardVariant.TRAP_CONTINUOUS ||
                selectedVariant == CardVariant.TRAP_COUNTER);
        }
        if (hasTypeVariantConflict) {
            AlertHelper.raiseAlert("Invalid Type selected for this card.");
            return false;
        }
        if (!passcodeField.isDisabled() && !passcodeField.getText().isBlank()) {
            try {
                Integer passcode = Integer.parseInt(passcodeField.getText());
                if (passcode < 0 || passcode > 99999999) {
                    AlertHelper.raiseAlert("Passcode is out of range: must be between 00000001 and 99999999");
                    return false;
                }
            } catch (NumberFormatException nfe) {
                AlertHelper.raiseAlert("Please enter a number for the passcode (range: 00000001 - 99999999).");
                return false;
            }
        }
        if (!atkField.isDisabled() && !atkField.getText().isBlank()) {
            try {
                Integer attack = Integer.parseInt(atkField.getText());
                if (attack < 0) {
                    AlertHelper.raiseAlert("ATK cannot be a negative value.");
                    return false;
                }
            } catch (NumberFormatException nfe) {
                AlertHelper.raiseAlert("Please enter a number for ATK (enter 0 for '?').");
                return false;
            }
        }
        if (!defField.isDisabled() && !defField.getText().isBlank()) {
            try {
                Integer defense = Integer.parseInt(defField.getText());
                if (defense < 0) {
                    AlertHelper.raiseAlert("DEF cannot be a negative value.");
                    return false;
                }
            } catch (NumberFormatException nfe) {
                AlertHelper.raiseAlert("Please enter a number for DEF (enter 0 for '?').");
                return false;
            }
        }
        if (setCodeField.getText().contains(",")) {
            AlertHelper.raiseAlert("Invalid character ',' in set code.");
            return false;
        }
        return true;
    }
    
    private boolean hasEmptyFields() {
        boolean hasEmptyFields = false;
        for (TextInputControl input : textInputs) {
            if (!input.isDisabled() && input.getText().isBlank()) {
                hasEmptyFields = true;
                break;
            }
        }
        if (hasEmptyFields) {
            AlertHelper.raiseAlert("Please ensure that all available fields are filled before saving.");
        } else if (!linkArrowPane.isDisabled() && selectedLinkArrowBoxes.isEmpty()) {
            hasEmptyFields = true;
            AlertHelper.raiseAlert("Please select at least one Link Arrow.");
        }
        return hasEmptyFields;
    }
}
