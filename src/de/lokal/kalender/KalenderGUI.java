package de.lokal.kalender; // Sagt, dass diese Datei zum "de.lokal.kalender"-Ordner gehört

import javafx.application.Application; // Importiert die Basisklasse für JavaFX-Anwendungen
import javafx.stage.Stage; // Importiert die Stage-Klasse, die das Hauptfenster der Anwendung repräsentiert

public class KalenderGUI extends Application { // Definiert die Hauptklasse unserer GUI-Anwendung und erbt von Application

    private TerminVerwaltung terminVerwaltung; // Eine Variable, um die TerminVerwaltung-Klasse zu nutzen

    // Diese Methode wird einmal aufgerufen, bevor das Fenster angezeigt wird (zum Initialisieren)
    @Override
    public void init() throws Exception {
        terminVerwaltung = new TerminVerwaltung(); // Erstellt ein Objekt der TerminVerwaltung-Klasse
        // Hier laden wir die gespeicherten Termine, wenn die Anwendung startet
        terminVerwaltung.ladeTermine();
    }

    // Diese Methode ist der Startpunkt für die GUI. Hier bauen wir die Benutzeroberfläche auf
    @Override
    public void start(Stage primaryStage) throws Exception {
        // 'primaryStage' ist das Hauptfenster unserer Anwendung
        primaryStage.setTitle("Mein Kalender"); // Setzt den Titel des Fensters

        // *** Hier kommt der Code zum Aufbau der Kalenderansicht ***
        // Im Moment zeigen wir nur ein leeres Fenster

        primaryStage.show(); // Macht das Fenster sichtbar
    }

    // Diese Methode wird aufgerufen, wenn die Anwendung beendet wird (zum Aufräumen oder Speichern)
    @Override
    public void stop() throws Exception {
        // Hier speichern wir die aktuellen Termine, wenn die Anwendung geschlossen wird
        terminVerwaltung.speichereTermine(terminVerwaltung.getTermine());
    }

    // Die 'main'-Methode ist der Einstiegspunkt für die gesamte Java-Anwendung
    public static void main(String[] args) {
        launch(args); // Startet die JavaFX-Anwendung und ruft die 'init()' und 'start()' Methoden auf
    }
}