package de.lokal.kalender; // legt fest das Termin.java zum selben Package (ordner) gehört

import java.io.Serializable; // importiert das Interface Serializable zum Speichern von Objekten
import java.time.LocalDate; // importiert die Klasse LocalDate für den Umgang mit Datumsangaben

public class Termin implements Serializable { // die öffentliche Klasse namens Termin die Serializable implementiert
	
	private static final long serialVersionUID = 1L;
	private LocalDate datum; // ein Datentyp (LocalDate) für das Datum
	private String beschreibung; // ein Container (String) für die Beschreibung
	private String erinnerung; // ein Container (String) für die Erinnerung
	
	public Termin(LocalDate datum, String beschreibung, String erinnerung) { 
		this.datum = datum;
		this.beschreibung = beschreibung;
		this.erinnerung = erinnerung;
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
	
	public String getErinnerung() { // Getter-Methode für das Erinnerungs-Attribut, gibt den Wert der Erinnerung zurück
		return erinnerung;
	}
	
	public void setErinnerung(String erinnerung) { // Setter-Methode für das 'Erinnerungs'-Attribut, erlaubt das Ändern der Erinnerung
		this.erinnerung = erinnerung;
	}
	
	@Override
    public String toString() { 
        return "Termin{" + 
               "datum=" + datum + 
               ", beschreibung='" + beschreibung + '\'' + 
               ", erinnerung='" + erinnerung + '\'' + 
               '}'; 
	
	}
	
}
