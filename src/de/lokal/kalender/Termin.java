package de.lokal.kalender; // legt fest das Termin.java zum selben Package (ordner) gehört

import java.io.Serializable; // importiert das Interface Serializable zum Speichern von Objekten
import java.time.LocalDate; // importiert die Klasse LocalDate für den Umgang mit Datumsangaben
import java.time.LocalTime;

public class Termin implements Serializable { // die öffentliche Klasse namens Termin die Serializable implementiert

    private static final long serialVersionUID = 1L;
    private LocalDate datum; // ein Datentyp (LocalDate) für das Datum
    private String bezeichnung; // nur die Bezeichnung/name des Termins
    private String beschreibung; // ein Container (String) für die Beschreibung
    private String erinnerung; // ein Container (String) für die Erinnerung
    private String kategorie; // <- Hier fehlt die Deklaration der 'kategorie'-Variable!
    private LocalTime startTime; // Feld für die Startzeit
    private LocalTime endTime;   // Feld für die Endzeit

    public Termin(LocalDate datum, String bezeichnung, String beschreibung, String erinnerung, String kategorie, LocalTime startTime, LocalTime endTime) {
        this.datum = datum;
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
        this.erinnerung = erinnerung;
        this.kategorie = kategorie; // Initialisierung im Konstruktor
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getKategorie() {
        return kategorie;
    }

    public LocalDate getDatum() { // Getter-Methode für das 'datum'-Attribut, gibt den Wert des Datums zurück
        return datum;
    }

    public void setDatum(LocalDate datum) { // Setter-Methode für das 'datum'-Attribut, erlaubt das Ändern des Datums
        this.datum = datum;
    }

    public String getBeschreibung() { // Getter-Methode für das 'Beschreibungs'-Attribut, gibt den Wert der Beschreibung zurück
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) { // Setter-Methode für das 'Beschreibung'-Attribut, erlaubt das Ändern der Beschreibung
        this.beschreibung = beschreibung;
    }

    public String getBezeichnung() { // Getter-Methode für das 'Beschreibungs'-Attribut, gibt den Wert der Beschreibung zurück
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) { // Setter-Methode für das 'Beschreibung'-Attribut, erlaubt das Ändern der Beschreibung
        this.bezeichnung = bezeichnung;
    }

    public String getErinnerung() { // Getter-Methode für das Erinnerungs-Attribut, gibt den Wert der Erinnerung zurück
        return erinnerung;
    }

    public void setErinnerung(String erinnerung) { // Setter-Methode für das 'Erinnerungs'-Attribut, erlaubt das Ändern der Erinnerung
        this.erinnerung = erinnerung;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    //wie unser Termin aussieht, wenn wir ihn als Text ausgeben wollen (z.B. beim Anzeigen auf dem Bildschirm)
    @Override // "Überschreibe" die Standard-Art, wie man ein Objekt als Text darstellt
    public String toString() {
        return "Termin{" +
                   "datum=" + datum +
                   ", bezeichnung='" + bezeichnung + '\'' +
                   ", beschreibung='" + beschreibung + '\'' +
                   ", erinnerung='" + erinnerung + '\'' +
                   ", kategorie='" + kategorie + '\'' + // Kategorie zur toString-Methode hinzugefügt
                   ", startTime=" + startTime +
                   ", endTime=" + endTime +
                   '}';
    }
}