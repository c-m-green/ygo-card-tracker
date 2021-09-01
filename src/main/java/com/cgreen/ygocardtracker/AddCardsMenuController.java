package com.cgreen.ygocardtracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCardsMenuController {
    private Stage stage;
    @FXML
    private TextField nameSearchField, passcodeSearchField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void handleAddByNameButtonAction(ActionEvent event) {
        try {
            DatabaseManager dbm = DatabaseManager.getDatabaseManager();
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_card_by_name_query"));
            stmt.setString(1,  nameSearchField.getText());
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (!rs.isBeforeFirst()) {
                promptForOnlineSearchByName(nameSearchField.getText());
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void handleAddByPasscodeButtonAction(ActionEvent event) {
        System.out.println("You pressed the wrong button!");
    }
    
    private void promptForOnlineSearchByName(String searchItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Local card info not found");
        alert.setContentText("Information for the card \"" + searchItem + "\" was not found locally. Search online for card info?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Do the online search
                // https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
                // https://stackoverflow.com/questions/8760052/httpurlconnection-sends-a-post-request-even-though-httpcon-setrequestmethodget
                try {
                    URL ygoDb = new URL("https://db.ygoprodeck.com/api/v7/cardinfo.php?name=" + URLEncoder.encode(searchItem, "UTF-8"));
                    HttpURLConnection connection = (HttpURLConnection) ygoDb.openConnection();
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    // https://stackoverflow.com/questions/10500775/parse-json-from-httpurlconnection-object
                    int status = connection.getResponseCode();
                    System.out.println(status + ": " + connection.getResponseMessage());
                    switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        // https://stackoverflow.com/a/29183161
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        break;
                    case 400:
                        ButtonType customEntry = new ButtonType("Enter custom card info...", ButtonBar.ButtonData.NEXT_FORWARD);
                        Alert promptCustomEntry = new Alert(AlertType.INFORMATION, null, ButtonType.OK, customEntry);
                        promptCustomEntry.setTitle("Card info not found");
                        promptCustomEntry.setHeaderText("Online search failed.");
                        promptCustomEntry.showAndWait().ifPresent(r -> {
                            if (r == customEntry) {
                                System.out.println("Here is where we can manually add card info to the DB.");
                            }
                        });
                    default:
                        AlertHelper.raiseAlert("There was a problem contacting the remote database.");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    AlertHelper.raiseAlert("Error", "YGOCardTracker has encountered an error.", "There was a problem contacting the remote database.");
                }
            }
        });
    }
}
