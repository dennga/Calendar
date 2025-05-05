package de.lokal.kalender;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KalenderGUI extends Application {

	private TerminVerwaltung terminVerwaltung;
	private LocalDate aktuellesDatum = LocalDate.now(); // Startdatum für die Ansichten
	private LocalDate heutigesDatum = LocalDate.now(); // Speichert das heutige Datum

	private VBox centerCalendarPane; // Speichert die Kalenderansicht (Wochenansicht)
	private VBox menuPane; // Speichert die Menüansicht
	private boolean isMenuVisible = false; // Zustand, ob das Menü sichtbar ist
	 private BorderPane root; // Das Hauptlayout als Klassenvariable

	private enum KategorieFarben {
		ARBEIT("blue"), PRIVAT("green"), FEIERTAGE("red"), GEBURTSTAG("orange"), WICHTIG("purple");

		private String farbe;

		KategorieFarben(String farbe) {
			this.farbe = farbe;
		}

		public String getFarbe() {
			return farbe;
		}
	}

	@Override
	public void init() throws Exception {
		terminVerwaltung = new TerminVerwaltung();
		terminVerwaltung.ladeTermine();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Terminator");

		// Hauptlayout: BorderPane
		root = new BorderPane();
        root.setPadding(new Insets(10)); // Etwas Abstand vom Fensterrand

		// Linker Bereich (VBox für Navigation, Monatsanzeige und leere Bereiche)
		VBox leftPane = new VBox(10); // 10 Pixel Abstand zwischen den Elementen

		// Navigation für die Monatsansicht
		HBox monthNav = new HBox(5); // Horizontaler Container für die Buttons
		Button prevMonthButton = new Button("<");
		Button todayMonthButton = new Button("Heute");
		Button nextMonthButton = new Button(">");
		monthNav.getChildren().addAll(prevMonthButton, todayMonthButton, nextMonthButton);
		leftPane.getChildren().add(monthNav);

		// Monat und Jahr anzeigen
		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
		Label monthYearLabel = new Label(monthYearFormatter.format(aktuellesDatum));
		leftPane.getChildren().add(monthYearLabel);

		// Monatsansicht
		GridPane monatsAnsichtGrid = new GridPane();
		monatsAnsichtGrid.setPadding(new Insets(5));
		monatsAnsichtGrid.setHgap(5);
		monatsAnsichtGrid.setVgap(5);
		updateMonatsAnsicht(monatsAnsichtGrid); // Methode zum Zeichnen der Monatsansicht
		leftPane.getChildren().add(monatsAnsichtGrid);

		// Legende (ersetzt leererBereich1)
		VBox legendenBereich = new VBox(5);
		Label legendenUeberschrift = new Label("Kategorien:");
		legendenBereich.getChildren().add(legendenUeberschrift);

		for (KategorieFarben kategorie : KategorieFarben.values()) {
			Label farbQuadrat = new Label("■");
			farbQuadrat.setStyle("-fx-text-fill: " + kategorie.getFarbe() + "; -fx-font-size: 16px;");
			Label kategorieName = new Label(kategorie.name());
			HBox legendenEintrag = new HBox(5, farbQuadrat, kategorieName);
			legendenBereich.getChildren().add(legendenEintrag);
		}
		leftPane.getChildren().add(legendenBereich);

		// Button für das Menü (ersetzt leererBereich2)
		Button menuButton = new Button("Menü");
		leftPane.getChildren().add(menuButton);

		// Center Bereich (VBox für Wochenansicht-Navigation und Wochenansicht-Grid)
		VBox centerPane = new VBox(5);

		// Navigation für die Wochenansicht
		HBox weekNav = new HBox(5);
		Button prevWeekButton = new Button("<");
		Button todayWeekButton = new Button("Heute");
		Button nextWeekButton = new Button(">");
		weekNav.getChildren().addAll(prevWeekButton, todayWeekButton, nextWeekButton);
		centerPane.getChildren().add(weekNav);

		// Wochenansicht-Titel (zeigt Kalenderwoche und Datumspanne)
		Label weekTitleLabel = new Label();
		centerPane.getChildren().add(weekTitleLabel);

		// Wochenansicht
		GridPane wochenAnsichtGrid = new GridPane();
		wochenAnsichtGrid.setPadding(new Insets(10));
		wochenAnsichtGrid.setHgap(5); // Horizontaler Abstand zwischen den Zellen
		wochenAnsichtGrid.setVgap(5); // Vertikaler Abstand zwischen den Zellen
		updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel); // Methode zum Zeichnen der Wochenansicht
		centerPane.getChildren().add(wochenAnsichtGrid);

		// Center Bereich für die Kalenderansicht speichern
		centerCalendarPane = centerPane;
		root.setCenter(centerCalendarPane); // Initial die Kalenderansicht setzen

		// Menü erstellen (wird initial nicht angezeigt)
		menuPane = createMenu();

		 // Initial die Kalenderansicht und den linken Bereich setzen
        root.setCenter(centerCalendarPane);
        root.setLeft(leftPane);

        menuButton.setOnAction(event -> {
            isMenuVisible = !isMenuVisible;
            if (isMenuVisible) {
                root.setCenter(menuPane);
            } else {
                root.setCenter(centerCalendarPane);
            }
        });

		// Top Bereich (Platzhalter für globale Navigation)
		Label navigationPlatzhalter = new Label("Navigation (Platzhalter)");
		root.setTop(navigationPlatzhalter);

		// Event-Handler für die Monatsansicht-Navigation
		prevMonthButton.setOnAction(event -> {
			aktuellesDatum = aktuellesDatum.minusMonths(1);
			monthYearLabel.setText(monthYearFormatter.format(aktuellesDatum));
			updateMonatsAnsicht(monatsAnsichtGrid);
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel); // Auch Wochenansicht aktualisieren
		});

		todayMonthButton.setOnAction(event -> {
			aktuellesDatum = heutigesDatum;
			monthYearLabel.setText(monthYearFormatter.format(aktuellesDatum));
			updateMonatsAnsicht(monatsAnsichtGrid);
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel); // Auch Wochenansicht aktualisieren
		});

		nextMonthButton.setOnAction(event -> {
			aktuellesDatum = aktuellesDatum.plusMonths(1);
			monthYearLabel.setText(monthYearFormatter.format(aktuellesDatum));
			updateMonatsAnsicht(monatsAnsichtGrid);
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel); // Auch Wochenansicht aktualisieren
		});

		// Event-Handler für die Wochenansicht-Navigation
		prevWeekButton.setOnAction(event -> {
			aktuellesDatum = aktuellesDatum.minusWeeks(1);
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel);
		});

		todayWeekButton.setOnAction(event -> {
			aktuellesDatum = heutigesDatum;
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel);
		});

		nextWeekButton.setOnAction(event -> {
			aktuellesDatum = aktuellesDatum.plusWeeks(1);
			updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel);
		});

		Scene scene = new Scene(root, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}




	private void zeigeTermineFuerTag(LocalDate tag) {
	    // ... (Implementierung, um Termine für den gegebenen Tag anzuzeigen)
	}

	public List<Termin> getTermineFuerTag(LocalDate tag) {
	    // ... (Implementierung, um eine Liste von Terminen für den gegebenen Tag zurückzugeben)
	}

	private VBox createMenu() {
		VBox menu = new VBox(10);
		menu.setPadding(new Insets(20));

		Label titelLabel = new Label("Menü");
		titelLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		menu.getChildren().add(titelLabel);

		Button monatButton = new Button("Monatsansicht");
		Button wocheButton = new Button("Wochenansicht");
		Button tagButton = new Button("Tagesansicht (zukünftig)");
		Button jahresButton = new Button("Jahresübersicht (zukünftig)");
		Button listeButton = new Button("Listenansicht (zukünftig)");
		Button aufgabenButton = new Button("Aufgaben (zukünftig)");
		Button notizenButton = new Button("Notizen (zukünftig)");
		Button sucheButton = new Button("Suche (zukünftig)");
		Button einstellungenButton = new Button("Einstellungen (zukünftig)");
		Button kontoButton = new Button("Konto (zukünftig)");
		Button heuteButton = new Button("Heute");
		Button neuerTerminButton = new Button("Neuer Termin (zukünftig)");
		Button zurueckButton = new Button("Zurück zum Kalender");
		zurueckButton.setOnAction(event -> {
			isMenuVisible = false;
			root.setCenter(centerCalendarPane); // Zurück zur Kalenderansicht

		});

		 monatButton.setOnAction(event -> {
	            isMenuVisible = false;
	            root.setCenter(centerCalendarPane); // Zeigt die Wochenansicht (im centerCalendarPane)
	        });

	        wocheButton.setOnAction(event -> {
	            isMenuVisible = false;
	            root.setCenter(centerCalendarPane); // Zeigt die Wochenansicht (im centerCalendarPane)
	        });

		menu.getChildren().addAll(monatButton, wocheButton, tagButton, jahresButton, listeButton, aufgabenButton,
				notizenButton, sucheButton, einstellungenButton, kontoButton, heuteButton, neuerTerminButton,
				zurueckButton);

		return menu;
	}

	private void updateMonatsAnsicht(GridPane grid) {
		grid.getChildren().clear();
		grid.setPadding(new Insets(5));
		grid.setHgap(5);
		grid.setVgap(5);

		LocalDate ersterTagDesMonats = aktuellesDatum.withDayOfMonth(1);
		LocalDate letzterTagDesMonats = aktuellesDatum.withDayOfMonth(aktuellesDatum.lengthOfMonth());
		DayOfWeek ersterWochentag = ersterTagDesMonats.getDayOfWeek();
		int ersterTagIndex = ersterWochentag.getValue() == 7 ? 0 : ersterWochentag.getValue();

		DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EE", Locale.getDefault());
		DayOfWeek ersterTagDerWoche = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
		for (int i = 0; i < 7; i++) {
			DayOfWeek aktuellerTagDerWoche = ersterTagDerWoche.plus(i);
			LocalDate irgendeinDatumDieserTag = LocalDate.now().with(aktuellerTagDerWoche);
			Label dayNameLabel = new Label(dayOfWeekFormatter.format(irgendeinDatumDieserTag));
			GridPane.setConstraints(dayNameLabel, i, 0);
			grid.getChildren().add(dayNameLabel);
		}

		int tagCounter = 1;
		for (int row = 1; row < 7; row++) {
			for (int col = 0; col < 7; col++) {
				if (row == 1 && col < ersterTagIndex) {
					continue;
				}
				LocalDate aktuellerTagImMonat = ersterTagDesMonats.plusDays(tagCounter - 1);
				if (tagCounter <= letzterTagDesMonats.getDayOfMonth()) {
					Label tagLabel = new Label(String.valueOf(tagCounter));
					GridPane.setConstraints(tagLabel, col, row);
					grid.getChildren().add(tagLabel);
					if (aktuellerTagImMonat.isEqual(heutigesDatum)) {
						tagLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue; -fx-cursor: hand;");
					} else {
						tagLabel.setStyle("-fx-cursor: hand;");
					}
					tagLabel.setOnMouseClicked(event -> {
						zeigeTermineFuerTag(aktuellerTagImMonat);
					});

					// Farbliche Darstellung des ersten Termins
					List<Termin> termineAmTag = terminVerwaltung.getTermineFuerTag(aktuellerTagImMonat);
					if (!termineAmTag.isEmpty()) {
						// Annahme: Termine sind nach Uhrzeit sortiert in getTermineFuerTag
						Termin ersterTermin = termineAmTag.get(0);
						String farbe = getFarbeFuerKategorie(ersterTermin.getKategorie());
						tagLabel.setStyle(tagLabel.getStyle() + "-fx-text-fill: " + farbe + "; -fx-font-weight: bold;"); // Farbe
																															// und
																															// Fett
																															// hinzufügen
					}

					tagCounter++;
				} else {
					break;
				}
			}
			if (tagCounter > letzterTagDesMonats.getDayOfMonth()) {
				break;
			}
		}
	}

	private String getFarbeFuerKategorie(String kategorie) {
	    if (kategorie == null) {
	        return "black"; // Oder eine andere Standardfarbe für Termine ohne Kategorie
	    }
	    switch (kategorie) {
	    case "Arbeit":
	        return KategorieFarben.ARBEIT.getFarbe();
	    case "Privat":
	        return KategorieFarben.PRIVAT.getFarbe();
	    case "Feiertage":
	        return KategorieFarben.FEIERTAGE.getFarbe();
	    case "Geburtstag":
	        return KategorieFarben.GEBURTSTAG.getFarbe();
	    case "Wichtig":
	        return KategorieFarben.WICHTIG.getFarbe();
	    default:
	        return "black";
	    }
	}

	// Methode zum Aktualisieren der Wochenansicht
	private void updateWochenAnsicht(GridPane grid, Label titleLabel) {
		grid.getChildren().clear();
		grid.setPadding(new Insets(10));
		grid.setHgap(5);
		grid.setVgap(5);

		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		LocalDate startOfWeek = aktuellesDatum.with(weekFields.dayOfWeek(), 1);
		int weekNumber = aktuellesDatum.get(weekFields.weekOfWeekBasedYear());
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault());
		String weekTitle = String.format("KW %d, %s - %s", weekNumber, dateFormatter.format(startOfWeek),
				dateFormatter.format(startOfWeek.plusDays(6)));
		titleLabel.setText(weekTitle);

		DateTimeFormatter dayOfWeekFormatterWoche = DateTimeFormatter.ofPattern("E", Locale.getDefault());
		for (int i = 0; i < 7; i++) {
			LocalDate day = startOfWeek.plusDays(i);
			Label dayLabel = new Label(dayOfWeekFormatterWoche.format(day));
			GridPane.setConstraints(dayLabel, i + 1, 0);
			grid.getChildren().add(dayLabel);
			if (day.isEqual(heutigesDatum)) {
				dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;"); // Beispielhafte Hervorhebung
			}
		}

		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00");
		for (int i = 0; i < 24; i++) {
			LocalTime time = LocalTime.of(i, 0);
			Label hourLabel = new Label(hourFormatter.format(time));
			GridPane.setConstraints(hourLabel, 0, i + 1);
			grid.getChildren().add(hourLabel);
		}
	}

	@Override
	public void stop() throws Exception {
		terminVerwaltung.speichereTermine(terminVerwaltung.getTermine());
	}

	public static void main(String[] args) {
		launch(args);
	}
}