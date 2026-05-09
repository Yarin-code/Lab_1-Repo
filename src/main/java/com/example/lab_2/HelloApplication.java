package com.example.lab_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

    // פרמטרים דיפולטיבים
    public static int maxAttempts = 3;
    public static int lockoutTime = 10; // בשניות

    @Override
    public void start(Stage stage) throws Exception {
        // קריאת פרמטרים משורת הפקודה
        List<String> args = getParameters().getRaw();
        if (args.size() >= 2) {
            try {
                maxAttempts = Integer.parseInt(args.get(0));
                lockoutTime = Integer.parseInt(args.get(1));
            } catch (NumberFormatException e) {
                System.out.println("Invalid runtime parameters. Using defaults (3 attempts, 10 seconds).");
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Users Login");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}