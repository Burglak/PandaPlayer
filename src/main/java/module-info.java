module pandaplayer.pandaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.media;
    requires javafx.swing;


    opens pandaplayer.pandaplayer to javafx.fxml;
    exports pandaplayer.pandaplayer;
}