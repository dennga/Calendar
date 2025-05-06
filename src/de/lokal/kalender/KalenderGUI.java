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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KalenderGUI extends Application {

    // Verwaltung der Termine
    private TerminVerwaltung terminVerwaltung;

    // Aktuelle und heutige Datumswerte
    private LocalDate aktuellesDatum = LocalDate.now();
    private LocalDate heutigesDatum = LocalDate.now();

    // Hauptlayout- und Menü-Elemente
    private BorderPane root;
    private VBox centerCalendarPane; // Für die Kalenderansicht (Wochenansicht)
    private VBox menuPane;
    private boolean isMenuVisible = false;

    // Definition der Farbkategorien als Enum
    private enum KategorieFarben {
        Beruflich("blue"), Privat("green"), FreieTage("red"), Geburtstage("orange"), Wichtig("purple");

        private String farbe;

        KategorieFarben(String farbe) {
            this.farbe = farbe;
        }

        public String getFarbe() {
            return farbe;
        }
    }

    // Initialisierung der Anwendung
    @Override
    public void init() throws Exception {
        terminVerwaltung = new TerminVerwaltung();
        terminVerwaltung.ladeTermine();
    }

    // Start der JavaFX-Anwendung
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Terminator");

        // Hauptlayout erstellen
        root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #b7b7a4;");

        // Linker Bereich für Monatsansicht und Navigation erstellen
        VBox leftPane = createLeftPane();
        root.setLeft(leftPane);

        // Center-Bereich für die Wochenansicht erstellen
        centerCalendarPane = createCenterPane();
        root.setCenter(centerCalendarPane);

        // Menü erstellen (wird initial nicht angezeigt)
        menuPane = createMenu();

        // Top-Bereich (Platzhalter für globale Navigation)
        Label navigationPlatzhalter = new Label("Navigation");
        root.setTop(navigationPlatzhalter);

        // Szenen und Stage einrichten
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Erstellt den linken Bereich mit Monatsansicht, Legende und Jahresübersicht
    private VBox createLeftPane() {
        VBox leftPane = new VBox(10);

        // Monatsnavigation
        HBox monthNav = new HBox(5);
        Button prevMonthButton = new Button("<");
        Button todayMonthButton = new Button("Heute");
        Button nextMonthButton = new Button(">");
        monthNav.getChildren().addAll(prevMonthButton, todayMonthButton, nextMonthButton);
        leftPane.getChildren().add(monthNav);

        // Monatsanzeige
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
        Label monthYearLabel = new Label(monthYearFormatter.format(aktuellesDatum));
        leftPane.getChildren().add(monthYearLabel);

        // Monatsansicht-Grid
        GridPane monatsAnsichtGrid = new GridPane();
        monatsAnsichtGrid.setPadding(new Insets(5));
        monatsAnsichtGrid.setHgap(5);
        monatsAnsichtGrid.setVgap(5);
        updateMonatsAnsicht(monatsAnsichtGrid);
        leftPane.getChildren().add(monatsAnsichtGrid);

        // Legende
        VBox legendenBereich = createLegende();
        leftPane.getChildren().add(legendenBereich);

        // Jahresübersicht
        GridPane jahresUebersichtGrid = new GridPane();
        jahresUebersichtGrid.setPadding(new Insets(5));
        jahresUebersichtGrid.setHgap(3);
        jahresUebersichtGrid.setVgap(3);
        updateJahresUebersicht(jahresUebersichtGrid, aktuellesDatum.getYear());
        leftPane.getChildren().add(jahresUebersichtGrid);

        // Menü-Button
        Button menuButton = new Button("Menü");
        menuButton.setOnAction(event -> toggleMenu());
        leftPane.getChildren().add(menuButton);

        // Neuer Termin Button
        Button neuerTerminButton = new Button("Neuer Termin");
        neuerTerminButton.setOnAction(event -> erstelleNeuerTermin());
        leftPane.getChildren().add(neuerTerminButton);

        return leftPane;
    }

    // Erstellt den Center-Bereich für die Wochenansicht
    private VBox createCenterPane() {
        VBox centerPane = new VBox(5);

        // Wochenansicht-Navigation
        HBox weekNav = new HBox(5);
        Button prevWeekButton = new Button("<");
        Button todayWeekButton = new Button("Heute");
        Button nextWeekButton = new Button(">");
        weekNav.getChildren().addAll(prevWeekButton, todayWeekButton, nextWeekButton);
        centerPane.getChildren().add(weekNav);

        // Wochenansicht-Titel
        Label weekTitleLabel = new Label();
        centerPane.getChildren().add(weekTitleLabel);

        // Wochenansicht-Grid mit ScrollPane
        GridPane wochenAnsichtGrid = new GridPane();
        wochenAnsichtGrid.setPadding(new Insets(10));
        wochenAnsichtGrid.setHgap(50);
        wochenAnsichtGrid.setVgap(15);
        updateWochenAnsicht(wochenAnsichtGrid, weekTitleLabel);
        ScrollPane scrollPane = new ScrollPane(wochenAnsichtGrid);
        scrollPane.setFitToWidth(true);
        centerPane.getChildren().add(scrollPane);

        // Event-Handler für die Wochenansicht-Navigation
        prevWeekButton.setOnAction(event -> navigateWeek(-1));
        todayWeekButton.setOnAction(event -> navigateWeek(0));
        nextWeekButton.setOnAction(event -> navigateWeek(1));

        return centerPane;
    }

    // Erstellt die Legende für die Terminkategorien
    private VBox createLegende() {
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
        return legendenBereich;
    }

    // Erstellt das Menü
    private VBox createMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));

        Label titelLabel = new Label("Menü");
        titelLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        menu.getChildren().add(titelLabel);

        Button sucheButton = new Button("Suche (zukünftig)");
        Button einstellungenButton = new Button("Einstellungen (zukünftig)");
        Button kontoButton = new Button("Konto (zukünftig)");
        Button naechsterTerminButton = new Button("Nächster Termin (zukünftig)");
        Button zurueckButton = new Button("Zurück zum Kalender");
        zurueckButton.setOnAction(event -> toggleMenu());

        menu.getChildren().addAll(sucheButton, einstellungenButton, kontoButton, naechsterTerminButton, zurueckButton);

        return menu;
    }

    // Zeigt oder verbirgt das Menü
    private void toggleMenu() {
        isMenuVisible = !isMenuVisible;
        if (isMenuVisible) {
            root.setCenter(menuPane);
        } else {
            root.setCenter(centerCalendarPane);
        }
    }

    // Erstellt den "Neuer Termin" Dialog
    private void erstelleNeuerTermin() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Neuen Termin erstellen");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        Label datumLabel = new Label("Datum:");
        DatePicker datumPicker = new DatePicker(aktuellesDatum);
        
        Label startTimeLabel = new Label("Startzeit:");
        ComboBox<LocalTime> startTimeCombo = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 30) { // Nur volle und halbe Stunden
                startTimeCombo.getItems().add(LocalTime.of(i, j));
            }
        }
        startTimeCombo.setValue(LocalTime.now().withMinute(0).withSecond(0).withNano(0)); // Standard auf aktuelle volle Stunde

        Label endTimeLabel = new Label("Endzeit:");
        ComboBox<LocalTime> endTimeCombo = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 30) {
                endTimeCombo.getItems().add(LocalTime.of(i, j));
            }
        }
        endTimeCombo.setValue(LocalTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0)); // Standard auf eine Stunde später


        Label beschreibungLabel = new Label("Beschreibung:");
        TextArea beschreibungText = new TextArea();
        beschreibungText.setPrefRowCount(3);
        
        Label bezeichnungLabel = new Label("Bezeichnung:");
        TextField bezeichnungText = new TextField();

        Label erinnerungLabel = new Label("Erinnerung:");
        TextField erinnerungText = new TextField();

        Label kategorieLabel = new Label("Kategorie:");
        ComboBox<String> kategorieCombo = new ComboBox<>();
        for (KategorieFarben kategorie : KategorieFarben.values()) {
            kategorieCombo.getItems().add(kategorie.name());
        }
        kategorieCombo.setValue(KategorieFarben.Privat.name()); // Standardkategorie

        Button speichernButton = new Button("Speichern");
        speichernButton.setOnAction(e -> {
            LocalDate terminDatum = datumPicker.getValue();
            LocalTime terminStartTime = startTimeCombo.getValue();
            LocalTime terminEndTime = endTimeCombo.getValue();
            String terminBeschreibung = beschreibungText.getText();
            String terminBezeichnung = bezeichnungText.getText();
            String terminErinnerung = erinnerungText.getText();
            String terminKategorie = kategorieCombo.getValue();

            if (terminDatum != null && terminStartTime != null && terminEndTime != null && !terminBeschreibung.isEmpty()) {
                Termin neuerTermin = new Termin(terminDatum,terminBezeichnung, terminBeschreibung, terminErinnerung, terminKategorie, terminStartTime, terminEndTime);
                terminVerwaltung.addTermin(neuerTermin);
                terminVerwaltung.speichereTermine(terminVerwaltung.getTermine());
                updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid")); // Monatsansicht aktualisieren
                updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel")); // Wochenansicht aktualisieren
                updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear()); // Jahresübersicht aktualisieren
                dialogStage.close();
            } else {
                System.out.println("Bitte füllen Sie alle notwendigen Felder aus (Datum, Startzeit, Endzeit, Beschreibung).");
            }
        });

        Button abbrechenButton = new Button("Abbrechen");
        abbrechenButton.setOnAction(e -> dialogStage.close());

        grid.add(datumLabel, 0, 0);
        grid.add(datumPicker, 1, 0);
        grid.add(bezeichnungLabel, 0, 1);
        grid.add(bezeichnungText, 1, 1);
        grid.add(startTimeLabel, 0, 2);
        grid.add(startTimeCombo, 1, 2);
        grid.add(endTimeLabel, 0, 3);
        grid.add(endTimeCombo, 1, 3);
        grid.add(beschreibungLabel, 0, 4);
        grid.add(beschreibungText, 1, 4);
        grid.add(erinnerungLabel, 0, 5);
        grid.add(erinnerungText, 1, 5);
        grid.add(kategorieLabel, 0, 6);
        grid.add(kategorieCombo, 1, 6);
        grid.add(speichernButton, 0, 7);
        grid.add(abbrechenButton, 1, 7);

        Scene dialogScene = new Scene(grid);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    // Navigiert in der Wochenansicht vor oder zurück
    private void navigateWeek(int direction) {
        if (direction < 0) {
            aktuellesDatum = aktuellesDatum.minusWeeks(1);
        } else if (direction > 0) {
            aktuellesDatum = aktuellesDatum.plusWeeks(1);
        } else {
            aktuellesDatum = heutigesDatum;
        }
        updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel"));
        updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid"));
        ((Label) root.getLeft().lookup("#monthYearLabel")).setText(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()).format(aktuellesDatum));
        updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear());
    }

    // Aktualisiert die Jahresübersicht
    private void updateJahresUebersicht(GridPane grid, int jahr) {
        grid.getChildren().clear();
        grid.setId("jahresUebersichtGrid"); // ID für Lookup
        LocalDate heute = LocalDate.now();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault());
        int colCounter = 0;
        int rowCounter = 0;

        for (int monat = 1; monat <= 12; monat++) {
            LocalDate ersterTagDesMonats = LocalDate.of(jahr, monat, 1);
            Label monatsLabel = new Label(monthFormatter.format(ersterTagDesMonats));
            monatsLabel.setStyle("-fx-font-size: 10px;");

            if (heute.getYear() == jahr && heute.getMonthValue() == monat) {
                monatsLabel.setStyle(monatsLabel.getStyle() + "-fx-font-weight: bold; -fx-text-fill: blue;");
            }

            GridPane.setConstraints(monatsLabel, colCounter, rowCounter);
            grid.getChildren().add(monatsLabel);

            if (colCounter == 3) {
                colCounter = 0;
                rowCounter++;
            } else {
                colCounter++;
            }
        }
    }

    // (Platzhalter) Zeigt Termine für einen bestimmten Tag an
    private void zeigeTermineFuerTag(LocalDate tag) {
        // Hier würde die Logik implementiert, um Termine für den ausgewählten Tag anzuzeigen
        System.out.println("Zeige Termine für: " + tag);
    }

    // (Platzhalter) Gibt eine Liste von Terminen für einen bestimmten Tag zurück
    public List<Termin> getTermineFuerTag(LocalDate tag) {
        // Hier würde die Logik implementiert, um Termine aus der TerminVerwaltung zu filtern
        return terminVerwaltung.getTermineFuerTag(tag);
    }

    // Aktualisiert die Monatsansicht
    private void updateMonatsAnsicht(GridPane grid) {
        grid.getChildren().clear();
        grid.setId("monatsAnsichtGrid"); // ID für Lookup

        LocalDate ersterTagDesMonats = aktuellesDatum.withDayOfMonth(1);
        LocalDate letzterTagDesMonats = aktuellesDatum.withDayOfMonth(aktuellesDatum.lengthOfMonth());
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        DayOfWeek ersterTagDerWoche = weekFields.getFirstDayOfWeek();
        int ersterWochentagDesMonatsWert = ersterTagDesMonats.getDayOfWeek().getValue();
        int ersterTagDerWocheWert = weekFields.getFirstDayOfWeek().getValue();
        int ersterTagIndex = (ersterWochentagDesMonatsWert - ersterTagDerWocheWert + 7) % 7;

        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("EE", Locale.getDefault());
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

                    List<Termin> termineAmTag = terminVerwaltung.getTermineFuerTag(aktuellerTagImMonat);
                    if (!termineAmTag.isEmpty()) {
                        Termin ersterTermin = termineAmTag.get(0);
                        String farbe = getFarbeFuerKategorie(ersterTermin.getKategorie());
                        tagLabel.setStyle(tagLabel.getStyle() + "-fx-text-fill: " + farbe + "; -fx-font-weight: bold;");
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

    // Hilfsmethode, um die Farbe für eine Terminkategorie zu bestimmen
    private String getFarbeFuerKategorie(String kategorie) {
        if (kategorie == null) {
            return "black"; // Standardfarbe
        }
        switch (kategorie) {
            case "Beruflich":
                return KategorieFarben.Beruflich.getFarbe();
            case "Privat":
                return KategorieFarben.Privat.getFarbe();
            case "FreieTage":
                return KategorieFarben.FreieTage.getFarbe();
            case "Geburtstage":
                return KategorieFarben.Geburtstage.getFarbe();
            case "Wichtig":
                return KategorieFarben.Wichtig.getFarbe();
            default:
                return "black";
        }
    }

 // Aktualisiert die Wochenansicht
    private void updateWochenAnsicht(GridPane grid, Label titleLabel) {
        grid.getChildren().clear();
        grid.setId("wochenAnsichtGrid"); // ID für Lookup
        grid.setPadding(new Insets(10));

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = aktuellesDatum.with(weekFields.dayOfWeek(), 1);
        int weekNumber = aktuellesDatum.get(weekFields.weekOfWeekBasedYear());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault());
        String weekTitle = String.format("KW %d, %s - %s", weekNumber, dateFormatter.format(startOfWeek),
                dateFormatter.format(startOfWeek.plusDays(6)));
        titleLabel.setText(weekTitle);
        titleLabel.setId("weekTitleLabel"); // ID für Lookup

        String linienStyle = "-fx-border-color: darkgray; -fx-border-width: 0.5px;";
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:00");
        DateTimeFormatter dayOfWeekFormatterWoche = DateTimeFormatter.ofPattern("E", Locale.getDefault());

        // Erste Spalte (Uhrzeiten) und erste Zeile (Wochentage) erstellen
        for (int i = 0; i < 24; i++) {
            LocalTime time = LocalTime.of(i, 0);
            Label hourLabel = new Label(hourFormatter.format(time));
            GridPane.setConstraints(hourLabel, 0, i + 1);
            grid.getChildren().add(hourLabel);
            hourLabel.setStyle(linienStyle);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            Label dayLabel = new Label(dayOfWeekFormatterWoche.format(day));
            GridPane.setConstraints(dayLabel, i + 1, 0);
            grid.getChildren().add(dayLabel);
            dayLabel.setStyle(linienStyle);
            if (day.isEqual(heutigesDatum)) {
                dayLabel.setStyle(dayLabel.getStyle() + "-fx-font-weight: bold; -fx-text-fill: blue;");
            }

            // Termine für den aktuellen Tag in der Wochenansicht anzeigen
            List<Termin> termineAmTag = terminVerwaltung.getTermineFuerTag(day);
            for (Termin termin : termineAmTag) {
                if (termin.getStartTime() != null && termin.getEndTime() != null) {
                    int startHour = termin.getStartTime().getHour();
                    int endHour = termin.getEndTime().getHour();

                    // Annahme: Termine dauern mindestens 30 Minuten und starten/enden zu vollen/halben Stunden
                    int startRow = startHour + 1;
                    int endRow = endHour + (termin.getEndTime().getMinute() > 0 ? 2 : 1); // +1 für die Header-Zeile

                    if (startRow > 0 && startRow < 25) {
                        Label terminLabel = new Label(termin.getBezeichnung());
                        String farbe = getFarbeFuerKategorie(termin.getKategorie());
                        terminLabel.setStyle("-fx-background-color: " + farbe + "; -fx-padding: 2px; -fx-text-fill: white; -fx-font-size: 10px; -fx-border-color: darkgray; -fx-border-width: 0.5px;");
                        GridPane.setConstraints(terminLabel, i + 1, startRow, 1, endRow - startRow); // Spanne über mehrere Zeilen
                        grid.getChildren().add(terminLabel);

                        // Doppelklick-Handler für Detailansicht
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
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setTitle("Termin Details");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label bezeichnungLabel = new Label("Bezeichnung: " + termin.getBezeichnung());
        Label datumLabel = new Label("Datum: " + DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()).format(termin.getDatum()));
        Label zeitLabel = new Label("Zeit: " + termin.getStartTime().toString() + " - " + termin.getEndTime().toString());
        Label beschreibungLabel = new Label("Beschreibung: " + termin.getBeschreibung());
        Label kategorieLabel = new Label("Kategorie: " + termin.getKategorie());
        Label erinnerungLabel = new Label("Erinnerung: " + (termin.getErinnerung() == null || termin.getErinnerung().isEmpty() ? "Keine" : termin.getErinnerung()));

        Button bearbeitenButton = new Button("Bearbeiten (zukünftig)");
        Button loeschenButton = new Button("Löschen");
        loeschenButton.setOnAction(e -> {
            terminVerwaltung.entferneTermin(termin);
            terminVerwaltung.speichereTermine(terminVerwaltung.getTermine());
            updateWochenAnsicht((GridPane) centerCalendarPane.lookup("#wochenAnsichtGrid"), (Label) centerCalendarPane.lookup("#weekTitleLabel"));
            updateMonatsAnsicht((GridPane) root.getLeft().lookup("#monatsAnsichtGrid"));
            updateJahresUebersicht((GridPane) root.getLeft().lookup("#jahresUebersichtGrid"), aktuellesDatum.getYear());
            detailsStage.close();
        });

        vbox.getChildren().addAll(bezeichnungLabel, datumLabel, zeitLabel, beschreibungLabel, kategorieLabel, erinnerungLabel, bearbeitenButton, loeschenButton);

        Scene scene = new Scene(vbox);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }

    // Speichert die Termine beim Beenden der Anwendung
    @Override
    public void stop() throws Exception {
        terminVerwaltung.speichereTermine(terminVerwaltung.getTermine());
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        launch(args);
    }
}