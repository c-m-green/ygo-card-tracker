package com.cgreen.ygocardtracker;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class YGOCardTracker extends Application
{
    public static void main(String[] args)
    {
        launch();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        dbm.init();
        promptLogin(primaryStage);
    }
    
    private static void promptLogin(Stage primaryStage) throws IOException, SQLException {
        FXMLLoader loginLoader = new FXMLLoader(YGOCardTracker.class.getClassLoader().getResource("login.fxml"));
        Parent parent = loginLoader.load();
        
        LoginController lc = loginLoader.getController();
        lc.setStage(primaryStage);
        
        primaryStage.setTitle("Log into database");
        primaryStage.setScene(new Scene(parent, 300, 275));
        primaryStage.show();
    }
}
