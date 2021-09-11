package com.cgreen.ygocardtracker.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cgreen.ygocardtracker.card.data.CardInfo;
import com.cgreen.ygocardtracker.dao.impl.CardInfoDao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.remote.RemoteDBKey;
import com.cgreen.ygocardtracker.util.AlertHelper;
import com.cgreen.ygocardtracker.util.CardInfoSaver;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddCardsMenuController {
    private Stage stage;
    @FXML
    private TextField nameSearchField, passcodeSearchField;
    @FXML
    private Button addByNameButton, addByPasscodeButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void handleAddByNameButtonAction(ActionEvent event) {
        addByNameButton.setDisable(true);
        nameSearchField.setDisable(true);
        Stage progress = createProgressBar();
        progress.show();
        String searchTerm = nameSearchField.getText();
        JSONArray data = doOnlineSearch(RemoteDBKey.NAME, searchTerm);
        // TODO: Might just want 200, not 201?
        if (data == null) {
            System.out.println("Online search failed -- resorting to local DB");
            try {
                CardInfoDao dao = new CardInfoDao();
                ObservableList<CardInfo> cardInfos = dao.getCardInfoByName(searchTerm);
                if (cardInfos.isEmpty()) {
                    AlertHelper.raiseAlert("No card info found for \"" + searchTerm + "\".");
                } else {
                    showConfirmationScreen(cardInfos);
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                AlertHelper.raiseAlert(e.getStackTrace().toString());
            }
        } else {
            // Save info for this card (may include multiple IDs)
            CardInfoSaver cis = new CardInfoSaver();
            List<CardInfo> newlySavedCardInfo = cis.saveCardInfoFromJson(data);
            if (newlySavedCardInfo.isEmpty()) {
                try {
                    CardInfoDao dao = new CardInfoDao();
                    ObservableList<CardInfo> cardInfos = dao.getCardInfoByName(searchTerm);
                    if (cardInfos.isEmpty()) {
                        AlertHelper.raiseAlert("No cards were found matching \"" + searchTerm + ".\" This could be due to an error while locating card information, or there could be an invalid passcode in the database.");
                    } else {
                        showConfirmationScreen(cardInfos);
                    }
                } catch (SQLException e) {
                    AlertHelper.raiseAlert(e.getStackTrace().toString());
                }
            } else {
                showConfirmationScreen(newlySavedCardInfo);
            }
        }
        progress.close();
        addByNameButton.setDisable(false);
        nameSearchField.setDisable(false);
    }
    
    public void handleAddByPasscodeButtonAction(ActionEvent event) {
        addByPasscodeButton.setDisable(true);
        passcodeSearchField.setDisable(true);
        Stage progress = createProgressBar();
        progress.show();
        try {
            Integer searchTerm = Integer.parseInt(passcodeSearchField.getText());
            CardInfoDao dao = new CardInfoDao();
            ObservableList<CardInfo> cardInfos = dao.getCardInfoByPasscode(searchTerm);
            if (cardInfos.isEmpty()) {
                JSONArray data = doOnlineSearch(RemoteDBKey.PASSCODE, searchTerm.toString());
                if (data == null) {
                    AlertHelper.raiseAlert("Could not find id #" + searchTerm);
                } else {
                    CardInfoSaver cis = new CardInfoSaver();
                    List<CardInfo> newlySavedCardInfo = cis.saveCardInfoFromJson(data);
                    showConfirmationScreen(newlySavedCardInfo);
                }
            } else if (cardInfos.size() > 1) {
                AlertHelper.raiseAlert("Multiple records found for passcode #" + String.format("%08d", searchTerm) + ". Please try searching by card name.");
            } else {
                showConfirmationScreen(cardInfos);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException nfe) {
            AlertHelper.raiseAlert("You need to enter an integer here...");
        }
        progress.close();
        addByPasscodeButton.setDisable(false);
        passcodeSearchField.setDisable(false);
    }
    
    private JSONArray doOnlineSearch(RemoteDBKey key, String value) {
        // Do the online search
        // https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
        // https://stackoverflow.com/questions/8760052/httpurlconnection-sends-a-post-request-even-though-httpcon-setrequestmethodget
        // TODO: Refactor this into its own class so the URL is kept in one place
        try {
            URL ygoDb = new URL("https://db.ygoprodeck.com/api/v7/cardinfo.php?" + key.toString() + "=" + URLEncoder.encode(value, "UTF-8"));
            System.out.println(ygoDb);
            HttpURLConnection connection = (HttpURLConnection) ygoDb.openConnection();
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();
            // https://stackoverflow.com/questions/10500775/parse-json-from-httpurlconnection-object
            int status = connection.getResponseCode();
            System.out.println(status + ": " + connection.getResponseMessage());
            switch (status) {
            case 200:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                // https://stackoverflow.com/a/29183161
                System.out.println(sb);
                JSONArray data = (JSONArray) new JSONObject(sb.toString()).get("data");
                /*for (int i = 0; i < data.length(); i++) {
                    JSONObject cardData = data.getJSONObject(i);
                    System.out.println(cardData.get("name"));
                }*/
                return data;
            case 400:
                break;
            default:
                AlertHelper.raiseAlert("There was a problem contacting the remote database.");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            AlertHelper.raiseAlert("Error", "YGOCardTracker has encountered an error.", "There was a problem contacting the remote database.");
        }
        return null;
    }
    
    private Stage createProgressBar() {
        // TODO: Make this display correctly, and actually track progress.
        Label label = new Label("Checking card info...");
        label.setPrefWidth(350);
        ProgressBar pBar = new ProgressBar();
        pBar.setPrefWidth(350);
        pBar.setProgress(-1);
        VBox vbox = new VBox(10, label, pBar);
        vbox.setPadding(new Insets(10));
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle("Searching...");
        stage.setScene(new Scene(vbox));
        return stage;
    }
    
    private void showConfirmationScreen(List<CardInfo> savedCardInfo) {
        // TODO: Review/revise how these exceptions are getting handled.
        try {
            FXMLLoader loader = new FXMLLoader(CardConfirmerController.class.getClassLoader().getResource("card_confirmation_menu.fxml"));
            Parent parent = loader.load();        
            
            Stage cardConfirmerStage = new Stage();
            cardConfirmerStage.setScene(new Scene(parent));
            CardConfirmerController ccc = loader.getController();
            ccc.setStage(cardConfirmerStage);
            ccc.init(savedCardInfo);
            cardConfirmerStage.initModality(Modality.APPLICATION_MODAL);
            cardConfirmerStage.show();
        } catch (SQLException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
