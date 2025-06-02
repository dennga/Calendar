package de.lokal.kalender; // Definiert den Paketnamen für diese Datei

import java.time.DayOfWeek; // Importiert die Klasse für Wochentage
import java.time.LocalDate; // Importiert die Klasse für Datumsangaben
import java.time.LocalTime; // Importiert die Klasse für Zeitangaben
import java.time.format.DateTimeFormatter; // Importiert die Klasse zum Formatieren von Datum und Zeit
import java.time.temporal.WeekFields; // Importiert die Klasse für wochenspezifische Felder
import java.util.List; // Importiert die Klasse für Listen von Objekten
import java.util.Locale; // Importiert die Klasse für sprach- und regionsspezifische Einstellungen
import javafx.application.Application; // Importiert die Basisklasse für JavaFX-Anwendungen
import javafx.geometry.Insets; // Importiert die Klasse für Innenabstände von UI-Elementen
import javafx.scene.Scene; // Importiert die Klasse für die Szene (Container für UI-Inhalte)
import javafx.scene.control.Button; // Importiert die Klasse für Schaltflächen
import javafx.scene.control.ComboBox; // Importiert die Klasse für Dropdown-Listen
import javafx.scene.control.DatePicker; // Importiert die Klasse für Datumsauswahlfelder
import javafx.scene.control.Label; // Importiert die Klasse für Textanzeigen
import javafx.scene.control.ScrollPane; // Importiert die Klasse für scrollbare Bereiche
import javafx.scene.control.TextArea; // Importiert die Klasse für mehrzeilige Textfelder
import javafx.scene.control.TextField; // Importiert die Klasse für einzeilige Textfelder
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane; // Importiert die Klasse für ein Layout mit Bereichen (oben, unten, links, rechts, mitte)
import javafx.scene.layout.GridPane; // Importiert die Klasse für ein Layout in Gitterform
import javafx.scene.layout.HBox; // Importiert die Klasse für ein horizontales Layout
import javafx.scene.layout.VBox; // Importiert die Klasse für ein vertikales Layout
import javafx.stage.Modality; // Importiert die Klasse für die Modalität von Fenstern
import javafx.stage.Stage; // Importiert die Klasse für das Hauptfenster der Anwendung



public class KalenderGUI extends Application { // Definiert die Hauptklasse der Anwendung, die von Application erbt


	private TerminVerwaltung terminVerwaltung; // Deklariert eine Variable zur Verwaltung der Termine
	private LocalDate aktuellesDatum = LocalDate.now(); // Deklariert und initialisiert das aktuell angezeigte Datum mit
														// dem heutigen Datum
	private LocalDate heutigesDatum = LocalDate.now(); // Deklariert und initialisiert das heutige Datum
	private BorderPane root; // Deklariert das Hauptlayout der Anwendung
	private VBox centerCalendarPane; // Deklariert einen Bereich für die Wochenansicht
	private VBox menuPane; // Deklariert einen Bereich für das Menü
	private boolean isMenuVisible = false; // Deklariert eine Variable, die angibt, ob das Menü sichtbar ist

	private enum KategorieFarben { // Definiert eine Aufzählung (Enum) für Terminkategorien und deren Farben


		Beruflich("blue"), // Definiert die Kategorie "Beruflich" mit der Farbe "blue"
		Privat("green"), // Definiert die Kategorie "Privat" mit der Farbe "green"
		FreieTage("red"), // Definiert die Kategorie "FreieTage" mit der Farbe "red"
		Geburtstage("orange"), // Definiert die Kategorie "Geburtstage" mit der Farbe "orange"
		Wichtig("purple"); // Definiert die Kategorie "Wichtig" mit der Farbe "purple"

		private String farbe; // Deklariert eine Variable zur Speicherung der Farbe einer Kategorie

		KategorieFarben(String farbe) { // Konstruktor für die KategorieFarben-Enum, nimmt eine Farbe entgegen
			this.farbe = farbe; // Weist die übergebene Farbe der Instanzvariable zu
		}
		public String getFarbe() { // Definiert eine Methode, um die Farbe einer Kategorie abzurufen
			return farbe; // Gibt die Farbe der Kategorie zurück
		}
	}


	@Override // Überschreibt eine Methode der Superklasse
	public void init() throws Exception { // Initialisierung der Anwendung
		terminVerwaltung = new TerminVerwaltung(); // Erstellt eine neue Instanz der TerminVerwaltung
		terminVerwaltung.ladeTermine(); // Lädt die gespeicherten Termine
	}



	@Override // Überschreibt eine Methode der Superklasse
	public void start(Stage primaryStage) throws Exception { // Start der JavaFX-Anwendung
		primaryStage.setTitle("Terminator"); // Setzt den Titel des Hauptfensters
		root = new BorderPane(); // Erstellt ein neues BorderPane als Hauptlayout
		root.setPadding(new Insets(10)); // Fügt einen Innenabstand von 10 Pixeln zum Hauptlayout hinzu
		root.setStyle("-fx-background-color: #b7b7a4;"); // Setzt die Hintergrundfarbe des Hauptlayouts

		VBox leftPane = createLeftPane(); // Erstellt den linken Bereich und speichert ihn in einer Variable
		root.setLeft(leftPane); // Setzt den linken Bereich im Hauptlayout
		centerCalendarPane = createCenterPane(); // Erstellt den zentralen Bereich (Wochenansicht) und speichert ihn
		root.setCenter(centerCalendarPane); // Setzt den zentralen Bereich im Hauptlayout
		menuPane = createMenu(); // Erstellt das Menü und speichert es
		Label navigationPlatzhalter = new Label("Navigation"); // Erstellt ein Label als Platzhalter für die Navigation im oberen Bereich
		root.setTop(navigationPlatzhalter); // Setzt das Navigations-Label im oberen Bereich des Hauptlayouts
		Scene scene = new Scene(root, 800, 600); // Erstellt eine neue Szene mit dem Hauptlayout und den Abmessungen  800x600 Pixel
		primaryStage.setScene(scene); // Setzt die erstellte Szene im Hauptfenster
		primaryStage.show(); // Zeigt das Hauptfenster der Anwendung an

		try {
            // Lade die Icon-Dateien aus den Ressourcen

            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/de/lokal/kalender/assets/kalender16x16.png")));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/de/lokal/kalender/assets/kalender24x24.png")));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/de/lokal/kalender/assets/kalender32x32.png")));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/de/lokal/kalender/assets/kalender64x64.png")));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/de/lokal/kalender/assets/kalender128x128.png")));

        } catch (Exception e) {
            // Eine einfache Fehlerbehandlung, falls ein Icon nicht gefunden wird.
            // Dies ist nützlich für das Debugging, falls Pfade falsch sind.
            System.err.println("Fehler beim Laden eines oder mehrerer Icons: " + e.getMessage());
            // Der Kalender sollte trotzdem starten, aber ohne Icon.
        }


        primaryStage.setScene(scene);
        primaryStage.show();

	}



	private VBox createLeftPane() { // Erstellt den linken Bereich der Anwendung
		VBox leftPane = new VBox(10); // Erstellt ein vertikales Layout mit einem Abstand von 10 Pixeln zwischen den Elementen
		HBox monthNav = new HBox(5); // Erstellt ein horizontales Layout für die Monatsnavigation mit einem Abstand von 5 Pixeln
		Button prevMonthButton = new Button("<"); // Erstellt einen Button für den vorherigen Monat
	    prevMonthButton.setOnAction(event -> navigateMonth(-1)); // Gehe zum vorherigen Monat
		Button todayMonthButton = new Button("Heute"); // Erstellt einen Button für den heutigen Monat
	    todayMonthButton.setOnAction(event -> navigateMonth(0));  // Gehe zum aktuellen Monat
		Button nextMonthButton = new Button(">"); // Erstellt einen Button für den nächsten Monat
	    nextMonthButton.setOnAction(event -> navigateMonth(1));  // Gehe zum nächsten Monat
		monthNav.getChildren().addAll(prevMonthButton, todayMonthButton, nextMonthButton); // Fügt die Buttons zur Monatsnavigation hinzu
		leftPane.getChildren().add(monthNav); // Fügt die Monatsnavigation zum linken Bereich hinzu
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()); // Erstellt einen Formatierer für die Anzeige von Monat und jahr
		Label monthYearLabel = new Label(monthYearFormatter.format(aktuellesDatum)); // Erstellt ein Label zur Anzeige des aktuellen Monats und jahres
		monthYearLabel.setId("monthYearLabel"); // Setzt die ID des Labels
		leftPane.getChildren().add(monthYearLabel); // Fügt das Monats-/Jahreslabel zum linken Bereich hinzu
		GridPane monatsAnsichtGrid = new GridPane(); // Erstellt ein Gitterlayout für die Monatsansicht
		monatsAnsichtGrid.setPadding(new Insets(5)); // Fügt einen Innenabstand von 5 Pixeln zum Monatsansicht-Grid  hinzu
		monatsAnsichtGrid.setHgap(5); // Setzt den horizontalen Abstand zwischen den Zellen des Grids auf 5 Pixel
		monatsAnsichtGrid.setVgap(5); // Setzt den vertikalen Abstand zwischen den Zellen des Grids auf 5 Pixel
		updateMonatsAnsicht(monatsAnsichtGrid); // Ruft eine Methode auf, um die Monatsansicht zu aktualisieren
		leftPane.getChildren().add(monatsAnsichtGrid); // Fügt das Monatsansicht-Grid zum linken Bereich hinzu
		VBox legendenBereich = createLegende(); // Erstellt den Bereich für die Legende
		leftPane.getChildren().add(legendenBereich); // Fügt den Legendenbereich zum linken Bereich hinzu
		GridPane jahresUebersichtGrid = new GridPane(); // Erstellt ein Gitterlayout für die Jahresübersicht
		jahresUebersichtGrid.setPadding(new Insets(5)); // Fügt einen Innenabstand von 5 Pixeln zum Jahresübersicht-Grid hinzu
		jahresUebersichtGrid.setHgap(3); // Setzt den horizontalen Abstand im Jahresübersicht-Grid auf 3 Pixel
		jahresUebersichtGrid.setVgap(3); // Setzt den vertikalen Abstand im Jahresübersicht-Grid auf 3 Pixel
		updateJahresUebersicht(jahresUebersichtGrid, aktuellesDatum.getYear()); // Aktualisiert die Jahresübersicht für das aktuelle Jahr
		leftPane.getChildren().add(jahresUebersichtGrid); // Fügt die Jahresübersicht zum linken Bereich hinzu
		Button menuButton = new Button("Menü"); // Erstellt einen Button für das Menü
		menuButton.setOnAction(event -> toggleMenu()); // Setzt eine Aktion für den Menü-Button, die die Sichtbarkeit des Menüs umschaltet
		leftPane.getChildren().add(menuButton); // Fügt den Menü-Button zum linken Bereich hinzu
		Button neuerTerminButton = new Button("Neuer Termin"); // Erstellt einen Button zum Erstellen neuer Termine
		neuerTerminButton.setOnAction(event -> erstelleNeuerTermin()); // Setzt eine Aktion für den "Neuer Termin"-Button, die den Dialog zur Terminerstellung öffnet
		leftPane.getChildren().add(neuerTerminButton); // Fügt den "Neuer Termin"-Button zum linken Bereich hinzu
		return leftPane; // Gibt den erstellten linken Bereich zurück
	}



	private VBox createCenterPane() { // Erstellt den Center-Bereich für die Wochenansicht
		VBox centerPane = new VBox(5); // Erstellt ein vertikales Layout mit einem Abstand von 5 Pixeln zwischen den Elementen
		HBox weekNav = new HBox(5); // Erstellt ein horizontales Layout für die Wochenansicht-Navigation mit einem Abstand von 5 Pixeln
		Button prevWeekButton = new Button("<"); // Erstellt einen Button für die vorherige Woche
	    prevWeekButton.setOnAction(event -> navigateWeek(-1)); // Gehe zur vorherigen Woche
		Button todayWeekButton = new Button("Heute"); // Erstellt einen Button für die aktuelle Woche
	    todayWeekButton.setOnAction(event -> navigateWeek(0));  // Gehe zur aktuellen Woche
		Button nextWeekButton = new Button(">"); // Erstellt einen Button für die nächste Woche
	    nextWeekButton.setOnAction(event -> navigateWeek(1));  // Gehe zur nächsten Woche
		weekNav.getChildren().addAll(prevWeekButton, todayWeekButton, nextWeekButton); // Fügt die Buttons zur Wochenansicht-Navigation hinzu
		centerPane.getChildren().add(weekNav); // Fügt die Wochenansicht-Navigation zum zentralen Bereich hinzu
		Label weekTitleLabel = new Label(); // Erstellt ein Label für den Titel der Wochenansicht
		centerPane.getChildren().add(weekTitleLabel); // Fügt das Wochenansicht-Titel-Label zum zentralen Bereich hinzu
		GridPane wochenAnsichtGrid = new GridPane(); // Erstellt ein Gitterlayout für die Wochenansicht
		wochenAnsichtGrid.setPadding(new Insets(10)); // Fügt einen Innenabstand von 10 Pixeln zum Wochenansicht-Grid
														// hinzu
		wochenAnsichtGrid.setHgap(50); // Setzt den horizontalen Abstand zwischen den Zellen des Grids auf 50 Pixel
		wochenAnsichtGrid.setVgap(2); // Setzt den vertikalen Abstand zwischen den Zellen des Grids auf 2 Pixel
										// (weniger Platz zwischen den Zeilen)
         // **Spaltenconstraints explizit setzen**
		for (int i = 0; i < 8; i++) { // Startet eine Schleife für die 8 Spalten des Wochenansicht-Grids (1 für Stunden, 7 für Tage)
			javafx.scene.layout.ColumnConstraints columnConstraints = new javafx.scene.layout.ColumnConstraints(); // Erstellt Beschränkungen für  eine Spalte
			if (i == 0) { // Überprüft, ob es sich um die erste Spalte (für die Stunden) handelt
				columnConstraints.setPrefWidth(50); // Setzt die bevorzugte Breite der ersten Spalte auf 50 Pixel
			} else { // Für alle anderen Spalten (die Tage)
				columnConstraints.setPrefWidth(100); // Setzt die bevorzugte Breite der Tages-Spalten auf 100 Pixel
														// (kann sich anpassen)
				columnConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS); // Erlaubt den Tages-Spalten, horizontal zu wachsen, wenn das Fenster größer wird
			}
			wochenAnsichtGrid.getColumnConstraints().add(columnConstraints); // Fügt die erstellten Spaltenbeschränkungen zum Wochenansicht-Grid hinzu
		}
		updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel); // Ruft eine Methode auf, um die Wochenansicht mit Daten zu füllen und zu aktualisieren
		ScrollPane scrollPane = new ScrollPane(wochenAnsichtGrid); // Erstellt einen scrollbaren Bereich für das Wochenansicht-Grid, falls der Inhalt zu groß wird
		scrollPane.setFitToWidth(true); // Sorgt dafür, dass der ScrollPane sich horizontal an die Breite des Inhalts anpasst und keine horizontale Scrollleiste unnötig anzeigt
		centerPane.getChildren().add(scrollPane); // Fügt den ScrollPane (mit dem Wochenansicht-Grid) zum zentralen Bereich hinzu
		return centerPane; // Gibt den erstellten zentralen Bereich zurück
	}



	// Erstellt die Legende für die Terminkategorien
	private VBox createLegende() {
		VBox legendenBereich = new VBox(5); // Erstellt ein vertikales Layout mit Abstand 5 für die Legende
		Label legendenUeberschrift = new Label("Kategorien:"); // Erstellt ein Label mit der Überschrift "Kategorien:"
		legendenBereich.getChildren().add(legendenUeberschrift); // Fügt die Überschrift zum Legendenbereich hinzu
		for (KategorieFarben kategorie : KategorieFarben.values()) { // Schleife durch alle Werte der KategorieFarben
																		// Enum
			Label farbQuadrat = new Label("■"); // Erstellt ein Label, das als farbiges Quadrat dargestellt wird
			farbQuadrat.setStyle("-fx-text-fill: " + kategorie.getFarbe() + "; -fx-font-size: 16px;"); // Setzt die Farbe und Schriftgröße des Quadrats
			Label kategorieName = new Label(kategorie.name()); // Erstellt ein Label mit dem Namen der Kategorie
			HBox legendenEintrag = new HBox(5, farbQuadrat, kategorieName); // Erstellt ein horizontales Layout für
																			// einen Legendeneintrag
			legendenBereich.getChildren().add(legendenEintrag); // Fügt den Eintrag zum Legendenbereich hinzu
		}
		return legendenBereich; // Gibt den erstellten Legendenbereich zurück
	}



	// Erstellt das Menü
	private VBox createMenu() {
		VBox menu = new VBox(10); // Erstellt ein vertikales Layout mit Abstand 10 für das Menü
		menu.setPadding(new Insets(20)); // Fügt einen Innenabstand von 20 Pixeln zum Menü hinzu
		Label titelLabel = new Label("Menü"); // Erstellt ein Label mit dem Titel "Menü"
		titelLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;"); // Setzt Schriftgröße und -gewicht des
																			// Titels
		menu.getChildren().add(titelLabel); // Fügt den Titel zum Menü hinzu
		Button sucheButton = new Button("Suche (zukünftig)"); // Erstellt einen Button für die Suche (zukünftig)
		Button einstellungenButton = new Button("Einstellungen (zukünftig)"); // Erstellt einen Button für die
																				// Einstellungen (zukünftig)
		Button kontoButton = new Button("Konto (zukünftig)"); // Erstellt einen Button für das Konto (zukünftig)
		Button naechsterTerminButton = new Button("Nächster Termin (zukünftig)"); // Erstellt einen Button für den
																					// nächsten Termin (zukünftig)
		Button zurueckButton = new Button("Zurück zum Kalender"); // Erstellt einen Button, um zum Kalender
																	// zurückzukehren
		zurueckButton.setOnAction(event -> toggleMenu()); // Setzt eine Aktion für den Zurück-Button, um das Menü
															// umzuschalten
		menu.getChildren().addAll(sucheButton, einstellungenButton, kontoButton, naechsterTerminButton, zurueckButton); // Fügt alle  Buttons zum Menü hinzu
		return menu; // Gibt das erstellte Menü zurück
	}



	// Zeigt oder verbirgt das Menü
	private void toggleMenu() {
		isMenuVisible = !isMenuVisible; // Invertiert den Zustand der Menüsichtbarkeit
		if (isMenuVisible) { // Wenn das Menü sichtbar sein soll
			root.setCenter(menuPane); // Setzt das Menü im zentralen Bereich des Hauptlayouts
		} else { // Wenn das Menü nicht sichtbar sein soll
			root.setCenter(centerCalendarPane); // Setzt die Kalenderansicht im zentralen Bereich des Hauptlayouts
		}
	}



	// Erstellt den "Neuer Termin" Dialog
	private void erstelleNeuerTermin() {
		Stage dialogStage = new Stage(); // Erstellt ein neues Fenster (Stage) für den Dialog
		dialogStage.initModality(Modality.APPLICATION_MODAL); // Macht den Dialog modal, d.h., er blockiert
																// Interaktionen mit anderen Fenstern
		dialogStage.setTitle("Neuen Termin erstellen"); // Setzt den Titel des Dialogfensters
		GridPane grid = new GridPane(); // Erstellt ein Gitterlayout für die Elemente im Dialog
		grid.setPadding(new Insets(10)); // Fügt einen Innenabstand von 10 Pixeln zum Gitter hinzu
		grid.setHgap(5); // Setzt den horizontalen Abstand zwischen den Zellen des Gitters auf 5 Pixel
		grid.setVgap(5); // Setzt den vertikalen Abstand zwischen den Zellen des Gitters auf 5 Pixel
		Label datumLabel = new Label("Datum:"); // Erstellt ein Label für das Datum
		DatePicker datumPicker = new DatePicker(aktuellesDatum); // Erstellt ein Datumsauswahlfeld, initialisiert mit
																	// dem aktuellen Datum
		Label startTimeLabel = new Label("Startzeit:"); // Erstellt ein Label für die Startzeit
		ComboBox<LocalTime> startTimeCombo = new ComboBox<>(); // Erstellt eine Dropdown-Liste für die Startzeit
		for (int i = 0; i < 24; i++) { // Schleife für die Stunden (0-23)
			for (int j = 0; j < 60; j += 30) { // Schleife für die Minuten (0, 30)
				startTimeCombo.getItems().add(LocalTime.of(i, j)); // Fügt die erstellte Uhrzeit zur Dropdown-Liste
																	// hinzu
			}
		}
		startTimeCombo.setValue(LocalTime.now().withMinute(0).withSecond(0).withNano(0)); // Standard auf aktuelle volle
																							// Stunde
		Label endTimeLabel = new Label("Endzeit:"); // Erstellt ein Label für die Endzeit
		ComboBox<LocalTime> endTimeCombo = new ComboBox<>(); // Erstellt eine Dropdown-Liste für die Endzeit
		for (int i = 0; i < 24; i++) { // Schleife für die Stunden (0-23)
			for (int j = 0; j < 60; j += 30) { // Schleife für die Minuten (0, 30)
				endTimeCombo.getItems().add(LocalTime.of(i, j)); // Fügt die erstellte Uhrzeit zur Dropdown-Liste hinzu
			}
		}
		endTimeCombo.setValue(LocalTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0)); // Standard auf eine Stunde später
		Label beschreibungLabel = new Label("Beschreibung:"); // Erstellt ein Label für die Beschreibung
		TextArea beschreibungText = new TextArea(); // Erstellt ein mehrzeiliges Textfeld für die Beschreibung
		beschreibungText.setPrefRowCount(3); // Setzt die bevorzugte Anzahl der Zeilen für das Textfeld
		Label bezeichnungLabel = new Label("Bezeichnung:"); // Erstellt ein Label für die Bezeichnung
		TextField bezeichnungText = new TextField(); // Erstellt ein einzeiliges Textfeld für die Bezeichnung
		Label erinnerungLabel = new Label("Erinnerung:"); // Erstellt ein Label für die Erinnerung
		TextField erinnerungText = new TextField(); // Erstellt ein einzeiliges Textfeld für die Erinnerung
		Label kategorieLabel = new Label("Kategorie:"); // Erstellt ein Label für die Kategorie
		ComboBox<String> kategorieCombo = new ComboBox<>(); // Erstellt eine Dropdown-Liste für die Kategorie
		for (KategorieFarben kategorie : KategorieFarben.values()) { // Schleife durch alle Werte der KategorieFarben
																		// Enum
			kategorieCombo.getItems().add(kategorie.name()); // Fügt den Namen der Kategorie zur Dropdown-Liste hinzu
		}
		kategorieCombo.setValue(KategorieFarben.Privat.name()); // Standardkategorie
		Button speichernButton = new Button("Speichern"); // Erstellt einen Button zum Speichern des neuen Termins
		speichernButton.setOnAction(e -> { // Setzt eine Aktion für den Speichern-Button
			LocalDate terminDatum = datumPicker.getValue(); // Holt das ausgewählte Datum
			LocalTime terminStartTime = startTimeCombo.getValue(); // Holt die ausgewählte Startzeit
			LocalTime terminEndTime = endTimeCombo.getValue(); // Holt die ausgewählte Endzeit
			String terminBeschreibung = beschreibungText.getText(); // Holt den eingegebenen Beschreibungstext
			String terminBezeichnung = bezeichnungText.getText(); // Holt den eingegebenen Bezeichnungstext
			String terminErinnerung = erinnerungText.getText(); // Holt den eingegebenen Erinnerungstext
			String terminKategorie = kategorieCombo.getValue(); // Holt die ausgewählte Kategorie
			if (terminDatum != null && terminStartTime != null && terminEndTime != null && !terminBezeichnung.isEmpty()) { // Überprüft, ob die Pflichtfelder ausgefüllt sind
				Termin neuerTermin = new Termin(terminDatum, terminBezeichnung, terminBeschreibung, terminErinnerung, terminKategorie, terminStartTime, terminEndTime); // Erstellt ein neues Termin-Objekt
				terminVerwaltung.addTermin(neuerTermin); // Fügt den neuen Termin zur Verwaltung hinzu
				terminVerwaltung.speichereTermine(terminVerwaltung.getTermine()); // Speichert die aktualisierte
																					// Terminliste
				updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid")); // Aktualisiert die
																								// Monatsansicht
				updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel")); // Aktualisiert die Wochenansicht
				updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear()); // Aktualisiert die Jahresübersicht
				dialogStage.close(); // Schließt den Dialog
			} else { // Wenn nicht alle Pflichtfelder ausgefüllt sind
				System.out.println("Bitte füllen Sie mindestens Bezeichnung, Datum und Uhrzeit aus.");
				// Hier könntest du dem Benutzer auch eine visuelle Rückmeldung geben, z.B.
				// durch rote Markierung der Pflichtfelder
			}
		});
		Button abbrechenButton = new Button("Abbrechen"); // Erstellt einen Button zum Abbrechen des Dialogs
		abbrechenButton.setOnAction(e -> dialogStage.close()); // Setzt eine Aktion für den Abbrechen-Button, um den
																// Dialog zu schließen
		grid.add(datumLabel, 0, 0); // Fügt das Datum-Label zum Gitter hinzu (Spalte 0, Zeile 0)
		grid.add(datumPicker, 1, 0); // Fügt den Datumsauswahl zum Gitter hinzu (Spalte 1, Zeile 0)
		grid.add(bezeichnungLabel, 0, 1); // Fügt das Bezeichnungs-Label zum Gitter hinzu (Spalte 0, Zeile 1)
		grid.add(bezeichnungText, 1, 1); // Fügt das Bezeichnungs-Textfeld zum Gitter hinzu (Spalte 1, Zeile 1)
		grid.add(startTimeLabel, 0, 2); // Fügt das Startzeit-Label zum Gitter hinzu (Spalte 0, Zeile 2)
		grid.add(startTimeCombo, 1, 2); // Fügt die Startzeit-Dropdown-Liste zum Gitter hinzu (Spalte 1, Zeile 2)
		grid.add(endTimeLabel, 0, 3); // Fügt das Endzeit-Label zum Gitter hinzu (Spalte 0, Zeile 3)
		grid.add(endTimeCombo, 1, 3); // Fügt die Endzeit-Dropdown-Liste zum Gitter hinzu (Spalte 1, Zeile 3)
		grid.add(beschreibungLabel, 0, 4); // Fügt das Beschreibungs-Label zum Gitter hinzu (Spalte 0, Zeile 4)
		grid.add(beschreibungText, 1, 4); // Fügt das Beschreibungs-Textfeld zum Gitter hinzu (Spalte 1, Zeile 4)
		grid.add(erinnerungLabel, 0, 5); // Fügt das Erinnerungs-Label zum Gitter hinzu (Spalte 0, Zeile 5)
		grid.add(erinnerungText, 1, 5); // Fügt das Erinnerungs-Textfeld zum Gitter hinzu (Spalte 1, Zeile 5)
		grid.add(kategorieLabel, 0, 6); // Fügt das Kategorie-Label zum Gitter hinzu (Spalte 0, Zeile 6)
		grid.add(kategorieCombo, 1, 6); // Fügt die Kategorie-Dropdown-Liste zum Gitter hinzu (Spalte 1, Zeile 6)
		grid.add(speichernButton, 0, 7); // Fügt den Speichern-Button zum Gitter hinzu (Spalte 0, Zeile 7)
		grid.add(abbrechenButton, 1, 7); // Fügt den Abbrechen-Button zum Gitter hinzu (Spalte 1, Zeile 7)
		Scene dialogScene = new Scene(grid); // Erstellt eine neue Szene mit dem Gitterlayout
		dialogStage.setScene(dialogScene); // Setzt die Szene für das Dialogfenster
		dialogStage.showAndWait(); // Zeigt den Dialog und blockiert die Ausführung, bis er geschlossen wird
	}


	// Methode zur Navigation in der Monatsansicht
	private void navigateMonth(int direction) {
	    if (direction < 0) {
	        aktuellesDatum = aktuellesDatum.minusMonths(1);
	    } else if (direction > 0) {
	        aktuellesDatum = aktuellesDatum.plusMonths(1);
	    } else {
	        aktuellesDatum = heutigesDatum;
	    }
	    updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid"));
	    ((Label) root.getLeft().lookup("#monthYearLabel")).setText(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()).format(aktuellesDatum)); // Korrigiertes Format
	    updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel"));
	    updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear());
	}



	// Navigiert in der Wochenansicht vor oder zurück
	private void navigateWeek(int direction) {
		if (direction < 0) { // Wenn die Richtung negativ ist (vorherige Woche)
			aktuellesDatum = aktuellesDatum.minusWeeks(1); // Subtrahiert eine Woche vom aktuellen Datum
		} else if (direction > 0) { // Wenn die Richtung positiv ist (nächste Woche)
			aktuellesDatum = aktuellesDatum.plusWeeks(1); // Addiert eine Woche zum aktuellen Datum
		} else { // Wenn die Richtung null ist (heutige Woche)
			aktuellesDatum = heutigesDatum; // Setzt das aktuelle Datum auf das heutige Datum zurück
		}
		updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel")); // Aktualisiert die Wochenansicht
		updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid")); // Aktualisiert die Monatsansicht
		((Label) root.getLeft().lookup("#monthYearLabel")).setText(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()).format(aktuellesDatum)); // Aktualisiert das Monats-/Jahreslabel
		updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear()); // Aktualisiert die Jahresübersicht
	}



	// Aktualisiert die Jahresübersicht
	private void updateJahresUebersicht(GridPane grid, int jahr) {
		grid.getChildren().clear(); // Entfernt alle vorhandenen Elemente aus dem Gitter
		grid.setId("jahresUebersichtGrid"); // Setzt die ID für das Gitter (für späteres Auffinden)
		LocalDate heute = LocalDate.now(); // Holt das heutige Datum
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault()); // Erstellt einen Formatierer für Monatsnamen (kurz)
		int colCounter = 0; // Zähler für die Spalten im Gitter
		int rowCounter = 0; // Zähler für die Zeilen im Gitter
		for (int monat = 1; monat <= 12; monat++) { // Schleife durch alle 12 Monate
			LocalDate ersterTagDesMonats = LocalDate.of(jahr, monat, 1); // Erstellt das Datum des ersten Tages des
																			// aktuellen Monats
			Label monatsLabel = new Label(monthFormatter.format(ersterTagDesMonats)); // Erstellt ein Label mit dem
																						// Monatsnamen
			monatsLabel.setStyle("-fx-font-size: 10px;"); // Setzt die Schriftgröße des Monatslabels
			if (heute.getYear() == jahr && heute.getMonthValue() == monat) { // Überprüft, ob der aktuelle Monat
																				// angezeigt wird
				monatsLabel.setStyle(monatsLabel.getStyle() + "-fx-font-weight: bold; -fx-text-fill: blue;"); // Hebt den aktuellen Monat hervor
			}
			GridPane.setConstraints(monatsLabel, colCounter, rowCounter); // Setzt die Position des Labels im Gitter
			grid.getChildren().add(monatsLabel); // Fügt das Monatslabel zum Gitter hinzu
			if (colCounter == 3) { // Wenn 4 Monate in einer Zeile sind
				colCounter = 0; // Setze den Spaltenzähler zurück auf 0
				rowCounter++; // Gehe zur nächsten Zeile
			} else { // Ansonsten
				colCounter++; // Gehe zur nächsten Spalte
			}
		}
	}



	// (Platzhalter) Zeigt Termine für einen bestimmten Tag an
	private void zeigeTermineFuerTag(LocalDate tag) {
		// Hier würde die Logik implementiert, um Termine für den ausgewählten Tag
		// anzuzeigen
		System.out.println("Zeige Termine für: " + tag);
	}
	// (Platzhalter) Gibt eine Liste von Terminen für einen bestimmten Tag zurück
	public List<Termin> getTermineFuerTag(LocalDate tag) {
		// Hier würde die Logik implementiert, um Termine aus der TerminVerwaltung zu
		// filtern
		return terminVerwaltung.getTermineFuerTag(tag);
	}



	// Aktualisiert die Monatsansicht
	private void updateMonatsAnsicht(GridPane grid) {
		grid.getChildren().clear(); // Entfernt alle vorhandenen Elemente aus dem Gitter
		grid.setId("monatsAnsichtGrid"); // Setzt die ID für das Gitter (für späteres Auffinden)
		LocalDate ersterTagDesMonats = aktuellesDatum.withDayOfMonth(1); // Ermittelt den ersten Tag des aktuell angezeigten Monats
		LocalDate letzterTagDesMonats = aktuellesDatum.withDayOfMonth(aktuellesDatum.lengthOfMonth()); // Ermittelt den letzten Tag des aktuell angezeigten  Monats
		WeekFields weekFields = WeekFields.of(Locale.getDefault()); // Ermittelt die Definition für die Wochen (abhängig von der Sprache)
		DayOfWeek ersterTagDerWoche = weekFields.getFirstDayOfWeek(); // Ermittelt den ersten Tag der Woche (z.B. Montag oder Sonntag)
		int ersterWochentagDesMonatsWert = ersterTagDesMonats.getDayOfWeek().getValue(); // Ermittelt den numerischen Wert des Wochentags des ersten Tages des Monats (z.B. Montag = 1, Sonntag = 7)
		int ersterTagDerWocheWert = weekFields.getFirstDayOfWeek().getValue(); // Ermittelt den numerischen Wert des ersten Tages der Woche
		int ersterTagIndex = (ersterWochentagDesMonatsWert - ersterTagDerWocheWert + 7) % 7; // Berechnet den Index des ersten Tages des Monats im Grid (um Leerzellen am Anfang zu erzeugen)
		DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EE", Locale.getDefault()); // Erstellt einen Formatierer für die Anzeige des Wochentags (kurz)
		for (int i = 0; i < 7; i++) { // Schleife für die 7 Tage der Woche
			DayOfWeek aktuellerTagDerWoche = ersterTagDerWoche.plus(i); // Ermittelt den aktuellen Tag der Woche in der
																		// Schleife
			LocalDate irgendeinDatumDieserTag = LocalDate.now().with(aktuellerTagDerWoche); // Nimmt irgendein Datum und setzt es auf den aktuellen Tag der Woche (für die Formatierung)
			Label dayNameLabel = new Label(dayOfWeekFormatter.format(irgendeinDatumDieserTag)); // Erstellt ein Label mit dem Namen des  Wochentags
			GridPane.setConstraints(dayNameLabel, i, 0); // Setzt die Position des Wochentags-Labels im Grid (erste Zeile)
			grid.getChildren().add(dayNameLabel); // Fügt das Wochentags-Label zum Grid hinzu
		}
		int tagCounter = 1; // Zähler für die Tage im Monat
		for (int row = 1; row < 7; row++) { // Schleife für die Zeilen im Grid (beginnend ab der zweiten Zeile für die
											// Tage)
			for (int col = 0; col < 7; col++) { // Schleife für die Spalten im Grid (die Tage der Woche)
				if (row == 1 && col < ersterTagIndex) { // Wenn es die erste Woche ist und der Tag vor dem ersten Tag
														// des Monats liegt
					continue; // Überspringe diese Zelle (erzeugt Leerzellen am Monatsanfang)
				}
				LocalDate aktuellerTagImMonat = ersterTagDesMonats.plusDays(tagCounter - 1); // Berechnet das aktuelle Datum im Monat
				if (tagCounter <= letzterTagDesMonats.getDayOfMonth()) { // Wenn der aktuelle Tag noch im Monat liegt
					Label tagLabel = new Label(String.valueOf(tagCounter)); // Erstellt ein Label mit der Tagesnummer
					GridPane.setConstraints(tagLabel, col, row); // Setzt die Position des Tageslabels im Grid
					grid.getChildren().add(tagLabel); // Fügt das Tageslabel zum Grid hinzu
					if (aktuellerTagImMonat.isEqual(heutigesDatum)) { // Wenn der aktuelle Tag der heutige Tag ist
						tagLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue; -fx-cursor: hand;"); // Hebt den heutigen Tag hervor und setzt den Cursor auf "Hand"
					} else { // Wenn der aktuelle Tag nicht der heutige Tag ist
						tagLabel.setStyle("-fx-cursor: hand;"); // Setzt den Cursor auf "Hand"
					}
					tagLabel.setOnMouseClicked(event -> { // Setzt eine Aktion beim Klicken auf einen Tag
						zeigeTermineFuerTag(aktuellerTagImMonat); // Zeigt die Termine für den angeklickten Tag an
																	// (Platzhalterfunktion)
					});
					List<Termin> termineAmTag = terminVerwaltung.getTermineFuerTag(aktuellerTagImMonat); // Holt die Termine für den aktuellen Tag
					if (!termineAmTag.isEmpty()) { // Wenn es Termine an diesem Tag gibt
						Termin ersterTermin = termineAmTag.get(0); // Holt den ersten Termin des Tages (für die Farbanzeige)
						String farbe = getFarbeFuerKategorie(ersterTermin.getKategorie()); // Ermittelt die Farbe für die Kategorie des Termins
						tagLabel.setStyle(tagLabel.getStyle() + "-fx-text-fill: " + farbe + "; -fx-font-weight: bold;"); // Färbt die Tagesnummer entsprechend der Kategorie und hebt sie hervor
					}
					tagCounter++; // Erhöht den Tageszähler
				} else { // Wenn der Tageszähler den letzten Tag des Monats überschreitet
					break; // Beende die innere Schleife (Spalten)
				}
			}
			if (tagCounter > letzterTagDesMonats.getDayOfMonth()) { // Wenn der Tageszähler den letzten Tag des Monats überschreitet
				break; // Beende die äußere Schleife (Zeilen)
			}
		}
	}



	// Hilfsmethode, um die Farbe für eine Terminkategorie zu bestimmen
	private String getFarbeFuerKategorie(String kategorie) {
		if (kategorie == null) { // Wenn die Kategorie null ist
			return "black"; // Standardfarbe ist Schwarz
		}
		switch (kategorie) { // Überprüft die Kategorie
		case "Beruflich":
			return KategorieFarben.Beruflich.getFarbe(); // Gibt die Farbe für "Beruflich" zurück
		case "Privat":
			return KategorieFarben.Privat.getFarbe(); // Gibt die Farbe für "Privat" zurück
		case "FreieTage":
			return KategorieFarben.FreieTage.getFarbe(); // Gibt die Farbe für "FreieTage" zurück
		case "Geburtstage":
			return KategorieFarben.Geburtstage.getFarbe(); // Gibt die Farbe für "Geburtstage" zurück
		case "Wichtig":
			return KategorieFarben.Wichtig.getFarbe(); // Gibt die Farbe für "Wichtig" zurück
		default:
			return "black"; // Standardfarbe, wenn die Kategorie nicht erkannt wird
		}
	}



	// Aktualisiert die Wochenansicht
		private void updateWochenAnsicht(GridPane grid, Label titleLabel) {
			grid.getChildren().clear(); // Entfernt alle vorhandenen Elemente aus dem Gitter
			grid.setId("wochenAnsichtGrid"); // Setzt die ID für das Gitter (für späteres Auffinden)
			grid.setPadding(new Insets(10)); // Fügt einen Innenabstand von 10 Pixeln zum Gitter hinzu
			grid.setHgap(50); // Setzt den horizontalen Abstand zwischen den Zellen auf 50 Pixel (Platz für
								// Stundenlabels)
			grid.setVgap(2); // Setzt den vertikalen Abstand zwischen den Zellen auf 2 Pixel (weniger Platz)
			WeekFields weekFields = WeekFields.of(Locale.getDefault()); // Ermittelt die Definition für die Wochen (abhängig
																		// von der Sprache)
			LocalDate startOfWeek = aktuellesDatum.with(weekFields.dayOfWeek(), 1); // Ermittelt den ersten Tag der aktuellen WOche
			int weekNumber = aktuellesDatum.get(weekFields.weekOfWeekBasedYear()); // Ermittelt die Kalenderwoche des aktuellen Datums
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()); // Erstellt einen Formatter für das Datumsformat (z.B. Mai 7)
			String weekTitle = String.format("KW %d, %s - %s", weekNumber, dateFormatter.format(startOfWeek), dateFormatter.format(startOfWeek.plusDays(6))); // Erstellt den Titel für die Wochenansicht (KW ...,
																																								// Startdatum - Enddatum)
			titleLabel.setText(weekTitle); // Setzt den Titel der Wochenansicht
			titleLabel.setId("weekTitleLabel"); // Setzt die ID für das Titel-Label
			// **Spaltenconstraints explizit VOR dem Hinzufügen der Labels setzen**
			grid.getColumnConstraints().clear(); // Entfernt vorherige Spaltenbeschränkungen
			for (int i = 0; i < 8; i++) { // Schleife für die 8 Spalten (1 für Stunden, 7 für Tage)
				javafx.scene.layout.ColumnConstraints columnConstraints = new javafx.scene.layout.ColumnConstraints(); // Erstellt neue Spaltenbeschränkungen
				if (i == 0) { // Für die erste Spalte (Stunden)
					columnConstraints.setPrefWidth(50); // Setzt die bevorzugte Breite auf 50 Pixel
					columnConstraints.setMaxWidth(50); // Setzt die maximale Breite auf 50 Pixel
					columnConstraints.setMinWidth(50); // Setzt die minimale Breite auf 50 Pixel
				} else { // Für die restlichen Spalten (Tage)
					columnConstraints.setPrefWidth(100); // Setzt die bevorzugte Breite auf 100 Pixel
					columnConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS); // Erlaubt horizontales Wachstum der Spalten
				}
				grid.getColumnConstraints().add(columnConstraints); // Fügt die Spaltenbeschränkungen zum Grid hinzu
			}

			 grid.getRowConstraints().clear(); // Entferne vorherige RowConstraints
			    double zeilenHoehe = 30; // Passe diesen Wert nach Bedarf an (Höhe jeder Stunde in Pixel)
			    javafx.scene.layout.RowConstraints wochentagZeile = new javafx.scene.layout.RowConstraints();
			    wochentagZeile.setPrefHeight(20); // Höhe für die Wochentagszeile (anpassen)
			    wochentagZeile.setVgrow(javafx.scene.layout.Priority.NEVER);
			    grid.getRowConstraints().add(wochentagZeile);

			    for (int i = 0; i < 24; i++) { // Für die 24 Stunden
			        javafx.scene.layout.RowConstraints stundenZeile = new javafx.scene.layout.RowConstraints();
			        stundenZeile.setPrefHeight(zeilenHoehe);
			        stundenZeile.setVgrow(javafx.scene.layout.Priority.NEVER);
			        grid.getRowConstraints().add(stundenZeile);
			    }

			DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00"); // Erstellt einen Formatierer für die Stundenanzeige (z.B. "09:00")
			DateTimeFormatter dayOfWeekFormatterWoche = DateTimeFormatter.ofPattern("E", Locale.getDefault()); // Erstellt einen  Formatierer  für den kurzen Wochentagsnamen (z.B. "Mo")
			// Erste Spalte (Uhrzeiten)
			for (int i = 0; i < 24; i++) { // Schleife für die 24 Stunden des Tages
				LocalTime time = LocalTime.of(i, 0); // Erstellt ein LocalTime-Objekt für die aktuelle Stunde
				Label hourLabel = new Label(hourFormatter.format(time)); // Erstellt ein Label mit der formatierten Stunde
				GridPane.setConstraints(hourLabel, 0, i + 1); // Setzt die Position des Stundenlabels im Grid (erste Spalte,
																// beginnend ab der zweiten Zeile)
				grid.getChildren().add(hourLabel); // Fügt das Stundenlabel zum Grid hinzu
			}
			// Erste Zeile (Wochentage)
			for (int i = 0; i < 7; i++) { // Schleife für die 7 Tage der Woche
				LocalDate day = startOfWeek.plusDays(i); // Ermittelt das Datum des aktuellen Tages der Woche
				Label dayLabel = new Label(dayOfWeekFormatterWoche.format(day)); // Erstellt ein Label mit dem kurzen
																					// Wochentagsnamen
				GridPane.setConstraints(dayLabel, i + 1, 0); // Setzt die Position des Tageslabels im Grid (erste Zeile,
																// beginnend ab der zweiten Spalte)
				grid.getChildren().add(dayLabel); // Fügt das Tageslabel zum Grid hinzu
				if (day.isEqual(heutigesDatum)) { // Wenn der aktuelle Tag der heutige Tag ist
					dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;"); // Hebt den heutigen Tag hervor
				}
			}

			for (int i = 0; i < 7; i++) { // Schleife für die 7 Tage der Woche
				LocalDate day = startOfWeek.plusDays(i); // Ermittelt das Datum des aktuellen Tages der Woche
				List<Termin> termineAmTag = terminVerwaltung.getTermineFuerTag(day); // Holt die Termine für den aktuellen
																						// Tag
				for (Termin termin : termineAmTag) { // Schleife durch die Termine des aktuellen Tages
					if (termin.getStartTime() != null && termin.getEndTime() != null) { // Überprüft, ob Start- und Endzeit
																						// vorhanden sind
						int startHour = termin.getStartTime().getHour(); // Holt die Startstunde des Termins
						int startMinute = termin.getStartTime().getMinute(); // Holt die Startminute des Termins
						int endHour = termin.getEndTime().getHour(); // Holt die Endstunde des Termins
						int endMinute = termin.getEndTime().getMinute(); // Holt die Endminute des Termins

						double startRow = startHour + 1 + (double) startMinute / 60;
						double endRow = endHour + 1 + (double) endMinute / 60;
						int rowSpan = (int) Math.ceil(endRow) - (int) Math.floor(startRow); // Berechne die Differenz der gerundeten Zeilenindizes
						System.out.println("Termin: " + termin.getBezeichnung() + ", StartRow: " + startRow + ", EndRow: " + endRow + ", RowSpan: " + (endRow - startRow));


						if (endRow > startRow && startRow > 0 && endRow <= 25) {
						    Label terminLabel = new Label(termin.getBezeichnung());
						    String farbe = getFarbeFuerKategorie(termin.getKategorie());
						    terminLabel.setStyle("-fx-background-color: " + farbe + "; -fx-padding: 2px; -fx-text-fill: white; -fx-font-size: 10px; -fx-alignment: TOP_LEFT;");
						    terminLabel.setWrapText(true);
						    if (grid.getColumnConstraints().size() > i + 1) {
						        terminLabel.setMaxWidth(grid.getColumnConstraints().get(i + 1).getPrefWidth());
						    }
						    GridPane.setConstraints(terminLabel, i + 1, (int) Math.floor(startRow), 1, rowSpan);
						    grid.getChildren().add(terminLabel);

						    // Versuche, die Höhe direkt zu setzen oder zu binden
						    double dauerInStunden = endHour + (double) endMinute / 60 - (startHour + (double) startMinute / 60);
						    double labelPrefHoehe = dauerInStunden * zeilenHoehe; // Verwende die zuvor definierte zeilenHoehe
						    terminLabel.setPrefHeight(labelPrefHoehe);
						    terminLabel.setMinHeight(labelPrefHoehe);
						    terminLabel.setMaxHeight(Double.MAX_VALUE); // Erlaube ggf. Wachstum, falls der Text es erfordert

						    GridPane.setFillWidth(terminLabel, true);
						    GridPane.setFillHeight(terminLabel, true);

						    terminLabel.setOnMouseClicked(event -> {
						        if (event.getClickCount() == 2) {
						            zeigeTerminDetails(termin);
								}
							});
						}
					}
				}
			}
		}



	// Zeigt die Details eines Termins in einem neuen Fenster
	private void zeigeTerminDetails(Termin termin) {
		Stage detailsStage = new Stage(); // Erstellt ein neues Fenster für die Termindetails
		detailsStage.initModality(Modality.APPLICATION_MODAL); // Macht das Fenster modal, blockiert Interaktionen mit
																// anderen Fenstern
		detailsStage.setTitle("Termin Details"); // Setzt den Titel des Detailfensters
		VBox vbox = new VBox(10); // Erstellt ein vertikales Layout mit einem Abstand von 10 Pixeln
		vbox.setPadding(new Insets(10)); // Fügt einen Innenabstand von 10 Pixeln hinzu
		Label bezeichnungLabel = new Label("Bezeichnung: " + termin.getBezeichnung()); // Erstellt ein Label für die
																						// Bezeichnung des Termins
		Label datumLabel = new Label("Datum: " + DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(termin.getDatum())); // Erstellt ein  Label für das Datum des Termins
		Label zeitLabel = new Label("Zeit: " + termin.getStartTime().toString() + " - " + termin.getEndTime().toString()); // Erstellt ein Label für die Start- und Endzeit des  Termins
		Label beschreibungLabel = new Label("Beschreibung: " + termin.getBeschreibung()); // Erstellt ein Label für die Beschreibung des Termins
		Label kategorieLabel = new Label("Kategorie: " + termin.getKategorie()); // Erstellt ein Label für die Kategorie des Termins
		Label erinnerungLabel = new Label("Erinnerung: " + (termin.getErinnerung() == null || termin.getErinnerung().isEmpty() ? "Keine" : termin.getErinnerung())); // Erstellt ein Label für die Erinnerung des Termins (zeigt "Keine", wenn keine vorhanden)
		Button bearbeitenButton = new Button("Bearbeiten (zukünftig)"); // Erstellt einen Button zum Bearbeiten des Termins (Funktionalität zukünftig)
		Button loeschenButton = new Button("Löschen"); // Erstellt einen Button zum Löschen des Termins
		loeschenButton.setOnAction(e -> { // Setzt eine Aktion für den Löschen-Button
			terminVerwaltung.entferneTermin(termin); // Entfernt den aktuellen Termin aus der Terminverwaltung
			terminVerwaltung.speichereTermine(terminVerwaltung.getTermine()); // Speichert die aktualisierte Liste der
																				// Termine
			updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel")); // Aktualisiert die Wochenansicht
			updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid")); // Aktualisiert die
																							// Monatsansicht
			updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear()); // Aktualisiert die Jahresübersicht
			detailsStage.close(); // Schließt das Detailfenster
		});
		vbox.getChildren().addAll(bezeichnungLabel, datumLabel, zeitLabel, beschreibungLabel, kategorieLabel, erinnerungLabel, bearbeitenButton, loeschenButton); // Fügt alle Labels und Buttons zum vertikalen Layout hinzu
		Scene scene = new Scene(vbox); // Erstellt eine neue Szene mit dem vertikalen Layout
		detailsStage.setScene(scene); // Setzt die Szene für das Detailfenster
		detailsStage.showAndWait(); // Zeigt das Detailfenster und blockiert die Ausführung, bis es geschlossen wird
	}
	// Speichert die Termine beim Beenden der Anwendung
	@Override public void stop() throws Exception {
		terminVerwaltung.speichereTermine(terminVerwaltung.getTermine()); // Speichert die aktuelle Liste der Termine, wenn die Anwendung beendet wird
	}
	// Main-Methode zum Starten der Anwendung
	public static void main(String[] args) {
		launch(args); // Startet die JavaFX-Anwendung
	}
}