package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane; // Import GridPane
import javafx.util.Duration;
import javafx.scene.control.Button;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class HelloController {

    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;

    private final DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
    private final DateTimeFormatter TimeFormatter=DateTimeFormatter.ofPattern("hh:mm a");

    public void initialize(){
        startClock();
    }
    private void startClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime now = LocalDateTime.now();
                    dateLabel.setText("Date:" + now.format(dateTimeFormatter));
                    timeLabel.setText("Time:" + now.format(TimeFormatter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @FXML
    private void gotoAdminLogin() {
        ScaneManager.switchTo("adminLogin");
    }
    @FXML
    private void gotoQRScan() {
        ScaneManager.switchTo("QRScan");
    }

    @FXML
    private void gotoAddData() {
        ScaneManager.switchTo("AdminDash");
    }

    @FXML
    private AnchorPane bottomRightPane;
    @FXML
     private  Button AdddataButton;

    @FXML
    private  Button DeldataButton;

    @FXML
    private  Button AccessdataButton;

    @FXML
    private  Button AttendenceButton;
@FXML
private  AnchorPane defaultpane;


//    @FXML
//    private void gotoDelData() {
//        try {
//            GridPane newPane = FXMLLoader.load(getClass().getResource("/com/example/demo/DeletData.fxml")); // Change to GridPane
//            bottomRightPane.getChildren().setAll(newPane);
//        } catch (IOException e) {
//            e.printStackTrace();
//            ;
//        }
      @FXML
       protected  void gotoDelData(ActionEvent event)throws IOException{
        loadRightPaneContent("DeletData.fxml");
        }
    @FXML
    protected  void gotoAddData(ActionEvent event)throws IOException{
        loadRightPaneContent("AddData.fxml");
    }

    @FXML
    protected  void gotoAttendence(ActionEvent event)throws IOException{
        loadRightPaneContent("MarkManually.fxml");
    }
    @FXML
    protected  void gotoAccessData(ActionEvent event)throws IOException{
        loadRightPaneContent("AccesData.fxml");
    }


        private void loadRightPaneContent(String fxmlFile)throws IOException{
    FXMLLoader loader =new FXMLLoader(getClass().getResource(fxmlFile));
    AnchorPane newContent=loader.load();

    bottomRightPane.getChildren().clear();;
    bottomRightPane.getChildren().add(newContent);
        }

    }


//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import javafx.scene.Node;
//import javafx.fxml.FXMLLoader;
//import java.io.IOException;
//
//public class HelloController extends Application {
//
//    private BorderPane rootLayout;
//    private Pane contentArea; // this is the dynamic area
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        rootLayout = new BorderPane();
//
//        // Create buttons
//        Button AddData = new Button("Screen 1");
//        Button DeleteData = new Button("Screen 2");
//        Button btn3 = new Button("Screen 3");
//        Button btn4 = new Button("Screen 4");
//
//        // VBox for the buttons on the left
//        VBox sideMenu = new VBox(10, AddData, DeleteData, btn3, btn4);
//        sideMenu.setPrefWidth(150);
//        rootLayout.setLeft(sideMenu);
//
//        // Content area (bottom right)
//        contentArea = new StackPane(); // or AnchorPane, Pane, etc.
//        rootLayout.setCenter(contentArea); // Place it in center (bottom right area)
//
//        // Button actions
//        AddData.setOnAction(e -> loadScreen("AddData.fxml"));
//        DeleteData.setOnAction(e -> loadScreen("Screen2.fxml"));
//        btn3.setOnAction(e -> loadScreen("Screen3.fxml"));
//        btn4.setOnAction(e -> loadScreen("Screen4.fxml"));
//
//        // Set default screen
//        loadScreen("DefaultScreen.fxml");
//
//        Scene scene = new Scene(rootLayout, 800, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("JavaFX Dynamic Screens");
//        primaryStage.show();
//    }
//
//    private void loadScreen(String fxmlFile) {
//        try {
//            Node screen = FXMLLoader.load(getClass().getResource(fxmlFile));
//            contentArea.getChildren().setAll(screen);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
