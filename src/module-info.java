module Terminator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens de.lokal.kalender to javafx.fxml, javafx.graphics; // Diese Zeile bleibt
    opens de.lokal.kalender.assets; // <-- DIESE NEUE ZEILE HINZUFÜGEN!

    exports de.lokal.kalender;
}