import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class MainScreen {

    private BorderPane root;
    private GameManager gm;
    private VBox contentArea;
    private String activeTab = "overview";

    public MainScreen() {
        gm = SceneManager.getGameManager();
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d0f1a;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        root.setTop(buildHeader());
        root.setLeft(buildSidebar());

        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: #0d0f1a;");
        root.setCenter(contentArea);

        showTab("overview");
    }

    // ── HEADER ──────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 24, 0, 0));
        header.setStyle("-fx-background-color: #080a12; -fx-border-color: #1e2a3a; " +
                "-fx-border-width: 0 0 1 0; -fx-min-height: 56;");

        // Logo bölümü
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 24, 0, 24));
        logoBox.setStyle("-fx-min-width: 220; -fx-border-color: #1e2a3a; -fx-border-width: 0 1 0 0;");
        Label icon = new Label("⚽");
        icon.setStyle("-fx-font-size: 20px;");
        Label appName = new Label("SPORTS MANAGER");
        appName.setStyle("-fx-font-weight: bold; -fx-text-fill: #eceff1; -fx-font-size: 13px;");
        logoBox.getChildren().addAll(icon, appName);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Sağ bilgiler
        Team userTeam = gm.getUserTeam();
        ISport sport = gm.getSelectedSport();

        Label teamLabel = new Label(userTeam != null ? userTeam.getName() : "");
        teamLabel.setStyle("-fx-text-fill: #eceff1; -fx-font-weight: bold; -fx-font-size: 14px;");

        String sportName = sport != null ? sport.getSportName() : "";
        String emoji = "Football".equals(sportName) ? "⚽" : "🏐";
        Label sportLabel = new Label(emoji + " " + sportName);
        sportLabel.setStyle("-fx-text-fill: #546e7a; -fx-font-size: 12px;");

        VBox rightInfo = new VBox(2);
        rightInfo.setAlignment(Pos.CENTER_RIGHT);
        rightInfo.getChildren().addAll(teamLabel, sportLabel);

        header.getChildren().addAll(logoBox, spacer, rightInfo);
        return header;
    }

    // ── SIDEBAR ─────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPadding(new Insets(16, 8, 16, 8));
        sidebar.setStyle("-fx-background-color: #080a12; -fx-border-color: #1e2a3a; " +
                "-fx-border-width: 0 1 0 0; -fx-min-width: 200;");

        sidebar.getChildren().add(buildNavLabel("MAIN MENU"));
        sidebar.getChildren().add(buildNavBtn("📊", "Overview", "overview"));
        sidebar.getChildren().add(buildNavBtn("👥", "My Squad", "squad"));
        sidebar.getChildren().add(buildNavBtn("🏆", "Standings", "standings"));
        sidebar.getChildren().add(buildNavBtn("📅", "Fixtures", "fixtures"));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        sidebar.getChildren().add(buildNavLabel("ACTIONS"));
        sidebar.getChildren().add(buildNavBtn("🏃", "Train Team", "train"));
        sidebar.getChildren().add(buildNavBtn("▶", "Play Next Match", "play"));

        return sidebar;
    }

    private Label buildNavLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #37474f; -fx-font-size: 10px; -fx-font-weight: bold; " +
                "-fx-padding: 12 8 4 8;");
        return lbl;
    }

    private Button buildNavBtn(String icon, String label, String tab) {
        Button btn = new Button(icon + "  " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(getNavStyle(tab.equals(activeTab)));
        btn.setOnAction(e -> {
            activeTab = tab;
            refreshNavStyles(btn.getParent());
            showTab(tab);
        });
        btn.setUserData(tab);
        return btn;
    }

    private String getNavStyle(boolean active) {
        if (active) {
            return "-fx-background-color: #1a3a5c; -fx-text-fill: #4fc3f7; -fx-font-size: 13px; " +
                    "-fx-cursor: hand; -fx-padding: 10 12 10 12; -fx-background-radius: 8; " +
                    "-fx-font-weight: bold; -fx-border-width: 0;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: #78909c; -fx-font-size: 13px; " +
                "-fx-cursor: hand; -fx-padding: 10 12 10 12; -fx-background-radius: 8; -fx-border-width: 0;";
    }

    private void refreshNavStyles(javafx.scene.Parent parent) {
        if (parent == null) return;
        for (javafx.scene.Node node : ((VBox) parent).getChildren()) {
            if (node instanceof Button btn && btn.getUserData() != null) {
                btn.setStyle(getNavStyle(btn.getUserData().equals(activeTab)));
            }
        }
    }

    // ── TAB ROUTING ─────────────────────────────────────────
    private void showTab(String tab) {
        contentArea.getChildren().clear();
        switch (tab) {
            case "overview" -> contentArea.getChildren().add(buildOverview());
            case "squad"    -> contentArea.getChildren().add(buildSquad());
            case "standings"-> contentArea.getChildren().add(buildStandings());
            case "fixtures" -> contentArea.getChildren().add(buildFixtures());
            case "train"    -> doTrain();
            case "play"     -> doPlay();
            default         -> contentArea.getChildren().add(buildOverview());
        }
    }

    // ── OVERVIEW ────────────────────────────────────────────
    private ScrollPane buildOverview() {
        VBox pane = new VBox(24);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #0d0f1a;");

        Team userTeam = gm.getUserTeam();
        League league = gm.getLeague();

        // Başlık
        Label title = new Label("Overview");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Stat kartları
        HBox statRow = new HBox(16);
        statRow.getChildren().addAll(
            buildStatCard("📊", "Points", String.valueOf(userTeam.getPoints())),
            buildStatCard("👥", "Squad Size", String.valueOf(userTeam.getPlayers().size())),
            buildStatCard("✅", "Available", String.valueOf(userTeam.getAvailablePlayers().size())),
            buildStatCard("🏆", "League Pos", getLeaguePosition(userTeam, league))
        );

        // Taktik seçimi
        VBox tacticBox = new VBox(10);
        tacticBox.setStyle("-fx-background-color: #151929; -fx-background-radius: 12; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20;");
        Label tacticTitle = new Label("TACTIC");
        tacticTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        Label currentTactic = new Label("Current: " + userTeam.getTactic());
        currentTactic.setStyle("-fx-text-fill: #eceff1; -fx-font-size: 14px;");
        HBox tacticBtns = new HBox(10);
        for (String t : new String[]{"Attacking", "Balanced", "Defensive"}) {
            Button tb = new Button(t);
            boolean isActive = t.equals(userTeam.getTactic());
            tb.setStyle(isActive
                ? "-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; -fx-font-weight: bold; " +
                  "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 7 18 7 18;"
                : "-fx-background-color: #1e2a3a; -fx-text-fill: #cfd8dc; " +
                  "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 7 18 7 18;");
            tb.setOnAction(e -> {
                userTeam.setTactic(t);
                showTab("overview");
            });
            tacticBtns.getChildren().add(tb);
        }
        tacticBox.getChildren().addAll(tacticTitle, currentTactic, tacticBtns);

        // Bir sonraki maç
        VBox nextMatchBox = new VBox(10);
        nextMatchBox.setStyle("-fx-background-color: #151929; -fx-background-radius: 12; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20;");
        Label nmTitle = new Label("NEXT MATCH");
        nmTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");

        Match nextMatch = getNextMatch(league);
        if (nextMatch != null) {
            Label matchLabel = new Label(nextMatch.getHomeTeam().getName() + "  vs  " + nextMatch.getAwayTeam().getName());
            matchLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
            Label matchInfo = new Label("Week " + (league.getCurrentWeek() + 1) + " of " + league.getFixtures().size());
            matchInfo.setStyle("-fx-text-fill: #546e7a; -fx-font-size: 13px;");
            Button playBtn = new Button("▶  Play Match");
            playBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 9 24 9 24;");
            playBtn.setOnAction(e -> doPlay());
            nextMatchBox.getChildren().addAll(nmTitle, matchLabel, matchInfo, playBtn);
        } else {
            Label noMatch = new Label("Season finished!");
            noMatch.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 16px; -fx-font-weight: bold;");
            nextMatchBox.getChildren().addAll(nmTitle, noMatch);
        }

        pane.getChildren().addAll(title, statRow, tacticBox, nextMatchBox);

        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        sp.getStyleClass().add("scroll-pane-dark");
        return sp;
    }

    private HBox buildStatCard(String icon, String label, String value) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle("-fx-background-color: #151929; -fx-background-radius: 12; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 12; -fx-border-width: 1;");
        card.setMinWidth(180);

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 22px;");
        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        Label labelLbl = new Label(label);
        labelLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #546e7a;");

        card.getChildren().addAll(iconLbl, valueLbl, labelLbl);
        HBox wrap = new HBox(card);
        HBox.setHgrow(card, Priority.ALWAYS);
        return wrap;
    }

    // ── SQUAD ───────────────────────────────────────────────
    private ScrollPane buildSquad() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #0d0f1a;");

        Label title = new Label("My Squad");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        Team userTeam = gm.getUserTeam();

        // Coach bilgisi
        VBox coachBox = new VBox(8);
        coachBox.setStyle("-fx-background-color: #151929; -fx-background-radius: 10; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 16;");
        Label coachTitle = new Label("COACHING STAFF");
        coachTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        coachBox.getChildren().add(coachTitle);
        for (Coach c : userTeam.getCoaches()) {
            Label cl = new Label("🧑‍💼  " + c.getName() + "   Age: " + c.getAge() +
                    "   Experience: " + c.getExperienceLevel());
            cl.setStyle("-fx-text-fill: #cfd8dc; -fx-font-size: 13px;");
            coachBox.getChildren().add(cl);
        }

        // Oyuncu tablosu
        Label playersTitle = new Label("PLAYERS");
        playersTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");

        TableView<Player> table = new TableView<>();
        table.setStyle("-fx-background-color: #151929; -fx-border-color: #1e2a3a;");
        table.getStyleClass().add("table-dark");
        table.setMinHeight(360);

        TableColumn<Player, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
        nameCol.setMinWidth(160);

        TableColumn<Player, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getAge())));
        ageCol.setMinWidth(60);

        TableColumn<Player, String> posCol = new TableColumn<>("Position");
        posCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPosition()));
        posCol.setMinWidth(100);

        TableColumn<Player, String> skillCol = new TableColumn<>("Skill");
        skillCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getSkillLevel())));
        skillCol.setMinWidth(80);

        TableColumn<Player, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> {
            Player p = d.getValue();
            if (p.isInjured())
                return new javafx.beans.property.SimpleStringProperty("🔴 Injured (" + p.getMatchesUntilFit() + " matches)");
            return new javafx.beans.property.SimpleStringProperty("🟢 Available");
        });
        statusCol.setMinWidth(160);

        table.getColumns().addAll(nameCol, ageCol, posCol, skillCol, statusCol);
        table.getItems().addAll(userTeam.getPlayers());

        pane.getChildren().addAll(title, coachBox, playersTitle, table);

        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    // ── STANDINGS ───────────────────────────────────────────
    private ScrollPane buildStandings() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #0d0f1a;");

        Label title = new Label("League Standings");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        League league = gm.getLeague();
        List<Team> standings = league.getStandings();
        Team userTeam = gm.getUserTeam();

        VBox table = new VBox(6);
        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(8, 16, 8, 16));
        header.setStyle("-fx-background-color: #1a2436; -fx-background-radius: 8;");
        addTableCell(header, "#", 40, "#4fc3f7");
        addTableCell(header, "Team", 300, "#4fc3f7");
        addTableCell(header, "Pts", 80, "#4fc3f7");
        table.getChildren().add(header);

        for (int i = 0; i < standings.size(); i++) {
            Team t = standings.get(i);
            boolean isUser = t.getName().equals(userTeam.getName());
            HBox row = new HBox();
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle(isUser
                ? "-fx-background-color: #0d2240; -fx-background-radius: 8; -fx-border-color: #4fc3f7; -fx-border-radius: 8; -fx-border-width: 1;"
                : (i % 2 == 0 ? "-fx-background-color: #151929;" : "-fx-background-color: #111524;") + " -fx-background-radius: 8;");

            String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : String.valueOf(i + 1);
            addTableCell(row, medal, 40, "#eceff1");
            addTableCell(row, (isUser ? "★ " : "") + t.getName(), 300, isUser ? "#4fc3f7" : "#eceff1");
            addTableCell(row, String.valueOf(t.getPoints()), 80, isUser ? "#4fc3f7" : "#eceff1");
            table.getChildren().add(row);
        }

        pane.getChildren().addAll(title, table);

        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    private void addTableCell(HBox row, String text, double width, String color) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px;");
        row.getChildren().add(lbl);
    }

    // ── FIXTURES ────────────────────────────────────────────
    private ScrollPane buildFixtures() {
        VBox pane = new VBox(12);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #0d0f1a;");

        Label title = new Label("Fixtures");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        League league = gm.getLeague();
        List<Match> fixtures = league.getFixtures();

        for (int i = 0; i < fixtures.size(); i++) {
            Match m = fixtures.get(i);
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 20, 12, 20));
            row.setStyle(m.isPlayed()
                ? "-fx-background-color: #0e1520; -fx-background-radius: 8; -fx-border-color: #1a2e1a; -fx-border-radius: 8; -fx-border-width: 1;"
                : "-fx-background-color: #151929; -fx-background-radius: 8; -fx-border-color: #1e2a3a; -fx-border-radius: 8; -fx-border-width: 1;");

            Label weekLbl = new Label("W" + (i + 1));
            weekLbl.setStyle("-fx-text-fill: #4fc3f7; -fx-font-size: 12px; -fx-font-weight: bold; -fx-min-width: 40;");

            Label matchLbl = new Label(m.getHomeTeam().getName() + "  vs  " + m.getAwayTeam().getName());
            matchLbl.setStyle("-fx-text-fill: #eceff1; -fx-font-size: 14px; -fx-min-width: 340;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label resultLbl;
            if (m.isPlayed()) {
                resultLbl = new Label(m.getHomeScore() + " - " + m.getAwayScore());
                resultLbl.setStyle("-fx-text-fill: #a5d6a7; -fx-font-size: 16px; -fx-font-weight: bold;");
            } else {
                resultLbl = new Label("Upcoming");
                resultLbl.setStyle("-fx-text-fill: #37474f; -fx-font-size: 13px;");
            }

            row.getChildren().addAll(weekLbl, matchLbl, spacer, resultLbl);
            pane.getChildren().add(row);
        }

        if (fixtures.isEmpty()) {
            Label noData = new Label("No fixtures generated yet.");
            noData.setStyle("-fx-text-fill: #546e7a;");
            pane.getChildren().add(noData);
        }

        ScrollPane sp = new ScrollPane(new VBox(title, new VBox(12, pane.getChildren().toArray(new javafx.scene.Node[0]))));
        // Daha basit:
        pane.getChildren().add(0, title);
        ScrollPane sp2 = new ScrollPane(pane);
        sp2.setFitToWidth(true);
        sp2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp2;
    }

    // ── ACTIONS ─────────────────────────────────────────────
    private void doTrain() {
        gm.trainUserTeam();
        activeTab = "overview";
        showAlert("Training Complete",
                "Your team completed today's training session.\nAll available players improved their skills!");
        showTab("overview");
    }

    private void doPlay() {
        if (gm.isSeasonFinished()) {
            SceneManager.showSeasonEnd();
            return;
        }

        League league = gm.getLeague();
        Match nextMatch = getNextMatch(league);
        if (nextMatch == null) {
            SceneManager.showSeasonEnd();
            return;
        }

        gm.playNextWeek();
        SceneManager.showMatchResult(nextMatch);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ── HELPERS ─────────────────────────────────────────────
    private Match getNextMatch(League league) {
        if (league == null) return null;
        List<Match> fixtures = league.getFixtures();
        int week = league.getCurrentWeek();
        if (week >= fixtures.size()) return null;
        return fixtures.get(week);
    }

    private String getLeaguePosition(Team userTeam, League league) {
        if (league == null) return "-";
        List<Team> standings = league.getStandings();
        for (int i = 0; i < standings.size(); i++) {
            if (standings.get(i).getName().equals(userTeam.getName())) {
                return (i + 1) + " / " + standings.size();
            }
        }
        return "-";
    }

    public BorderPane getRoot() { return root; }
}
