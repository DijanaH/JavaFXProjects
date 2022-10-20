module com.example.rgdomaci2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rgdomaci2 to javafx.fxml;
    exports com.example.rgdomaci2;
}