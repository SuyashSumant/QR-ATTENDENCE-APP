package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
////        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("scanQR.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 200);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
        ScaneManager.setStage(primaryStage);
        ScaneManager.loadScene("welcome","welcome.fxml");
        ScaneManager.loadScene("QRScan","scanQR.fxml");
        ScaneManager.loadScene("AdminDash","hello-view.fxml");
        ScaneManager.loadScene("IdGen","IDgenerated.fxml");
        ScaneManager.loadScene("DeleteDataSuccess","DataDeleted.fxml");

        ScaneManager.switchTo("welcome");


    }
    public  static void main(String [] args){
        launch(args);
    }
}
