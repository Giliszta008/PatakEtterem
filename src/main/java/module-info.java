module hu.petrik.pataketterem {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.pataketterem to javafx.fxml;
    exports hu.petrik.pataketterem;
}