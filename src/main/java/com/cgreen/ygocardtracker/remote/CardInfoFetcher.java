package com.cgreen.ygocardtracker.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.cgreen.ygocardtracker.util.AlertHelper;

public class CardInfoFetcher {
    private static final String REMOTE_DB_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php";
    public static JSONObject doOnlineSearch(RemoteDBKey key, String value) {
        // https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
        // https://stackoverflow.com/questions/8760052/httpurlconnection-sends-a-post-request-even-though-httpcon-setrequestmethodget
        try {
            URL ygoDb = new URL(REMOTE_DB_URL + "?" + key.toString() + "=" + URLEncoder.encode(value, "UTF-8"));
            System.out.println(ygoDb);
            HttpURLConnection connection = (HttpURLConnection) ygoDb.openConnection();
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.connect();
            // https://stackoverflow.com/questions/10500775/parse-json-from-httpurlconnection-object
            int status = connection.getResponseCode();
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
                JSONObject data = new JSONObject(sb.toString());
                return data;
            case 400:
                break;
            default:
                AlertHelper.raiseAlert("Server returned " + status + ": " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            AlertHelper.raiseAlert("Error", "YGOCardTracker has encountered an error.", "There was a problem contacting the remote database.");
        }
        return null;
    }
}
