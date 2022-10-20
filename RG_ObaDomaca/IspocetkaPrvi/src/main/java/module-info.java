module com.example.prvi_domaci {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prvi_domaci to javafx.fxml;
    exports com.example.prvi_domaci;
}