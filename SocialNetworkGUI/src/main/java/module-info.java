module com.example.socialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnetworkgui to javafx.fxml;
    exports com.example.socialnetworkgui;
    exports com.example.socialnetworkgui.controller;
    opens com.example.socialnetworkgui.controller to javafx.fxml;
}