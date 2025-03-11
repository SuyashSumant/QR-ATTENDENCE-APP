module sample.qr_attendence {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample.qr_attendence to javafx.fxml;
    exports sample.qr_attendence;
}