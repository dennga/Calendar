package de.lokal.kalender; // Sagt, dass diese Datei zum "de.lokal.kalender"-Ordner gehört

// Importiert Klassen für die Ein- und Ausgabe, die wir zum Speichern und Laden brauchen
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList; // Importiert die ArrayList-Klasse, eine flexible Liste
import java.util.List; // Importiert das List-Interface, das ArrayList implementiert

public class TerminVerwaltung { // Die Klasse, die sich um die Verwaltung unserer Termine kümmert

    private static final String DATEIPFAD = "kalender_daten.ser"; // Ein fester Name für die Datei, in der wir die Termine speichern, eine nach der initialisierung unveränderbare Konstante
    private List<Termin> termine = new ArrayList<>(); // Eine Liste, in der wir unsere Termin-Objekte im Arbeitsspeicher halten

    // Diese Methode versucht, die gespeicherten Termine aus der Datei zu laden
    public List<Termin> ladeTermine() {
        try (FileInputStream fileIn = new FileInputStream(DATEIPFAD); // Öffnet die Datei zum Lesen
             ObjectInputStream in = new ObjectInputStream(fileIn)) { // Macht es möglich, Java-Objekte aus der Datei zu lesen
            termine = (List<Termin>) in.readObject(); // Liest die Liste der Termine aus der Datei und speichert sie in unserer 'termine'-Liste
            System.out.println("Termine wurden geladen."); // Gibt eine Nachricht aus, dass das Laden erfolgreich war
        } catch (FileNotFoundException e) { // Falls die Datei nicht gefunden wird...
            System.out.println("Keine gespeicherten Termine gefunden. Starte mit einer leeren Liste."); // ...geben wir eine Meldung aus
            termine = new ArrayList<>(); // ...und erstellen eine neue, leere Liste
        } catch (IOException e) { // Falls es einen anderen Fehler beim Lesen der Datei gibt...
            System.err.println("Fehler beim Laden der Termine: " + e.getMessage()); // ...geben wir eine Fehlermeldung aus
            termine = new ArrayList<>(); // ...und stellen sicher, dass wir mit einer leeren Liste arbeiten
        } catch (ClassNotFoundException e) { // Falls die 'Termin'-Klasse nicht gefunden wird (was sehr unwahrscheinlich ist, wenn unser Code korrekt ist)...
            System.err.println("Klasse Termin nicht gefunden beim Laden: " + e.getMessage()); // ...geben wir eine Fehlermeldung aus
            termine = new ArrayList<>(); // ...und arbeiten mit einer leeren Liste
        }
        return termine; // Gibt die Liste der geladenen (oder leeren) Termine zurück
    }

    // Diese Methode nimmt eine Liste von Terminen und speichert sie in der Datei
    public void speichereTermine(List<Termin> termine) {
        try (FileOutputStream fileOut = new FileOutputStream(DATEIPFAD); // Öffnet die Datei zum Schreiben
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) { // Macht es möglich, Java-Objekte in die Datei zu schreiben
            out.writeObject(termine); // Schreibt die gesamte Liste der Termin-Objekte in die Datei
            System.out.println("Termine wurden gespeichert."); // Gibt eine Nachricht aus, dass das Speichern erfolgreich war
        } catch (IOException e) { // Falls es einen Fehler beim Schreiben in die Datei gibt...
            System.err.println("Fehler beim Speichern der Termine: " + e.getMessage()); // ...geben wir eine Fehlermeldung aus
        }
    }

    // Diese Methode gibt die aktuelle Liste der Termine zurück, die im Speicher ist
    public List<Termin> getTermine() {
        return termine;
    }

    // Diese Methode fügt einen neuen Termin zur Liste hinzu
    public void addTermin(Termin termin) {
        this.termine.add(termin);
    }

    // Diese Methode sucht einen alten Termin in der Liste und ersetzt ihn durch einen neuen
    public void updateTermin(Termin alterTermin, Termin neuerTermin) {
        int index = termine.indexOf(alterTermin); // Findet die Position des alten Termins in der Liste
        if (index != -1) { // Wenn der alte Termin gefunden wurde...
            this.termine.set(index, neuerTermin); // ...ersetzen wir ihn durch den neuen Termin an dieser Position
        }
    }

    // Diese Methode entfernt einen bestimmten Termin aus der Liste
    public void entferneTermin(Termin termin) {
        this.termine.remove(termin);

    }
}