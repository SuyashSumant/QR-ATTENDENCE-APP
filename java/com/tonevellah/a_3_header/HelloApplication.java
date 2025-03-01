////////////// Start
package com.tonevellah.a_3_header;

import com.tonevellah.a_3_header.Camera; // Import the Camera class
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelloApplication extends Application {
    static {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }


    Button button = new Button("Admin Login");
    Label dateTimeLabel = new Label();
    Label profileLabel = new Label(); // Label to show profile info
    Button cameraButton = new Button("Open Camera"); // Add a camera button
    Scene mainScene; // Store the main scene

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label headerLabel = new Label("Company Name");
        headerLabel.setFont(new Font("Arial", 24));

        Rectangle box = new Rectangle(150, 150);
        box.setFill(Color.LIGHTGRAY);
        box.setOpacity(-1);

        HBox headerBox = new HBox(headerLabel);
        headerBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10;");
        headerBox.setAlignment(Pos.CENTER);

        Line separator = new Line();

        StackPane boxPane = new StackPane(box);
        boxPane.setAlignment(Pos.CENTER);

        VBox headerArea = new VBox(headerBox, separator);
        headerArea.setAlignment(Pos.TOP_CENTER);

        // Separate Date and Time Labels
        Label dateLabel = new Label();
        Label timeLabel = new Label();

        HBox dateTimeButtonArea = new HBox(dateLabel, separator, timeLabel);
        dateTimeButtonArea.setAlignment(Pos.TOP_LEFT);
        dateTimeButtonArea.setPadding(new Insets(5, 5, 5, 5));

        dateLabel.setStyle("-fx-background-color: gray; -fx-padding: 7; -fx-border-radius: 30;");
        dateLabel.setFont(new Font("Arial", 12));
        timeLabel.setStyle("-fx-background-color: cyan; -fx-padding: 7; -fx-border-radius: 30;");
        timeLabel.setFont(new Font("Arial", 12));

        HBox buttonArea = new HBox(button);
        buttonArea.setAlignment(Pos.TOP_RIGHT);
        buttonArea.setPadding(new Insets(5, 5, 5, 5));
        HBox.setHgrow(buttonArea, javafx.scene.layout.Priority.ALWAYS);

        dateLabel.setTranslateY(-25);
        dateLabel.setTranslateX(-5);
        timeLabel.setTranslateY(-25);
        timeLabel.setTranslateX(-5);
        button.setTranslateY(-25);
        button.setTranslateX(5);

        button.setStyle("-fx-text-fill: black; -fx-background-color: #4CAF50; -fx-border-radius: 30; -fx-background-radius: 10; -fx-padding: 5;");

        HBox datiad = new HBox();
        datiad.getChildren().addAll(dateTimeButtonArea, buttonArea);

        VBox boxArea = new VBox(boxPane);
        boxArea.setAlignment(Pos.CENTER);

        Label boxLabel = new Label("Scan QR Code");
        boxLabel.setFont(new Font("Arial", 20));
        VBox boxDetail = new VBox(boxLabel);
        boxDetail.setAlignment(Pos.TOP_CENTER);

        // Camera button setup
        cameraButton.setStyle("-fx-text-fill: black; -fx-background-color: #007BFF; -fx-border-radius: 30; -fx-background-radius: 10; -fx-padding: 5;");
        HBox cameraButtonArea = new HBox(cameraButton);
        cameraButtonArea.setAlignment(Pos.CENTER);
        cameraButtonArea.setPadding(new Insets(10, 50, 02, 30)); // Add padding

//        VBox proCab = new VBox();
//        proCab.setSpacing(10);
//        proCab.setPadding(new Insets(10));
//        proCab.setAlignment(Pos.BASELINE_CENTER);
//        boxPane.getChildren().addAll(boxArea, proCab);

        VBox root = new VBox(headerArea, datiad, boxDetail,  boxArea, profileLabel, cameraButtonArea); // Add cameraButtonArea
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
//        root.getChildren().addAll(headerArea,datiad,boxArea,boxDetail,profileLabel,cameraButtonArea,proCab);

        mainScene = new Scene(root, 450, 550); // Store the main scene

        primaryStage.setTitle("Header Example");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        button.setOnAction(e -> {
            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Login");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(8);
            grid.setHgap(10);

            Label nameLabel = new Label("Username");
            GridPane.setConstraints(nameLabel, 0, 0);
            TextField nameInput = new TextField();
            nameInput.setPromptText("Enter Username");
            GridPane.setConstraints(nameInput, 1, 0);
            Label passLabel = new Label("Password");
            GridPane.setConstraints(passLabel, 0, 1);
            TextField passInput = new TextField();
            passInput.setPromptText("Enter Password");
            GridPane.setConstraints(passInput, 1, 1);
            Button loginButton = new Button("Log In");
            GridPane.setConstraints(loginButton, 1, 2);

            grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, loginButton);

            Label adminTitle = new Label("Admin Login");
            adminTitle.setFont(new Font("Arial", 18));
            VBox adminRoot = new VBox(adminTitle, grid);
            adminRoot.setAlignment(Pos.CENTER);

            loginButton.setOnAction(f -> {
                String username = nameInput.getText();
                String password = passInput.getText();
                // Basic validation (replace with your actual login logic)
                if (!username.isEmpty() && !password.isEmpty()) {
                    profileLabel.setText("Logged Admin: " + username); // Show profile
                    primaryStage.setScene(mainScene); // Return to main scene
                    adminStage.close(); // Close login window
                } else {
                    // Handle invalid login
                    profileLabel.setText("Invalid Login");
                }
            });

            Scene adminScene = new Scene(adminRoot, 300, 200);
            adminStage.setScene(adminScene);
            adminStage.show();
        });

        cameraButton.setOnAction(e -> {
            // Open the Camera window in a new thread
            new Thread(() -> {
                System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
                Camera camera = new Camera();
                camera.startCamera();
            }).start();
        });

        updateDateTime(dateLabel, timeLabel);
    }

    private void updateDateTime(Label dateLabel, Label timeLabel) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            dateLabel.setText(dateFormatter.format(now));
            timeLabel.setText(timeFormatter.format(now));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

