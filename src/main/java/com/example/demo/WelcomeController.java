package com.example.demo;

import javafx.fxml.FXML;

public class WelcomeController {
   @FXML
   private  void gotoQRScan(){
        ScaneManager.switchTo("QRScan");
    }

}
