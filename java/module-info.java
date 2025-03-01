module com.tonevellah.a_3_header {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires opencv;


    opens com.tonevellah.a_3_header to javafx.fxml;
    exports com.tonevellah.a_3_header;
}