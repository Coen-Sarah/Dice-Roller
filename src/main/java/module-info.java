module com.example.diceroller {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.fxyz3d.core;
    requires org.fxyz3d.importers;

    opens com.example.diceroller to javafx.fxml;
    exports com.example.diceroller;
}