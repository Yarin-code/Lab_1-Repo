package com.example.lab_2;

import javafx.application.Platform;
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


            Scanner reader = new Scanner(myFile);
            while (reader.hasNext()) {
                String email = reader.next();
                if (!reader.hasNext()) break;
                String password = reader.next();
                try {
                    // ניסיון יצירת משתמש ובדיקה מול ה-Regex
                    validUsers.add(new User(email, password));
                } catch (Exception e) {
                    // הדפסה שתסביר למה משתמש ספציפי נפסל (למשל סיסמה לא תקינה)
                    System.out.println("Skipping user " + email + ": " + e.getMessage());
                }
            }
            reader.close();
            System.out.println("Successfully loaded " + validUsers.size() + " valid users.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoginClick() {
        String emailInput = usernameField.getText();
        String passInput = passwordField.getText();

        //  חיפוש המשתמש
        User foundUser = null;
        for (User user : validUsers) {
            if (user.getEmail().equalsIgnoreCase(emailInput)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            errorLabel.setText("User or password do not match");
            return;
        }

        final User targetUser = foundUser;

        if (targetUser.isLocked()) {
            long remaining = (targetUser.getLockoutEndTime() - System.currentTimeMillis()) / 1000;
            if (remaining > 0) {
                errorLabel.setText("User is locked! Try again in " + remaining + " seconds.");
                return; //
            }
        }

        if (targetUser.getPassword().equals(passInput)) {
            // חוט ב' - בדיקת תקינות בזמן אמת
            new Thread(() -> {
                if (targetUser.isLocked()) {
                    Platform.runLater(() -> errorLabel.setText("Access Denied: You are still locked."));
                } else {
                    targetUser.resetFailedAttempts();
                    Platform.runLater(this::openWelcomeScreen);
                }
            }).start();
        } else {
            // חוט א' - עדכון ניסיונות וחסימה
            new Thread(() -> {
                targetUser.incrementFailedAttempts();
                int attempts = targetUser.getFailedAttempts();

                if (attempts >= HelloApplication.maxAttempts) {
                    // חסימה: קביעת זמן הסיום
                    long endTime = System.currentTimeMillis() + (HelloApplication.lockoutTime * 1000L);
                    targetUser.setLockoutEndTime(endTime);

                    Platform.runLater(() -> errorLabel.setText("Too many attempts! Locked for " + HelloApplication.lockoutTime + "s."));

                    try {
                        Thread.sleep(HelloApplication.lockoutTime * 1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    targetUser.resetFailedAttempts();
                    targetUser.setLockoutEndTime(0);

                    Platform.runLater(() -> errorLabel.setText("Lockout ended. You can try again."));
                } else {
                    int left = HelloApplication.maxAttempts - attempts;
                    Platform.runLater(() -> errorLabel.setText("Wrong password. " + left + " attempts left."));
                }
            }).start();
        }
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