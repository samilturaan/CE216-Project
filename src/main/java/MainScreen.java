import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.List;

public class MainScreen {
    private BorderPane root;
    private GameManager gm;
    private VBox contentArea;
    private String activeTab = "dashboard";
    private String selectedFocusResult = null;

    public MainScreen() {
        gm = SceneManager.getGameManager();
        root = new BorderPane();
        root.setStyle("-fx-background-color: #080c18;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));
        root.setLeft(buildSidebar());
        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: #080c18;");
        root.setCenter(contentArea);
        showTab("dashboard");
    }

    private VBox buildSidebar() {
        VBox sb = new VBox(0);
        sb.setStyle("-fx-background-color: #060a14; -fx-border-color: #1e2d45; -fx-border-width: 0 1 0 0; -fx-min-width: 200; -fx-max-width: 200;");

        // Logo
        HBox logo = new HBox(10);
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setPadding(new Insets(16, 14, 16, 14));
        logo.setStyle("-fx-border-color: #1e2d45; -fx-border-width: 0 0 1 0; -fx-background-color: #080c18;");
        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(28, 28);
        iconBg.setArcWidth(6); iconBg.setArcHeight(6);
        iconBg.setFill(Color.web("#3b82f6"));
        Label iconL = new Label("⚽");
        iconL.setStyle("-fx-font-size: 14px;");
        iconBox.getChildren().addAll(iconBg, iconL);
        VBox logoText = new VBox(0);
        Label logoTop = new Label("SPORTS");
        logoTop.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label logoBot = new Label("MANAGER");
        logoBot.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #3b82f6;");
        logoText.getChildren().addAll(logoTop, logoBot);
        logo.getChildren().addAll(iconBox, logoText);

        // Team info
        HBox teamInfo = new HBox(10);
        teamInfo.setAlignment(Pos.CENTER_LEFT);
        teamInfo.setPadding(new Insets(10, 14, 10, 14));
        teamInfo.setStyle("-fx-border-color: #1e2d45; -fx-border-width: 0 0 1 0; -fx-background-color: #080c18;");
        Label teamCircle = new Label(gm.getUserTeam() != null ? gm.getUserTeam().getName().substring(0,1).toUpperCase() : "?");
        teamCircle.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-min-width: 32; -fx-min-height: 32; -fx-alignment: center;");
        VBox teamText = new VBox(1);
        Label teamNameL = new Label(gm.getUserTeam() != null ? gm.getUserTeam().getName() : "");
        teamNameL.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        ISport sport = gm.getSelectedSport();
        String sportEmoji = sport != null && "Football".equals(sport.getSportName()) ? "⚽" : "🏐";
        Label sportL = new Label(sportEmoji + " " + (sport != null ? sport.getSportName() : ""));
        sportL.setStyle("-fx-font-size: 11px; -fx-text-fill: #334155;");
        teamText.getChildren().addAll(teamNameL, sportL);
        teamInfo.getChildren().addAll(teamCircle, teamText);

        // Nav items
        VBox nav = new VBox(0);
        nav.setPadding(new Insets(6, 0, 6, 0));
        nav.getChildren().addAll(
                fmNavBtn("🏠", "Home",          "dashboard"),
                fmNavBtn("👥", "Squad",         "squad"),
                fmNavBtn("🏆", "Standings",     "standings"),
                fmNavBtn("📅", "Schedule",      "fixtures"),
                fmNavBtn("🎯", "Tactics",       "dashboard"),
                fmNavBtn("🏃", "Training",      "train"),
                fmNavBtn("▶",  "Play Match",    "play"),
                fmNavBtn("⚙",  "Settings",      "settings")
        );

        Button saveBtn = fmNavBtn("💾", "Save Game", "save");
        saveBtn.setOnAction(e -> {
            gm.saveGame("savegame.dat");
            showTechAlert("💾", "DATA SAVED", "Oyunun başarıyla kaydedildi!", "#3b82f6");
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Week indicator bottom
        League league = gm.getLeague();
        int week = league != null ? league.getCurrentWeek() : 0;
        int total = league != null ? league.getFixtures().size() : 0;
        HBox weekBox = new HBox();
        weekBox.setPadding(new Insets(10, 14, 10, 14));
        weekBox.setStyle("-fx-border-color: #1e2d45; -fx-border-width: 1 0 0 0;");
        Label weekLbl = new Label("Week " + week + " / " + total);
        weekLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #1e2d45;");
        weekBox.getChildren().add(weekLbl);

        sb.getChildren().addAll(logo, teamInfo, nav, spacer, saveBtn, weekBox);
        return sb;
    }

    private Button fmNavBtn(String icon, String label, String tab) {
        Button btn = new Button(icon + "   " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        boolean active = tab.equals(activeTab) && !tab.equals("train") && !tab.equals("play");
        btn.setStyle(active
                ? "-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 0 0 16; -fx-alignment: center-left; -fx-border-width: 0; -fx-background-radius: 0; -fx-border-color: #3b82f6; -fx-border-width: 0 0 0 3;"
                : "-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 0 0 0 19; -fx-alignment: center-left; -fx-border-width: 0; -fx-background-radius: 0;");
        btn.setOnMouseEntered(e -> {
            if (!tab.equals(activeTab))
                btn.setStyle("-fx-background-color: #111827; -fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 0 0 0 19; -fx-alignment: center-left; -fx-border-width: 0; -fx-background-radius: 0;");
        });
        btn.setOnMouseExited(e -> {
            if (!tab.equals(activeTab))
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 0 0 0 19; -fx-alignment: center-left; -fx-border-width: 0; -fx-background-radius: 0;");
        });
        btn.setUserData(tab);
        btn.setOnAction(e -> {
            activeTab = tab;
            refreshSidebar();
            showTab(tab);
        });
        return btn;
    }


    private void refreshSidebar() {
        root.setLeft(buildSidebar());
    }

    private void showTab(String tab) {
        contentArea.getChildren().clear();
        switch (tab) {
            case "dashboard" -> contentArea.getChildren().add(buildDashboard());
            case "squad"     -> contentArea.getChildren().add(buildSquad());
            case "standings" -> contentArea.getChildren().add(buildStandings());
            case "fixtures"  -> contentArea.getChildren().add(buildFixtures());
            case "train"     -> doTrain();
            case "play"      -> doPlay();
            case "settings"  -> contentArea.getChildren().add(buildSettings());
            case "save"      -> {
                gm.saveGame("savegame.dat");
                showTechAlert("💾", "DATA SAVED", "Game progress has been securely written to disk.", "#3b82f6");
                activeTab = "dashboard";
                contentArea.getChildren().add(buildDashboard());
            }
        }
    }

    private ScrollPane buildDashboard() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #080c18;");

        HBox hdr = new HBox();
        hdr.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Team userTeam = gm.getUserTeam();
        Label ptsBadge = new Label(userTeam.getPoints() + " pts");
        ptsBadge.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 14 6 14;");
        hdr.getChildren().addAll(title, sp, ptsBadge);

        HBox statRow = new HBox(14);
        statRow.getChildren().addAll(
                statCard("POINTS", String.valueOf(userTeam.getPoints()), "#3b82f6"),
                statCard("SQUAD", String.valueOf(userTeam.getPlayers().size()), "#22c55e"),
                statCard("AVAILABLE", String.valueOf(userTeam.getAvailablePlayers().size()), "#f59e0b"),
                statCard("POSITION", getPos(userTeam), "#a78bfa"),
                statCard("FORM", userTeam.getFormString(), "#60a5fa")
        );

        HBox cols = new HBox(16);
        VBox nextMatch = buildNextMatchCard();
        VBox tactic = buildTacticCard();
        HBox.setHgrow(nextMatch, Priority.ALWAYS);
        HBox.setHgrow(tactic, Priority.ALWAYS);
        cols.getChildren().addAll(nextMatch, tactic);

        HBox insightRow = new HBox(16);
        VBox formBox = buildFormGraphCard(userTeam);
        VBox topScorerBox = buildTopScorerCard(userTeam);
        HBox.setHgrow(formBox, Priority.ALWAYS);
        HBox.setHgrow(topScorerBox, Priority.ALWAYS);
        insightRow.getChildren().addAll(formBox, topScorerBox);

        VBox recentBox = buildRecentResults();

        pane.getChildren().addAll(hdr, statRow, cols, insightRow, recentBox);

        ScrollPane sp2 = new ScrollPane(pane);
        sp2.setFitToWidth(true);
        sp2.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp2;
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setMinWidth(160);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 12; -fx-border-color: #1e2d45; -fx-border-radius: 12; -fx-border-width: 1;");
        Label valLbl = new Label(value);
        valLbl.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lblLbl = new Label(label);
        lblLbl.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        card.getChildren().addAll(valLbl, lblLbl);
        return card;
    }

    private VBox buildFormGraphCard(Team team) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        Label title = new Label("LAST 5 MATCHES");
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        HBox formRow = new HBox(10);
        formRow.setAlignment(Pos.CENTER_LEFT);

        if (team.getRecentForm().isEmpty()) {
            Label empty = new Label("No matches played yet");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #475569;");
            formRow.getChildren().add(empty);
        } else {
            for (String result : team.getRecentForm()) {
                Label badge = new Label(result);
                badge.setMinSize(36, 36);
                badge.setAlignment(Pos.CENTER);
                badge.setStyle(getFormBadgeStyle(result));
                formRow.getChildren().add(badge);
            }
        }

        Label explanation = new Label("Form updates after each league match and affects team momentum.");
        explanation.setWrapText(true);
        explanation.setStyle("-fx-font-size: 12px; -fx-text-fill: #475569;");

        card.getChildren().addAll(title, formRow, explanation);
        return card;
    }

    private String getFormBadgeStyle(String result) {
        if (result.equals("W")) {
            return "-fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-background-radius: 18;";
        }
        if (result.equals("D")) {
            return "-fx-background-color: #422006; -fx-text-fill: #f59e0b; -fx-font-weight: bold; -fx-background-radius: 18;";
        }
        return "-fx-background-color: #450a0a; -fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-background-radius: 18;";
    }

    private VBox buildTopScorerCard(Team team) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        Label title = new Label("TEAM TOP SCORER");
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        Player topScorer = team.getTopScorer();
        if (topScorer == null) {
            Label empty = new Label("No players available");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #475569;");
            card.getChildren().addAll(title, empty);
            return card;
        }

        HBox playerLine = new HBox(12);
        playerLine.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle circle = new Circle(24);
        circle.setFill(Color.web("#1e3a5f"));
        Label initials = new Label(getInitials(topScorer.getName()));
        initials.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        avatar.getChildren().addAll(circle, initials);

        VBox textBox = new VBox(2);
        Label name = new Label(topScorer.getName());
        name.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label details = new Label(topScorer.getPosition() + " • " + topScorer.getGoalsScored() + " goals • " + topScorer.getMatchesPlayed() + " apps");
        details.setStyle("-fx-font-size: 12px; -fx-text-fill: #60a5fa;");
        textBox.getChildren().addAll(name, details);

        playerLine.getChildren().addAll(avatar, textBox);

        Label note = new Label("Goals are tracked from the live match engine.");
        note.setWrapText(true);
        note.setStyle("-fx-font-size: 12px; -fx-text-fill: #475569;");

        card.getChildren().addAll(title, playerLine, note);
        return card;
    }

    private VBox buildNextMatchCard() {
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        Label cardTitle = new Label("NEXT MATCH");
        cardTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        League league = gm.getLeague();
        Match next = getNextMatch(league);

        if (next != null) {
            HBox teams = new HBox(16);
            teams.setAlignment(Pos.CENTER);

            VBox home = new VBox(6);
            home.setAlignment(Pos.CENTER);
            Label homeEmoji = new Label("🏟");
            homeEmoji.setStyle("-fx-font-size: 36px;");
            Label homeName = new Label(next.getHomeTeam().getName());
            homeName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
            home.getChildren().addAll(homeEmoji, homeName);

            Label vsLbl = new Label("VS");
            vsLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e2d45;");

            VBox away = new VBox(6);
            away.setAlignment(Pos.CENTER);
            Label awayEmoji = new Label("🏟");
            awayEmoji.setStyle("-fx-font-size: 36px;");
            Label awayName = new Label(next.getAwayTeam().getName());
            awayName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
            away.getChildren().addAll(awayEmoji, awayName);

            teams.getChildren().addAll(home, vsLbl, away);

            Label weekLbl = new Label("WEEK " + (league.getCurrentWeek() + 1) + " OF " + league.getFixtures().size());
            weekLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #1e2d45; -fx-alignment: center;");
            weekLbl.setMaxWidth(Double.MAX_VALUE);
            weekLbl.setAlignment(Pos.CENTER);

            Button playBtn = new Button("▶  PLAY MATCH");
            playBtn.setMaxWidth(Double.MAX_VALUE);
            boolean canPlay = !gm.isSeasonFinished();
            playBtn.setDisable(!canPlay);
            playBtn.setStyle(canPlay
                    ? "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 0 10 0; -fx-border-width: 0;"
                    : "-fx-background-color: #1f2937; -fx-text-fill: #64748b; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 10 0 10 0; -fx-border-width: 0;");
            playBtn.setOnAction(e -> doPlay());

            card.getChildren().addAll(cardTitle, teams, weekLbl, playBtn);
        } else {
            Label done = new Label("Season Complete!");
            done.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #22c55e;");
            card.getChildren().addAll(cardTitle, done);
        }
        return card;
    }

    private VBox buildTacticCard() {
        Team userTeam = gm.getUserTeam();
        VBox card = new VBox(14);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        Label cardTitle = new Label("TACTICS");
        cardTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        Label current = new Label(userTeam.getTactic().toUpperCase());
        current.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #3b82f6;");

        VBox btns = new VBox(8);
        for (String t : new String[]{"Attacking", "Balanced", "Defensive"}) {
            Button tb = new Button(t);
            tb.setMaxWidth(Double.MAX_VALUE);
            boolean active = t.equals(userTeam.getTactic());
            tb.setStyle(active
                    ? "-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 9 0 9 0; -fx-border-width: 0;"
                    : "-fx-background-color: #0d1222; -fx-text-fill: #475569; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 9 0 9 0; -fx-border-width: 0;");
            tb.setDisable(getNextUserMatch(gm.getLeague()) == null);
            tb.setOnAction(e -> { userTeam.setTactic(t); showTab("dashboard"); });
            btns.getChildren().add(tb);
        }

        Button trainBtn = new Button("🏃  Train Team (" + gm.getTrainingsThisWeek() + "/" + gm.getMaxTrainingsPerWeek() + ")");
        trainBtn.setMaxWidth(Double.MAX_VALUE);
        boolean canTrain = gm.canTrainUserTeam() && getNextUserMatch(gm.getLeague()) != null;
        trainBtn.setDisable(!canTrain);
        trainBtn.setStyle(canTrain
                ? "-fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 9 0 9 0; -fx-border-width: 0;"
                : "-fx-background-color: #1f2937; -fx-text-fill: #64748b; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 9 0 9 0; -fx-border-width: 0;");
        trainBtn.setOnAction(e -> doTrain());

        card.getChildren().addAll(cardTitle, current, btns, trainBtn);
        return card;
    }

    private VBox buildRecentResults() {
        VBox box = new VBox(10);
        Label title = new Label("RECENT RESULTS");
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        box.getChildren().add(title);

        League league = gm.getLeague();
        Team userTeam = gm.getUserTeam();
        List<Match> fixtures = league.getFixtures();
        int shown = 0;
        for (int i = fixtures.size()-1; i >= 0 && shown < 3; i--) {
            Match m = fixtures.get(i);
            if (!m.isPlayed()) continue;
            boolean userHome = m.getHomeTeam().getName().equals(userTeam.getName());
            boolean userAway = m.getAwayTeam().getName().equals(userTeam.getName());
            if (!userHome && !userAway) continue;

            int userScore = userHome ? m.getHomeScore() : m.getAwayScore();
            int oppScore  = userHome ? m.getAwayScore()  : m.getHomeScore();
            String opp    = userHome ? m.getAwayTeam().getName() : m.getHomeTeam().getName();

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #111827; -fx-background-radius: 10; -fx-border-color: #1e2d45; -fx-border-radius: 10; -fx-border-width: 1;");

            String resultClass = userScore > oppScore ? "badge-win" : userScore < oppScore ? "badge-loss" : "badge-draw";
            String resultText  = userScore > oppScore ? "W" : userScore < oppScore ? "L" : "D";
            Label badge = new Label(resultText);
            badge.getStyleClass().add(resultClass);

            Label matchLbl = new Label(userTeam.getName() + "  " + userScore + " — " + oppScore + "  " + opp);
            matchLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

            row.getChildren().addAll(badge, matchLbl);
            box.getChildren().add(row);
            shown++;
        }
        if (shown == 0) {
            Label noResults = new Label("No matches played yet.");
            noResults.setStyle("-fx-font-size: 13px; -fx-text-fill: #1e2d45;");
            box.getChildren().add(noResults);
        }
        return box;
    }

    private ScrollPane buildSquad() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #080c18;");

        Label title = new Label("Squad");
        title.getStyleClass().add("page-title");

        Team userTeam = gm.getUserTeam();

        VBox coachCard = new VBox(10);
        coachCard.setPadding(new Insets(16, 20, 16, 20));
        coachCard.setStyle("-fx-background-color: #111827; -fx-background-radius: 12; -fx-border-color: #1e2d45; -fx-border-radius: 12; -fx-border-width: 1;");
        Label coachTitle = new Label("COACHING STAFF");
        coachTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        coachCard.getChildren().add(coachTitle);
        for (Coach c : userTeam.getCoaches()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            Label nameL = new Label("🧑‍💼  " + c.getName());
            nameL.setStyle("-fx-font-size: 14px; -fx-text-fill: #f0f4ff;");
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label expL = new Label("Exp: " + c.getExperienceLevel());
            expL.setStyle("-fx-font-size: 12px; -fx-text-fill: #334155;");
            Label ageL = new Label("Age " + c.getAge());
            ageL.setStyle("-fx-font-size: 12px; -fx-text-fill: #1e2d45;");
            row.getChildren().addAll(nameL, sp, expL, ageL);
            coachCard.getChildren().add(row);
        }

        Label playersTitle = new Label("PLAYER CARDS");
        playersTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        FlowPane playerCards = new FlowPane();
        playerCards.setHgap(14);
        playerCards.setVgap(14);
        playerCards.setPrefWrapLength(900);

        for (Player p : userTeam.getPlayers()) {
            playerCards.getChildren().add(buildPlayerCard(p));
        }

        pane.getChildren().addAll(title, coachCard, playersTitle, playerCards);
        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    private VBox buildPlayerCard(Player player) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(220);
        card.setMinHeight(210);
        card.setStyle(player.isInjured()
                ? "-fx-background-color: #1f1115; -fx-background-radius: 14; -fx-border-color: #7f1d1d; -fx-border-radius: 14; -fx-border-width: 1;"
                : "-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = new StackPane();
        Circle avatarCircle = new Circle(24);
        avatarCircle.setFill(player.isInjured() ? Color.web("#7f1d1d") : Color.web("#1e3a5f"));
        Label initials = new Label(getInitials(player.getName()));
        initials.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        avatar.getChildren().addAll(avatarCircle, initials);

        VBox nameBox = new VBox(2);
        Label name = new Label(player.getName());
        name.setWrapText(true);
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label position = new Label(player.getPosition());
        position.setStyle("-fx-font-size: 11px; -fx-text-fill: #60a5fa;");
        nameBox.getChildren().addAll(name, position);
        header.getChildren().addAll(avatar, nameBox);

        HBox ratingRow = new HBox(12);
        ratingRow.setAlignment(Pos.CENTER_LEFT);
        ratingRow.getChildren().addAll(
                miniStat("OVR", String.valueOf(player.getSkillLevel()), "#3b82f6"),
                miniStat("AGE", String.valueOf(player.getAge()), "#94a3b8"),
                miniStat("GLS", String.valueOf(player.getGoalsScored()), "#22c55e")
        );

        VBox bars = new VBox(8);
        bars.getChildren().addAll(
                progressLine("STAMINA", player.getStamina(), getStaminaColor(player.getStamina())),
                progressLine("MORALE", player.getMorale(), "#a78bfa")
        );

        Label status = new Label(player.isInjured() ? "🚑 Injured for " + player.getMatchesUntilFit() + " match(es)" : "✅ Available");
        status.setStyle(player.isInjured()
                ? "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #ef4444;"
                : "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #22c55e;");

        card.getChildren().addAll(header, ratingRow, bars, status);
        return card;
    }

    private VBox miniStat(String title, String value, String color) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(52);
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        box.getChildren().addAll(valueLabel, titleLabel);
        return box;
    }

    private VBox progressLine(String label, int value, String color) {
        VBox box = new VBox(4);
        HBox top = new HBox();
        Label title = new Label(label);
        title.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label valueLabel = new Label(value + "%");
        valueLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748b;");
        top.getChildren().addAll(title, spacer, valueLabel);

        ProgressBar bar = new ProgressBar(value / 100.0);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setPrefHeight(8);
        bar.setStyle("-fx-accent: " + color + "; -fx-control-inner-background: #0d1222;");

        box.getChildren().addAll(top, bar);
        return box;
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "?";
        }
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
    }

    private String getStaminaColor(int stamina) {
        if (stamina >= 70) {
            return "#22c55e";
        }
        if (stamina >= 35) {
            return "#f59e0b";
        }
        return "#ef4444";
    }

    private void addCol(HBox row, String text, double width, String color) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width); lbl.setMaxWidth(width);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px;");
        row.getChildren().add(lbl);
    }

    private ScrollPane buildStandings() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #080c18;");

        Label title = new Label("Standings");
        title.getStyleClass().add("page-title");

        League league = gm.getLeague();
        List<Team> standings = league.getStandings();
        Team userTeam = gm.getUserTeam();

        VBox table = new VBox(6);
        HBox hdr = new HBox(0);
        hdr.setPadding(new Insets(8, 20, 8, 20));
        hdr.setStyle("-fx-background-color: #0d1222; -fx-background-radius: 8;");
        addCol(hdr, "#", 40, "#334155");
        addCol(hdr, "CLUB", 260, "#334155");
        addCol(hdr, "W", 45, "#334155");
        addCol(hdr, "D", 45, "#334155");
        addCol(hdr, "L", 45, "#334155");
        addCol(hdr, "GF", 55, "#334155");
        addCol(hdr, "GA", 55, "#334155");
        addCol(hdr, "GD", 55, "#334155");
        addCol(hdr, "PTS", 70, "#334155");
        table.getChildren().add(hdr);

        String[] medals = {"🥇", "🥈", "🥉", "4"};
        for (int i = 0; i < standings.size(); i++) {
            Team t = standings.get(i);
            boolean isUser = t.getName().equals(userTeam.getName());
            HBox row = new HBox(0);
            row.setPadding(new Insets(13, 20, 13, 20));
            row.setStyle(isUser
                    ? "-fx-background-color: #0f1e35; -fx-background-radius: 10; -fx-border-color: #3b82f6; -fx-border-radius: 10; -fx-border-width: 1;"
                    : (i % 2 == 0 ? "-fx-background-color: #111827;" : "-fx-background-color: #0e1520;") + " -fx-background-radius: 10;");

            Label medal = new Label(i < 3 ? medals[i] : medals[3]);
            medal.setMinWidth(40);

            Label nameL = new Label((isUser ? "★  " : "    ") + t.getName());
            nameL.setMinWidth(260);
            nameL.setMaxWidth(260);
            nameL.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isUser ? "#60a5fa" : "#94a3b8") + "; -fx-font-weight: " + (isUser ? "bold" : "normal") + ";");

            Label winsL = tableValue(String.valueOf(t.getWins()), 45, isUser);
            Label drawsL = tableValue(String.valueOf(t.getDraws()), 45, isUser);
            Label lossesL = tableValue(String.valueOf(t.getLosses()), 45, isUser);
            Label gfL = tableValue(String.valueOf(t.getGoalsFor()), 55, isUser);
            Label gaL = tableValue(String.valueOf(t.getGoalsAgainst()), 55, isUser);
            Label gdL = tableValue(String.valueOf(t.getGoalDifference()), 55, isUser);

            Label ptsL = new Label(String.valueOf(t.getPoints()));
            ptsL.setMinWidth(70);
            ptsL.setMaxWidth(70);
            ptsL.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + (isUser ? "#3b82f6" : "#475569") + ";");

            row.getChildren().addAll(medal, nameL, winsL, drawsL, lossesL, gfL, gaL, gdL, ptsL);
            table.getChildren().add(row);
        }

        pane.getChildren().addAll(title, table);
        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    private Label tableValue(String text, double width, boolean isUser) {
        Label label = new Label(text);
        label.setMinWidth(width);
        label.setMaxWidth(width);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: " + (isUser ? "#60a5fa" : "#64748b") + ";");
        return label;
    }
    private ScrollPane buildSettings() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #080c18;");

        Label title = new Label("Settings");
        title.getStyleClass().add("page-title");

        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setMaxWidth(600);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        // GÖRÜNTÜ AYARLARI
        Label displayLabel = new Label("DISPLAY SETTINGS");
        displayLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        Button fullscreenBtn = new Button("🖥  Toggle Full Screen");
        fullscreenBtn.setMaxWidth(Double.MAX_VALUE);
        fullscreenBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: #60a5fa; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 12 20 12 20; -fx-border-width: 0;");
        fullscreenBtn.setOnAction(e -> {
            javafx.stage.Stage stage = (javafx.stage.Stage) root.getScene().getWindow();
            stage.setFullScreen(!stage.isFullScreen());
        });

        Region divider = new Region();
        divider.setMinHeight(1); divider.setMaxHeight(1);
        divider.setStyle("-fx-background-color: #1e2d45; -fx-margin: 10 0 10 0;");

        Label gameLabel = new Label("GAME SETTINGS");
        gameLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        Button saveGameBtn = new Button("💾  Save Game");
        saveGameBtn.setMaxWidth(Double.MAX_VALUE);
        saveGameBtn.setStyle("-fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 12 20 12 20; -fx-border-width: 0;");
        saveGameBtn.setOnAction(e -> {
            gm.saveGame("savegame.dat");
            showTechAlert("💾", "DATA SAVED", "Game progress has been saved succesfully!", "#3b82f6");
        });

        Button exitBtn = new Button("🚪  Exit to Desktop");
        exitBtn.setMaxWidth(Double.MAX_VALUE);
        exitBtn.setStyle("-fx-background-color: #7f1d1d; -fx-text-fill: #fca5a5; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 12 20 12 20; -fx-border-width: 0;");
        exitBtn.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Game");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Any unsaved progress will be lost.");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    javafx.application.Platform.exit();
                    System.exit(0);
                }
            });
        });

        card.getChildren().addAll(displayLabel, fullscreenBtn, divider, gameLabel, saveGameBtn, exitBtn);
        pane.getChildren().addAll(title, card);

        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    private ScrollPane buildFixtures() {
        VBox pane = new VBox(8);
        pane.setPadding(new Insets(28));
        pane.setStyle("-fx-background-color: #080c18;");

        Label title = new Label("Fixtures");
        title.getStyleClass().add("page-title");
        pane.getChildren().add(title);

        League league = gm.getLeague();
        List<Match> fixtures = league.getFixtures();

        if (fixtures.isEmpty()) {
            Label noData = new Label("No fixtures available.");
            noData.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");
            pane.getChildren().add(noData);
        } else {
            for (int i = 0; i < fixtures.size(); i++) {
                Match m = fixtures.get(i);
                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(12, 20, 12, 20));
                row.setStyle(m.isPlayed()
                        ? "-fx-background-color: #0e1520; -fx-background-radius: 10; -fx-border-color: #111827; -fx-border-radius: 10; -fx-border-width: 1;"
                        : "-fx-background-color: #111827; -fx-background-radius: 10; -fx-border-color: #1e2d45; -fx-border-radius: 10; -fx-border-width: 1;");

                Label weekLbl = new Label("W" + (i + 1));
                weekLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155; -fx-min-width: 32;");

                Label matchLbl = new Label(m.getHomeTeam().getName() + "  vs  " + m.getAwayTeam().getName());
                matchLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (m.isPlayed() ? "#475569" : "#94a3b8") + ";");

                Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

                if (m.isPlayed()) {
                    Label score = new Label(m.getHomeScore() + " – " + m.getAwayScore());
                    score.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #22c55e;");
                    row.getChildren().addAll(weekLbl, matchLbl, spacer, score);
                } else {
                    Label upcoming = new Label("UPCOMING");
                    upcoming.getStyleClass().add("badge-upcoming");
                    row.getChildren().addAll(weekLbl, matchLbl, spacer, upcoming);
                }
                pane.getChildren().add(row);
            }
        }

        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
    }

    private void doTrain() {
        if (gm.isSeasonFinished() || getNextUserMatch(gm.getLeague()) == null) {
            activeTab = "standings";
            showTechAlert("🚫", "TRAINING OFFLINE", "Season concluded. Training facilities are closed.", "#ef4444");
            showTab("standings");
            return;
        }
        String type = showTechChoiceDialog();

        if (type == null) {
            showTab("dashboard");
            return;
        }
        boolean trained = gm.trainUserTeam(type);
        activeTab = "dashboard";

        if (trained) {
            String alertColor = type.equals("Stamina") ? "#22c55e" : "#a78bfa";

            showTechAlert("⚡", "SQUAD OPTIMIZED",
                    type.toUpperCase() + " focus sequence complete. Weekly limit: " +
                            gm.getTrainingsThisWeek() + "/" + gm.getMaxTrainingsPerWeek(),
                    alertColor);
        } else {

            showTechAlert("⚠", "CAPACITY REACHED",
                    "Maximum weekly sessions reached. Players require recovery.",
                    "#f59e0b");
        }
        showTab("dashboard");
    }

    private void doPlay() {
        if (gm.isSeasonFinished()) {
            SceneManager.showSeasonEnd();
            return;
        }

        Match match = getNextUserMatch(gm.getLeague());
        if (match == null) {
            SceneManager.showSeasonEnd();
            return;
        }

        SceneManager.showLiveMatch(match);
    }


    private void showTechAlert(String icon, String title, String message, String color) {
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        if (root.getScene() != null && root.getScene().getWindow() != null) {
            dialogStage.initOwner(root.getScene().getWindow());
        }

        dialogStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

        VBox pane = new VBox(15);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(30, 50, 30, 50));
        // Siberpunk tarzı neon kenarlıklı tasarım
        pane.setStyle("-fx-background-color: #0d1222; -fx-border-color: " + color + "; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12;");

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 40px;");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        Label msgLbl = new Label(message);
        msgLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        Button okBtn = new Button("ACKNOWLEDGE");
        okBtn.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + "; -fx-border-radius: 6; -fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 8 24 8 24;");

        okBtn.setOnMouseEntered(e -> okBtn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #0d1222; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 8 24 8 24;"));
        okBtn.setOnMouseExited(e -> okBtn.setStyle("-fx-background-color: transparent; -fx-border-color: " + color + "; -fx-border-radius: 6; -fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 8 24 8 24;"));

        okBtn.setOnAction(e -> dialogStage.close());

        pane.getChildren().addAll(iconLbl, titleLbl, msgLbl, okBtn);

        javafx.scene.Scene scene = new javafx.scene.Scene(pane);

        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
    private String showTechChoiceDialog() {
        selectedFocusResult = null; // Reset
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        if (root.getScene() != null && root.getScene().getWindow() != null) {
            dialogStage.initOwner(root.getScene().getWindow());
        }
        dialogStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

        VBox pane = new VBox(20);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(35, 50, 35, 50));
        pane.setStyle("-fx-background-color: #0d1222; -fx-border-color: #3b82f6; -fx-border-width: 2; -fx-border-radius: 15; -fx-background-radius: 15;");

        Label title = new Label("SELECT TRAINING PROTOCOL");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff; -fx-letter-spacing: 2px;");

        Label sub = new Label("Choose the primary focus for the current sequence:");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        HBox options = new HBox(15);
        options.setAlignment(Pos.CENTER);

        Button staminaBtn = new Button("PHYSICAL\n(STAMINA)");
        staminaBtn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        staminaBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #22c55e; -fx-border-radius: 8; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;");
        staminaBtn.setOnAction(e -> { selectedFocusResult = "Stamina"; dialogStage.close(); });

        Button moraleBtn = new Button("MENTAL\n(MORALE)");
        moraleBtn.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        moraleBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #a78bfa; -fx-border-radius: 8; -fx-text-fill: #a78bfa; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;");
        moraleBtn.setOnAction(e -> { selectedFocusResult = "Morale"; dialogStage.close(); });

        staminaBtn.setOnMouseEntered(e -> staminaBtn.setStyle("-fx-background-color: #22c55e22; -fx-border-color: #22c55e; -fx-border-radius: 8; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;"));
        staminaBtn.setOnMouseExited(e -> staminaBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #22c55e; -fx-border-radius: 8; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;"));
        moraleBtn.setOnMouseEntered(e -> moraleBtn.setStyle("-fx-background-color: #a78bfa22; -fx-border-color: #a78bfa; -fx-border-radius: 8; -fx-text-fill: #a78bfa; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;"));
        moraleBtn.setOnMouseExited(e -> moraleBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #a78bfa; -fx-border-radius: 8; -fx-text-fill: #a78bfa; -fx-font-weight: bold; -fx-padding: 15 25; -fx-cursor: hand;"));

        options.getChildren().addAll(staminaBtn, moraleBtn);

        Button cancelBtn = new Button("ABORT MISSION");
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-size: 10px; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> dialogStage.close());

        pane.getChildren().addAll(title, sub, options, cancelBtn);

        javafx.scene.Scene scene = new javafx.scene.Scene(pane);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return selectedFocusResult;
    }

    private Match getNextMatch(League league) {
        return getNextUserMatch(league);
    }

    private Match getNextUserMatch(League league) {
        if (league == null || gm.getUserTeam() == null) return null;

        Team userTeam = gm.getUserTeam();
        for (Match match : league.getFixtures()) {
            boolean userHome = match.getHomeTeam().getName().equals(userTeam.getName());
            boolean userAway = match.getAwayTeam().getName().equals(userTeam.getName());

            if (!match.isPlayed() && (userHome || userAway)) {
                return match;
            }
        }

        return null;
    }

    private String getPos(Team userTeam) {
        League league = gm.getLeague();
        if (league == null) return "-";
        List<Team> standings = league.getStandings();
        for (int i = 0; i < standings.size(); i++) {
            if (standings.get(i).getName().equals(userTeam.getName())) return (i+1) + "/" + standings.size();
        }
        return "-";
    }

    public BorderPane getRoot() { return root; }
}