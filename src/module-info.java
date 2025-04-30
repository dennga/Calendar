module Terminator { // Behalte hier "Terminator", da das der Name deines Moduls ist
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    // Füge hier weitere benötigte JavaFX-Module hinzu, z.B.
    // requires javafx.web;
    // requires javafx.media;
    // requires javafx.swing;

    opens de.lokal.kalender to javafx.fxml, javafx.graphics;
    exports de.lokal.kalender; // Wenn andere Module auf deine Klassen zugreifen sollen
}