module com.example.parcial3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens com.example.parcial3 to javafx.fxml;
    exports com.example.parcial3;
    exports Controllers;
    opens Controllers to javafx.fxml;
}