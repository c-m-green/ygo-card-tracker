package com.cgreen.ygocardtracker;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    
    private Stage stage;
    
    public LoginController() { }
    
    @FXML
    private void handleSignInButtonAction(ActionEvent event) {
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        if (dbm.testConnection(usernameField.getText(), passwordField.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(LoginController.class.getClassLoader().getResource("main_menu.fxml"));
                Parent parent = loader.load();
                
                User user = new User();
                user.setUsername(usernameField.getText());
                user.setPassword(passwordField.getText());
                dbm.setUser(user);
                
                dbm.initializeTables();
            
                stage.hide();
                
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
            displayLoginError();
            usernameField.setDisable(false);
            passwordField.clear();
            passwordField.setDisable(false);
        }
    }
    
    private void displayLoginError() {
        AlertHelper.raiseAlert("Error logging in", "Log in Failed", "Invalid credentials. Please try again.");
    }
    
    protected void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
