package com.example.lab_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Scanner;

public class HelloApplication extends Application {

    // משתנים סטטיים שיכילו את הערכים שנקלטו בטרמינל
    public static int maxAttempts;
    public static int lockoutTime;

    @Override
    public void start(Stage stage) throws Exception {
        // טעינת המסך רק לאחר שהפרמטרים כבר קיימים במערכת
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Users Login");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> System.exit(0));

        stage.show();
    }

    public static void main(String[] args) {
        // מימוש הדרישה: קבלת פרמטרים בשורת הפקודה בזמן ריצה (לא משורת הרצה)
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Lab 3 Setup ---");
        System.out.print("Enter maximum failed attempts (n): ");
        maxAttempts = scanner.nextInt();

        System.out.print("Enter lockout duration in seconds (t): ");
        lockoutTime = scanner.nextInt();

        System.out.println("Starting GUI...");

        // רק עכשיו, אחרי קבלת הפרמטרים, נפעיל את ה-JavaFX
        launch(args);
    }
}