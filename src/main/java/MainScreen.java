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
                fmNavBtn("▶",  "Play Match",    "play")
        );

        Button saveBtn = fmNavBtn("💾", "Save Game", "save");
        saveBtn.setOnAction(e -> {
            gm.saveGame("savegame.dat");
            showAlert("💾 Saved", "Oyunun başarıyla kaydedildi!");
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

        sb.getChildren().addAll(logo, teamInfo, nav, spacer, weekBox);
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
                statCard("POSITION", getPos(userTeam), "#a78bfa")
        );

        HBox cols = new HBox(16);
        VBox nextMatch = buildNextMatchCard();
        VBox tactic = buildTacticCard();
        HBox.setHgrow(nextMatch, Priority.ALWAYS);
        HBox.setHgrow(tactic, Priority.ALWAYS);
        cols.getChildren().addAll(nextMatch, tactic);

        VBox recentBox = buildRecentResults();

        pane.getChildren().addAll(hdr, statRow, cols, recentBox);

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
            playBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 0 10 0; -fx-border-width: 0;");
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
            tb.setOnAction(e -> { userTeam.setTactic(t); showTab("dashboard"); });
            btns.getChildren().add(tb);
        }

        Button trainBtn = new Button("🏃  Train Team");
        trainBtn.setMaxWidth(Double.MAX_VALUE);
        trainBtn.setStyle("-fx-background-color: #14532d; -fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 9 0 9 0; -fx-border-width: 0;");
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

        Label playersTitle = new Label("PLAYERS");
        playersTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        VBox playerRows = new VBox(6);
        HBox hdr = new HBox(0);
        hdr.setPadding(new Insets(8, 16, 8, 16));
        hdr.setStyle("-fx-background-color: #0d1222; -fx-background-radius: 8;");
        addCol(hdr, "NAME", 220, "#334155");
        addCol(hdr, "AGE", 60, "#334155");
        addCol(hdr, "POSITION", 120, "#334155");
        addCol(hdr, "SKILL", 80, "#334155");
        addCol(hdr, "STATUS", 140, "#334155");
        playerRows.getChildren().add(hdr);

        for (int i = 0; i < userTeam.getPlayers().size(); i++) {
            Player p = userTeam.getPlayers().get(i);
            HBox row = new HBox(0);
            row.setPadding(new Insets(11, 16, 11, 16));
            row.setStyle(i % 2 == 0
                    ? "-fx-background-color: #111827; -fx-background-radius: 8;"
                    : "-fx-background-color: #0e1520; -fx-background-radius: 8;");
            addCol(row, p.getName(), 220, "#94a3b8");
            addCol(row, String.valueOf(p.getAge()), 60, "#64748b");
            addCol(row, p.getPosition(), 120, "#64748b");
            addCol(row, String.valueOf(p.getSkillLevel()), 80, "#3b82f6");

            Label statusLbl = new Label(p.isInjured() ? "Injured (" + p.getMatchesUntilFit() + ")" : "Available");
            statusLbl.getStyleClass().add(p.isInjured() ? "badge-injured" : "badge-fit");
            HBox statusWrap = new HBox(statusLbl);
            statusWrap.setMinWidth(140);
            row.getChildren().add(statusWrap);
            playerRows.getChildren().add(row);
        }

        pane.getChildren().addAll(title, coachCard, playersTitle, playerRows);
        ScrollPane sp = new ScrollPane(pane);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return sp;
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
        addCol(hdr, "CLUB", 320, "#334155");
        addCol(hdr, "PTS", 80, "#334155");
        table.getChildren().add(hdr);

        String[] medals = {"🥇", "🥈", "🥉", "4"};
        for (int i = 0; i < standings.size(); i++) {
            Team t = standings.get(i);
            boolean isUser = t.getName().equals(userTeam.getName());
            HBox row = new HBox(0);
            row.setPadding(new Insets(13, 20, 13, 20));
            row.setStyle(isUser
                    ? "-fx-background-color: #0f1e35; -fx-background-radius: 10; -fx-border-color: #3b82f6; -fx-border-radius: 10; -fx-border-width: 1;"
                    : (i%2==0 ? "-fx-background-color: #111827;" : "-fx-background-color: #0e1520;") + " -fx-background-radius: 10;");

            Label medal = new Label(i < 3 ? medals[i] : medals[3]);
            medal.setMinWidth(40);
            Label nameL = new Label((isUser ? "★  " : "    ") + t.getName());
            nameL.setMinWidth(320);
            nameL.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isUser ? "#60a5fa" : "#94a3b8") + "; -fx-font-weight: " + (isUser ? "bold" : "normal") + ";");
            Label ptsL = new Label(String.valueOf(t.getPoints()));
            ptsL.setMinWidth(80);
            ptsL.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + (isUser ? "#3b82f6" : "#475569") + ";");
            row.getChildren().addAll(medal, nameL, ptsL);
            table.getChildren().add(row);
        }

        pane.getChildren().addAll(title, table);
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
        gm.trainUserTeam();
        activeTab = "dashboard";
        showAlert("✅ Training Complete", "All available players improved their skills!");
        showTab("dashboard");
    }

    private void doPlay() {
        if (gm.isSeasonFinished()) { SceneManager.showSeasonEnd(); return; }
        Match match = getNextMatch(gm.getLeague());
        if (match == null) { SceneManager.showSeasonEnd(); return; }

        Team userTeam = gm.getUserTeam();
        boolean isUserMatch = match.getHomeTeam().getName().equals(userTeam.getName()) ||
                match.getAwayTeam().getName().equals(userTeam.getName());

        if (isUserMatch) {
            while (!match.isPlayed()) {
                match.playNextPeriod();

                if (!match.isPlayed()) {
                    String periodName = match.getSport().getPeriodName();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(periodName + " " + (match.getCurrentPeriod() - 1) + " Ended");
                    alert.setHeaderText(match.getHomeTeam().getName() + "  " + match.getHomeScore() + " - " + match.getAwayScore() + "  " + match.getAwayTeam().getName());
                    alert.setContentText(periodName + " finished! Change your tactic for the next " + periodName.toLowerCase() + "?");

                    ButtonType btnAttack = new ButtonType("Attacking");
                    ButtonType btnBalanced = new ButtonType("Balanced");
                    ButtonType btnDefensive = new ButtonType("Defensive");

                    alert.getButtonTypes().setAll(btnAttack, btnBalanced, btnDefensive);

                    java.util.Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent()) {
                        if (result.get() == btnAttack) userTeam.setTactic("Attacking");
                        else if (result.get() == btnDefensive) userTeam.setTactic("Defensive");
                        else userTeam.setTactic("Balanced");
                    }
                }
            }
        }

        gm.playNextWeek();
        SceneManager.showMatchResult(match);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private Match getNextMatch(League league) {
        if (league == null) return null;
        List<Match> f = league.getFixtures();
        int w = league.getCurrentWeek();
        return w < f.size() ? f.get(w) : null;
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