package com.example.demo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ScaneManager {
    private static Stage primaryStage;
    private static final HashMap<String,Scene> scenes=new HashMap<>();

    //Main stage called once

    public static  void setStage(Stage stage){
        primaryStage =stage;

    }
    public static void loadScene(String name,String fxmlFile) throws IOException{
        FXMLLoader loader=new FXMLLoader(ScaneManager.class.getResource(fxmlFile));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        scenes.put(name,scene);

    }
    public static void switchTo(String name){
        if(scenes.containsKey(name)){
            primaryStage.setScene(scenes.get(name));
            primaryStage.show();
        }else{
            System.out.println("Scenes not found:"+name);
        }
    }

}
