package com.cgreen.ygocardtracker;

import java.io.IOException;
import java.sql.SQLException;

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
                
                User user = new User();
                user.setUsername("ygoadmin");
                user.setPassword("password");
                dbm.setUser(user);
                
                dbm.initializeTables();
            
                Stage mainMenuStage = new Stage();
                mainMenuStage.setScene(new Scene(parent));                
                MainMenuController mmc = loader.getController();
                mmc.setStage(mainMenuStage);
                mainMenuStage.show();
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Platform.exit();
        }
    }
}
