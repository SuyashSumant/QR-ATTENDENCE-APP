package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class DownloadAttendenceController {
    @FXML

    private ComboBox<String> dropdown;

    @FXML
    public void initialize() {
        // Populate ComboBox with months
        dropdown.getItems().addAll("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
    }

    @FXML
    private void handleSelection() {
        System.out.println("Selected month: " + dropdown.getValue());
        dropdown.setValue("January");
    }

    @FXML
    private void handleDownload() {
        System.out.println("Downloading attendance for: " + dropdown.getValue());
    }


}
