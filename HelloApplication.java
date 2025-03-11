package sample.qr_attendence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader1 = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("addData.fxml"));
        FXMLLoader fxmlLoader3 = new FXMLLoader(HelloApplication.class.getResource("accessData.fxml"));
        FXMLLoader fxmlLoader4 = new FXMLLoader(HelloApplication.class.getResource("attendence.fxml"));
        FXMLLoader fxmlLoader5 = new FXMLLoader(HelloApplication.class.getResource("cameraModule.fxml"));
        FXMLLoader fxmlLoader7 = new FXMLLoader(HelloApplication.class.getResource("deleteData.fxml"));
        FXMLLoader fxmlLoader6 = new FXMLLoader(HelloApplication.class.getResource("deleteDataDetails.fxml"));

//        Scene scene = new Scene(fxmlLoader.load(), 600, 461);
        Scene scene = new Scene(fxmlLoader2.load(), 600, 461);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}