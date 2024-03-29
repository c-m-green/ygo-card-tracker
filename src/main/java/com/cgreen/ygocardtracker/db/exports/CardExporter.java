package com.cgreen.ygocardtracker.db.exports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.util.AlertHelper;
import com.cgreen.ygocardtracker.util.Version;

public class CardExporter {
    private static final String CARD_QUERY = "select_cards_for_export_query";
    private static final String DECK_QUERY = "select_deck_table_query";
    private static final String SET_CODE_QUERY = "select_set_codes_for_export_query";
    private static final String GROUP_QUERY = "select_group_table_query";
    private static final String JSON_FILENAME = "collection.json";
    private static final String CSV_FILENAME = "collection.csv";
    private static final String TXT_FILENAME = "set_codes.txt";
    
    public static void exportCollection(File outputDir, boolean doJson, boolean doCsv, boolean doTxt) throws IOException, SQLException {
        if (outputDir == null || !outputDir.isDirectory()) {
            throw new IllegalArgumentException("No directory passed to exporter");
        }
        if (doJson || doCsv || doTxt) {    
            if (doJson) {
                File jsonOut = new File(Paths.get(outputDir.toString(), JSON_FILENAME).toString());
                try (Writer out = new BufferedWriter(new FileWriter(jsonOut));) {
                    JSONObject json = buildJsonFromDatabase();
                    if (json != null) {
                        out.write(json.toString());
                    }
                    // TODO: Log that we succeeded
                } catch (IOException e) {
                    // TODO: Log this
                    throw new IOException("Error writing to " + jsonOut);
                }
            }
            if (doCsv) {
                // TODO
            }
            if (doTxt) {
                File txtOut = new File(Paths.get(outputDir.toString(), TXT_FILENAME).toString());
                try (Writer out = new BufferedWriter(new FileWriter(txtOut));) {
                    String txt = buildTxtFromDatabase();
                    if (txt != null) {
                        out.write(txt);
                    }
                } catch (IOException e) {
                 // TODO: Log this
                    throw new IOException("Error writing to " + txtOut);
                }
            }
            // TODO: Log that exporter is closing
        }
    }
    
    private static JSONObject buildJsonFromDatabase() {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        JSONObject root = new JSONObject();
        JSONArray cards = new JSONArray();
        JSONArray decks = new JSONArray();
        JSONArray groups = new JSONArray();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement cardStmt = conn.prepareStatement(Queries.getQuery(CARD_QUERY));
            PreparedStatement deckStmt = conn.prepareStatement(Queries.getQuery(DECK_QUERY));
            PreparedStatement grpStmt = conn.prepareStatement(Queries.getQuery(GROUP_QUERY));
        ) {            
            cardStmt.executeQuery();
            ResultSet rs = cardStmt.getResultSet();
            while (rs.next()) {
                JSONObject cardData = new JSONObject();
                cardData.put("name", rs.getString("name"));
                cardData.put("passcode", rs.getInt("passcode"));
                cardData.put("set_code", rs.getString("set_code"));
                cardData.put("deck_id", rs.getInt("deck_id"));
                cardData.put("group_id", rs.getInt("group_id"));
                cards.put(cardData);
            }
            deckStmt.executeQuery();
            rs = deckStmt.getResultSet();
            while (rs.next()) {
                JSONObject deckData = new JSONObject();
                deckData.put("notes", rs.getString("note"));
                deckData.put("id", rs.getInt("id"));
                deckData.put("name", rs.getString("name"));
                decks.put(deckData);
            }
            grpStmt.executeQuery();
            rs = grpStmt.getResultSet();
            while (rs.next()) {
                JSONObject grpData = new JSONObject();
                grpData.put("name", rs.getString("name"));
                grpData.put("id", rs.getInt("id"));
                groups.put(grpData);
            }
        } catch (SQLException e) {
            // TODO: Log this
            AlertHelper.raiseAlert("Error exporting collection: " + e.getMessage());
            return null;
        }
        root.put("cards", cards);
        root.put("decks", decks);
        root.put("groups", groups);
        String appVersion = "?.?.?";
        try {
            appVersion = Version.getVersion();
        } catch (IOException e) {
            // TODO Log this
        }
        root.put("app_version", appVersion);
        return root;
    }
    
    private static String buildTxtFromDatabase() {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        List<String> setCodes = new ArrayList<String>();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery(SET_CODE_QUERY));
        ) {
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                setCodes.add(rs.getString("set_code"));
            }
        } catch (SQLException e) {
            AlertHelper.raiseAlert("Error exporting collection: " + e.getMessage());
            return null;
        }
        final int limit = 50;
        StringBuilder sb = new StringBuilder("Max. " + limit + " set codes per line\n");
        for (int i = 0; i < setCodes.size(); i++) {
            int writeIndex = i % 50;
            if (writeIndex == limit - 1) {
                sb.append(setCodes.get(i) + "\n");
            } else if (i < setCodes.size() - 1) {
                sb.append(setCodes.get(i) + ",");
            } else {
                sb.append(setCodes.get(i));
            }
        }
        return sb.toString();
    }
}
