package com.example.demo;
import javafx.fxml.FXML;

public class loginController {
    @FXML
    private void onLogin() {
        ScaneManager.switchTo("AdminDash");
    }
    @FXML
    private void onCancel() {
        System.exit(0);
    }

}


