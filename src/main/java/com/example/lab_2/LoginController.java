package com.example.lab_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private ArrayList<User> validUsers = new ArrayList<>();

    @FXML
    public void initialize() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            File myFile = new File("Users.txt");
            if (!myFile.exists()) return;
            Scanner reader = new Scanner(myFile);
            while (reader.hasNext()) {
                String email = reader.next();
                if (!reader.hasNext()) break;
                String password = reader.next();
                try {
                    // הוספת משתמשים תקינים בלבד כפי שנדרש
                    validUsers.add(new User(email, password));
                } catch (Exception e) {

                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoginClick() {
        String emailInput = usernameField.getText();
        String passInput = passwordField.getText();

        for (User user : validUsers) {
            if (user.getEmail().equals(emailInput) && user.getPassword().equals(passInput)) {
                openWelcomeScreen();
                return;
            }
        }
        errorLabel.setText("user or password do not match");
    }

    private void openWelcomeScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
