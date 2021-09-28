package com.cgreen.ygocardtracker;

import java.io.IOException;

import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.User;
import com.cgreen.ygocardtracker.menu.MainMenuController;

import javafx.application.Application;
import javafx.application.Platform;
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
        loadMainMenu(primaryStage);
    }
    
    private static void loadMainMenu(Stage primaryStage) {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        if (dbm.testConnection("ygoadmin", "password")) {
            try {
                FXMLLoader loader = new FXMLLoader(MainMenuController.class.getClassLoader().getResource("main_menu.fxml"));                
                Parent parent = loader.load();
                ((MainMenuController)loader.getController()).init();
                
                User user = new User();
                user.setUsername("ygoadmin");
                user.setPassword("password");
                dbm.setUser(user);
                
                dbm.initializeTables();
            
                primaryStage.setScene(new Scene(parent));                
                primaryStage.show();
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Platform.exit();
        }
    }
}
