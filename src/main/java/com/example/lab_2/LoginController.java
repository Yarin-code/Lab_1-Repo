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

        // חיפוש משתמש לפי אימייל
        User foundUser = null;
        for (User user : validUsers) {
            if (user.getEmail().equalsIgnoreCase(emailInput)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            errorLabel.setText("User not found"); // השגיאה אם הרשימה ריקה
            return;
        }

        final User targetUser = foundUser;

        if (targetUser.getPassword().equals(passInput)) {
            // חוט ב' - בדיקת חסימה
            new Thread(() -> {
                if (targetUser.isLocked()) {
                    Platform.runLater(() -> errorLabel.setText("User is locked. Try again later."));
                } else {
                    targetUser.resetFailedAttempts();
                    Platform.runLater(this::openWelcomeScreen);
                }
            }).start();
        } else {
            // חוט א' - ניהול ניסיונות כושלים
            new Thread(() -> {
                targetUser.incrementFailedAttempts();
                int attempts = targetUser.getFailedAttempts();

                if (attempts >= HelloApplication.maxAttempts) {
                    long lockEnd = System.currentTimeMillis() + (HelloApplication.lockoutTime * 1000L);
                    targetUser.setLockoutEndTime(lockEnd);
                    Platform.runLater(() -> errorLabel.setText("Too many attempts! Locked for " + HelloApplication.lockoutTime + "s."));
                } else {
                    int remaining = HelloApplication.maxAttempts - attempts;
                    Platform.runLater(() -> errorLabel.setText("Wrong password. " + remaining + " attempts left."));
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