package com.example.demo;

import javafx.fxml.FXML;

public class AttendenceMarkedController {
    @FXML
    private  void onAttendenceSuccess(){
        ScaneManager.switchTo("AdminDash");
    }
}
