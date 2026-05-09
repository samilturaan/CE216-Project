import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class LiveMatchScreen {

    private final Match match;
    private final GameManager gameManager;
    private final StackPane root;
    private VBox eventList;
    private final Label scoreLabel;
    private final Label statusLabel;
    private final Label possessionLabel;
    private final Label shotsLabel;
    private final Label shotsOnTargetLabel;
    private final Label foulsLabel;
    private final ProgressBar homePossessionBar;
    private final ProgressBar awayPossessionBar;
    private VBox setScoresBox;
    private Player selectedLiveLineupPlayer;
    private Label clockLabel;
    private ProgressBar matchClockBar;
    private Timeline periodTimeline;
    private boolean periodRunning;

    public LiveMatchScreen(Match match, GameManager gameManager) {
        this.match = match;
        this.gameManager = gameManager;

        root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #081120, #111827);");

        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));

        possessionLabel = new Label();
        shotsLabel = new Label();
        shotsOnTargetLabel = new Label();
        foulsLabel = new Label();

        homePossessionBar = new ProgressBar();
        awayPossessionBar = new ProgressBar();
        selectedLiveLineupPlayer = null;
        periodTimeline = null;
        periodRunning = false;

        VBox content = new VBox(20);
        content.setPadding(new Insets(28));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(1200);

        Label title = new Label("LIVE MATCH CENTER");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        statusLabel = new Label(match.getLiveStatusText());
        statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #60a5fa;");

        HBox scoreCard = buildScoreCard();
        VBox clockPanel = buildMatchClockPanel();

        HBox centerLayout = new HBox(20);
        centerLayout.setAlignment(Pos.TOP_CENTER);

        VBox leftPanel = buildLiveFeedPanel();
        VBox rightPanel = buildStatsPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        centerLayout.getChildren().addAll(leftPanel, rightPanel);

        HBox controls = buildControls();

        content.getChildren().addAll(title, statusLabel, scoreCard, clockPanel, centerLayout, controls);

        root.getChildren().add(content);

        scoreLabel = (Label) ((VBox) scoreCard.getChildren().get(1)).getChildren().get(1);

        updateLiveData();
    }

    private HBox buildScoreCard() {
        HBox card = new HBox(40);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 18; -fx-border-color: #1e2d45; -fx-border-radius: 18; -fx-border-width: 1;");

        VBox homeBox = buildTeamBox(match.getHomeTeam().getName());
        VBox awayBox = buildTeamBox(match.getAwayTeam().getName());

        VBox middle = new VBox(10);
        middle.setAlignment(Pos.CENTER);

        Label score = new Label(match.getHomeScore() + " - " + match.getAwayScore());
        score.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        Label sport = new Label(match.getSport().getSportName().toUpperCase());
        sport.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");

        middle.getChildren().addAll(sport, score);

        card.getChildren().addAll(homeBox, middle, awayBox);
        return card;
    }

    private VBox buildTeamBox(String name) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(220);

        Circle logo = new Circle(36);
        logo.setFill(Color.web("#1e3a5f"));

        Label initials = new Label(getInitials(name));
        initials.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        StackPane logoPane = new StackPane(logo, initials);

        Label teamName = new Label(name);
        teamName.setWrapText(true);
        teamName.setAlignment(Pos.CENTER);
        teamName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        box.getChildren().addAll(logoPane, teamName);
        return box;
    }

    private VBox buildLiveFeedPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(18));
        panel.setPrefWidth(650);
        panel.setStyle("-fx-background-color: #111827; -fx-background-radius: 16; -fx-border-color: #1e2d45; -fx-border-radius: 16; -fx-border-width: 1;");

        Label title = new Label("MATCH FEED");
        title.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        eventList = new VBox(10);
        eventList.setPadding(new Insets(4));
        eventList.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(eventList);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-clean");
        scroll.setPrefHeight(420);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: #111827; -fx-border-color: transparent;");

        panel.getChildren().addAll(title, scroll);
        return panel;
    }

    private VBox buildStatsPanel() {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(18));
        panel.setPrefWidth(320);
        panel.setStyle("-fx-background-color: #111827; -fx-background-radius: 16; -fx-border-color: #1e2d45; -fx-border-radius: 16; -fx-border-width: 1;");

        Label title = new Label("MATCH STATS");
        title.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        VBox statsContent = new VBox(12);

        if (match.getSport().getSportName().equals("Volleyball")) {
            setScoresBox = new VBox(10);
            statsContent.getChildren().addAll(statTitle("SET SCORES"), setScoresBox);
        } else {
            homePossessionBar.setPrefWidth(250);
            awayPossessionBar.setPrefWidth(250);
            homePossessionBar.setStyle("-fx-accent: #3b82f6;");
            awayPossessionBar.setStyle("-fx-accent: #ef4444;");

            possessionLabel.setStyle(statValueStyle());
            shotsLabel.setStyle(statValueStyle());
            shotsOnTargetLabel.setStyle(statValueStyle());
            foulsLabel.setStyle(statValueStyle());

            statsContent.getChildren().addAll(
                    statTitle("POSSESSION"),
                    homePossessionBar,
                    awayPossessionBar,
                    possessionLabel,
                    statTitle("SHOTS"),
                    shotsLabel,
                    statTitle("SHOTS ON TARGET"),
                    shotsOnTargetLabel,
                    statTitle("FOULS"),
                    foulsLabel
            );
        }

        panel.getChildren().addAll(title, statsContent);
        return panel;
    }

    private VBox buildMatchClockPanel() {
        VBox panel = new VBox(8);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(720);
        panel.setPadding(new Insets(14, 18, 14, 18));
        panel.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        clockLabel = new Label(match.isPlayed() ? "FULL TIME" : "READY TO START " + match.getSport().getPeriodName().toUpperCase() + " " + match.getCurrentPeriod());
        clockLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #60a5fa;");

        matchClockBar = new ProgressBar(0);
        matchClockBar.setPrefWidth(620);
        matchClockBar.setPrefHeight(9);
        matchClockBar.setStyle("-fx-accent: #22c55e; -fx-control-inner-background: #0d1222;");

        Label hint = new Label("During the running period, you can still open tactics or make substitutions.");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: #475569;");

        panel.getChildren().addAll(clockLabel, matchClockBar, hint);
        return panel;
    }

    private HBox buildControls() {
        HBox controls = new HBox(16);
        controls.setAlignment(Pos.CENTER);

        Button tacticBtn = new Button("CHANGE TACTIC");
        tacticBtn.setOnAction(e -> showTacticDialog());

        Button substitutionBtn = new Button("MAKE SUBSTITUTION");
        substitutionBtn.setOnAction(e -> showSubstitutionDialog());

        Button continueBtn = new Button(match.isPlayed() ? "FINISH MATCH" : "START PERIOD");

        styleButton(tacticBtn, "#1d4ed8");
        styleButton(substitutionBtn, "#7c3aed");
        styleButton(continueBtn, "#22c55e");

        continueBtn.setOnAction(e -> {
            if (!match.isPlayed()) {
                if (!periodRunning) {
                    startPeriodAnimation(continueBtn);
                }
            } else {
                if (periodTimeline != null) {
                    periodTimeline.stop();
                }
                gameManager.playNextWeek();
                SceneManager.showMatchResult(match);
            }
        });

        controls.getChildren().addAll(tacticBtn, substitutionBtn, continueBtn);
        return controls;
    }

    private void startPeriodAnimation(Button controlButton) {
        periodRunning = true;
        controlButton.setDisable(true);

        final int totalSeconds = 15;
        final int[] elapsed = {0};
        final int[] revealedEvents = {0};
        final int periodNumber = match.getCurrentPeriod();
        final String periodName = match.getSport().getPeriodName().toUpperCase();

        match.prepareNextPeriodForLive();
        int pendingEventCount = Math.max(1, match.getPendingLiveEventCount());

        matchClockBar.setProgress(0);
        clockLabel.setText(buildClockText(periodName, periodNumber, 0));
        statusLabel.setText(periodName + " " + periodNumber + " IN PLAY");

        periodTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsed[0]++;
            double progress = elapsed[0] / (double) totalSeconds;
            matchClockBar.setProgress(progress);
            clockLabel.setText(buildClockText(periodName, periodNumber, progress));

            int targetRevealCount = (int) Math.floor(progress * pendingEventCount);
            while (revealedEvents[0] < targetRevealCount && match.hasPendingLiveEvents()) {
                match.revealNextLiveEvent();
                revealedEvents[0]++;
                updateLiveData();
            }

            if (elapsed[0] >= totalSeconds) {
                while (match.hasPendingLiveEvents()) {
                    match.revealNextLiveEvent();
                    revealedEvents[0]++;
                    updateLiveData();
                }
                periodTimeline.stop();
                finishAnimatedPeriod(controlButton);
            }
        }));

        periodTimeline.setCycleCount(totalSeconds);
        periodTimeline.play();
    }

    private String buildClockText(String periodName, int periodNumber, double progress) {
        if (match.getSport().getSportName().equalsIgnoreCase("Football")) {
            int minuteInHalf = Math.max(1, Math.min(45, (int) Math.round(progress * 45)));
            int totalMinute = ((periodNumber - 1) * 45) + minuteInHalf;
            return periodName + " " + periodNumber + " IN PLAY  •  " + totalMinute + "'";
        }

        int rally = Math.max(1, Math.min(25, (int) Math.round(progress * 25)));
        return periodName + " " + periodNumber + " IN PLAY  •  Rally " + rally + "/25";
    }

    private void finishAnimatedPeriod(Button controlButton) {
        periodRunning = false;
        controlButton.setDisable(false);
        matchClockBar.setProgress(0);
        updateLiveData();

        if (match.isPlayed()) {
            clockLabel.setText("FULL TIME");
            controlButton.setText("FINISH MATCH");
        } else {
            clockLabel.setText(match.getSport().getPeriodName().toUpperCase() + " " + (match.getCurrentPeriod() - 1) + " ENDED  •  READY FOR " + match.getSport().getPeriodName().toUpperCase() + " " + match.getCurrentPeriod());
            controlButton.setText("START NEXT PERIOD");
        }
    }

    private void updateLiveData() {
        scoreLabel.setText(match.getHomeScore() + " - " + match.getAwayScore());
        if (!periodRunning) {
            statusLabel.setText(match.getLiveStatusText());
        }

        eventList.getChildren().clear();

        if (match.getMatchEvents().isEmpty()) {
            Label empty = new Label("No match events yet. Continue to the next period to start the action.");
            empty.setWrapText(true);
            empty.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-padding: 12;");
            eventList.getChildren().add(empty);
        } else {
            for (String event : match.getMatchEvents()) {
                eventList.getChildren().add(buildEventCard(event));
            }
        }

        if (match.getSport().getSportName().equals("Volleyball")) {
            if (setScoresBox != null) {
                setScoresBox.getChildren().clear();
                List<String> scores = match.getSetScores();
                for (int i = 0; i < scores.size(); i++) {
                    Label l = new Label("Set " + (i + 1) + ":  " + scores.get(i));
                    l.setStyle(statValueStyle());
                    setScoresBox.getChildren().add(l);
                }
            }
        } else {
            int homePoss = match.getHomePossession();
            int awayPoss = match.getAwayPossession();

            homePossessionBar.setProgress(homePoss / 100.0);
            awayPossessionBar.setProgress(awayPoss / 100.0);

            possessionLabel.setText(match.getHomeTeam().getName() + " " + homePoss + "%  -  " + awayPoss + "% " + match.getAwayTeam().getName());

            shotsLabel.setText(match.getHomeShots() + " - " + match.getAwayShots());
            shotsOnTargetLabel.setText(match.getHomeShotsOnTarget() + " - " + match.getAwayShotsOnTarget());
            foulsLabel.setText(match.getHomeFouls() + " - " + match.getAwayFouls());
        }
    }

    private void showTacticDialog() {
        Team userTeam = gameManager.getUserTeam();
        final String[] selectedTactic = {userTeam.getTactic()};

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(3, 7, 18, 0.78);");
        overlay.setPickOnBounds(true);

        VBox modal = new VBox(20);
        modal.setAlignment(Pos.CENTER);
        modal.setMaxWidth(620);
        modal.setPadding(new Insets(28));
        modal.setStyle("-fx-background-color: linear-gradient(to bottom right, #111827, #0b1220); -fx-background-radius: 22; -fx-border-color: #1e3a5f; -fx-border-radius: 22; -fx-border-width: 1.2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 30, 0, 0, 12);");

        Label title = new Label("MATCH TACTICS");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        Label subtitle = new Label("Choose your tactical approach for the next period.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        HBox tacticCards = new HBox(14);
        tacticCards.setAlignment(Pos.CENTER);

        Button attacking = tacticCard("🔥", "Attacking", "High risk, more scoring chances", selectedTactic);
        Button balanced = tacticCard("⚖", "Balanced", "Stable shape and controlled play", selectedTactic);
        Button defensive = tacticCard("🛡", "Defensive", "Protect the score and reduce risk", selectedTactic);

        Runnable refreshSelection = () -> {
            attacking.setStyle(tacticCardStyle(selectedTactic[0].equals("Attacking"), "#ef4444"));
            balanced.setStyle(tacticCardStyle(selectedTactic[0].equals("Balanced"), "#3b82f6"));
            defensive.setStyle(tacticCardStyle(selectedTactic[0].equals("Defensive"), "#22c55e"));
        };

        attacking.setOnAction(e -> { selectedTactic[0] = "Attacking"; refreshSelection.run(); });
        balanced.setOnAction(e -> { selectedTactic[0] = "Balanced"; refreshSelection.run(); });
        defensive.setOnAction(e -> { selectedTactic[0] = "Defensive"; refreshSelection.run(); });
        refreshSelection.run();

        tacticCards.getChildren().addAll(attacking, balanced, defensive);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER);

        Button cancel = new Button("CANCEL");
        cancel.setStyle("-fx-background-color: #1f2937; -fx-text-fill: #94a3b8; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 11 24 11 24; -fx-background-radius: 10;");
        cancel.setOnAction(e -> root.getChildren().remove(overlay));

        Button apply = new Button("APPLY TACTIC");
        apply.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 11 28 11 28; -fx-background-radius: 10;");
        apply.setOnAction(e -> {
            userTeam.setTactic(selectedTactic[0]);
            root.getChildren().remove(overlay);
        });

        actions.getChildren().addAll(cancel, apply);
        modal.getChildren().addAll(title, subtitle, tacticCards, actions);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private void showSubstitutionDialog() {
        Team userTeam = gameManager.getUserTeam();
        boolean volleyballMatch = isVolleyballTeam(userTeam);
        selectedLiveLineupPlayer = null;

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(3, 7, 18, 0.84);");
        overlay.setPickOnBounds(true);

        VBox modal = new VBox(18);
        modal.setAlignment(Pos.CENTER);
        modal.setMaxWidth(1040);
        modal.setPadding(new Insets(26));
        modal.setStyle("-fx-background-color: linear-gradient(to bottom right, #111827, #0b1220); -fx-background-radius: 22; -fx-border-color: #1e3a5f; -fx-border-radius: 22; -fx-border-width: 1.2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 30, 0, 0, 12);");

        Label title = new Label("MATCH SUBSTITUTION");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        Label subtitle = new Label(volleyballMatch
                ? "Select one of the starting six, then choose a bench player to enter the rotation."
                : "Select a player on the pitch, then choose a bench player to enter the match.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        Label selectedLabel = new Label("Selected player: none");
        selectedLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #64748b;");

        HBox body = new HBox(18);
        body.setAlignment(Pos.TOP_CENTER);

        VBox pitchPanel = new VBox(10);
        pitchPanel.setPadding(new Insets(16));
        pitchPanel.setPrefWidth(660);
        pitchPanel.setStyle("-fx-background-color: #0d1222; -fx-background-radius: 16; -fx-border-color: #1e2d45; -fx-border-radius: 16; -fx-border-width: 1;");

        HBox pitchHeader = new HBox();
        pitchHeader.setAlignment(Pos.CENTER_LEFT);

        Label pitchTitle = new Label(volleyballMatch ? "VOLLEYBALL COURT" : "TACTICAL PITCH");
        pitchTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");

        Region pitchSpacer = new Region();
        HBox.setHgrow(pitchSpacer, Priority.ALWAYS);

        Label pitchHint = new Label(volleyballMatch ? "STARTING SIX" : "ON FIELD");
        pitchHint.setStyle("-fx-background-color: #10251a; -fx-text-fill: #22c55e; -fx-font-size: 9px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 3 8 3 8;");

        pitchHeader.getChildren().addAll(pitchTitle, pitchSpacer, pitchHint);

        StackPane pitch = new StackPane();
        pitch.setMinHeight(470);
        if (volleyballMatch) {
            pitch.setStyle("-fx-background-color: linear-gradient(to bottom, #92400e, #b45309); -fx-background-radius: 20; -fx-border-color: #f59e0b88; -fx-border-radius: 20; -fx-border-width: 1.5;");
        } else {
            pitch.setStyle("-fx-background-color: linear-gradient(to bottom, #0f5132, #14532d); -fx-background-radius: 20; -fx-border-color: #22c55e66; -fx-border-radius: 20; -fx-border-width: 1.5;");
        }

        StackPane fieldLines = new StackPane();
        fieldLines.setMouseTransparent(true);
        fieldLines.setMinHeight(470);
        fieldLines.setMaxWidth(Double.MAX_VALUE);

        if (volleyballMatch) {
            javafx.scene.shape.Rectangle netLine = new javafx.scene.shape.Rectangle(2, 3);
            netLine.widthProperty().bind(pitch.widthProperty().subtract(48));
            netLine.setFill(Color.web("#f8fafcaa"));

            javafx.scene.shape.Rectangle attackLineTop = new javafx.scene.shape.Rectangle(2, 2);
            attackLineTop.widthProperty().bind(pitch.widthProperty().subtract(70));
            attackLineTop.setFill(Color.web("#f8fafc66"));

            javafx.scene.shape.Rectangle attackLineBottom = new javafx.scene.shape.Rectangle(2, 2);
            attackLineBottom.widthProperty().bind(pitch.widthProperty().subtract(70));
            attackLineBottom.setFill(Color.web("#f8fafc66"));

            fieldLines.getChildren().addAll(netLine, attackLineTop, attackLineBottom);
            StackPane.setAlignment(netLine, Pos.CENTER);
            StackPane.setAlignment(attackLineTop, Pos.TOP_CENTER);
            StackPane.setAlignment(attackLineBottom, Pos.BOTTOM_CENTER);
            StackPane.setMargin(attackLineTop, new Insets(116, 0, 0, 0));
            StackPane.setMargin(attackLineBottom, new Insets(0, 0, 116, 0));
        } else {
            javafx.scene.shape.Rectangle centerLine = new javafx.scene.shape.Rectangle(2, 2);
            centerLine.widthProperty().bind(pitch.widthProperty().subtract(48));
            centerLine.setFill(Color.web("#ffffff55"));

            Circle centerCircle = new Circle(52);
            centerCircle.setFill(Color.TRANSPARENT);
            centerCircle.setStroke(Color.web("#ffffff55"));
            centerCircle.setStrokeWidth(2);

            javafx.scene.shape.Rectangle penaltyBox = new javafx.scene.shape.Rectangle(230, 82);
            penaltyBox.setFill(Color.TRANSPARENT);
            penaltyBox.setStroke(Color.web("#ffffff44"));
            penaltyBox.setStrokeWidth(2);
            penaltyBox.setArcWidth(8);
            penaltyBox.setArcHeight(8);

            javafx.scene.shape.Rectangle goalBox = new javafx.scene.shape.Rectangle(128, 30);
            goalBox.setFill(Color.TRANSPARENT);
            goalBox.setStroke(Color.web("#ffffff44"));
            goalBox.setStrokeWidth(2);
            goalBox.setArcWidth(6);
            goalBox.setArcHeight(6);

            fieldLines.getChildren().addAll(centerCircle, centerLine, penaltyBox, goalBox);
            StackPane.setAlignment(centerCircle, Pos.CENTER);
            StackPane.setAlignment(centerLine, Pos.CENTER);
            StackPane.setAlignment(penaltyBox, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(goalBox, Pos.BOTTOM_CENTER);
            StackPane.setMargin(penaltyBox, new Insets(0, 0, 42, 0));
            StackPane.setMargin(goalBox, new Insets(0, 0, 18, 0));
        }

        GridPane lineupGrid = new GridPane();
        if (volleyballMatch) {
            lineupGrid.setPadding(new Insets(54, 60, 54, 60));
            lineupGrid.setVgap(88);
            lineupGrid.setHgap(48);
            lineupGrid.setAlignment(Pos.CENTER);
            StackPane.setAlignment(lineupGrid, Pos.CENTER);
        } else {
            lineupGrid.setPadding(new Insets(30, 24, 24, 24));
            lineupGrid.setVgap(16);
            lineupGrid.setHgap(12);
            lineupGrid.setAlignment(Pos.BOTTOM_CENTER);
            StackPane.setAlignment(lineupGrid, Pos.BOTTOM_CENTER);
        }

        List<Player> lineup = userTeam.getStartingLineup();
        if (lineup.isEmpty()) {
            userTeam.generateDefaultLineup();
            lineup = userTeam.getStartingLineup();
        }

        int[][] footballPositions = {
                {2, 5},
                {0, 4}, {1, 4}, {3, 4}, {4, 4},
                {1, 3}, {2, 3}, {3, 3},
                {1, 2}, {2, 2}, {3, 2}
        };
        int[][] compactPositions = {
                {2, 5},
                {1, 4}, {3, 4},
                {0, 3}, {2, 3}, {4, 3},
                {2, 2}
        };
        int[][] volleyballPositions = {
                {1, 1},
                {0, 1},
                {2, 1},
                {0, 0},
                {1, 0},
                {2, 0}
        };
        int[][] positions = volleyballMatch ? volleyballPositions : (lineup.size() <= 7 ? compactPositions : footballPositions);

        for (int i = 0; i < lineup.size(); i++) {
            Player starter = lineup.get(i);

            StackPane playerCard = new StackPane();
            playerCard.setPrefSize(106, 52);
            playerCard.setStyle("-fx-background-color: #0d1222dd; -fx-background-radius: 14; -fx-border-color: #22c55e88; -fx-border-radius: 14; -fx-border-width: 1; -fx-cursor: hand;");

            VBox text = new VBox(2);
            text.setAlignment(Pos.CENTER);

            String displayName = starter.getName().length() > 11 ? starter.getName().substring(0, 11) + "..." : starter.getName();
            Label name = new Label(displayName);
            name.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

            String staminaColor = starter.getStamina() >= 70 ? "#22c55e" : starter.getStamina() >= 35 ? "#f59e0b" : "#ef4444";
            Label meta = new Label(shortPosition(starter.getPosition()) + " • " + starter.getStamina() + "%");
            meta.setStyle("-fx-font-size: 9px; -fx-text-fill: " + staminaColor + ";");

            text.getChildren().addAll(name, meta);
            playerCard.getChildren().add(text);

            playerCard.setOnMouseClicked(e -> {
                selectedLiveLineupPlayer = starter;
                selectedLabel.setText("Selected player: " + starter.getName());
                selectedLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #60a5fa;");

                for (javafx.scene.Node node : lineupGrid.getChildren()) {
                    node.setStyle("-fx-background-color: #0d1222dd; -fx-background-radius: 14; -fx-border-color: #22c55e88; -fx-border-radius: 14; -fx-border-width: 1; -fx-cursor: hand;");
                }
                playerCard.setStyle("-fx-background-color: #0f1e35; -fx-background-radius: 14; -fx-border-color: #60a5fa; -fx-border-radius: 14; -fx-border-width: 2; -fx-cursor: hand;");
            });

            int[] pos = positions[Math.min(i, positions.length - 1)];
            lineupGrid.add(playerCard, pos[0], pos[1]);
        }

        pitch.getChildren().addAll(fieldLines, lineupGrid);
        pitchPanel.getChildren().addAll(pitchHeader, pitch);

        VBox benchPanel = new VBox(10);
        benchPanel.setPadding(new Insets(16));
        benchPanel.setPrefWidth(310);
        benchPanel.setStyle("-fx-background-color: #0d1222; -fx-background-radius: 16; -fx-border-color: #1e2d45; -fx-border-radius: 16; -fx-border-width: 1;");

        HBox benchHeader = new HBox();
        benchHeader.setAlignment(Pos.CENTER_LEFT);

        Label benchTitle = new Label("BENCH");
        benchTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #475569;");

        Region benchSpacer = new Region();
        HBox.setHgrow(benchSpacer, Priority.ALWAYS);

        Label benchCount = new Label(userTeam.getSubstitutes().size() + (volleyballMatch ? " BENCH" : " SUBS"));
        benchCount.setStyle("-fx-background-color: #1e1635; -fx-text-fill: #a78bfa; -fx-font-size: 9px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 3 8 3 8;");
        benchHeader.getChildren().addAll(benchTitle, benchSpacer, benchCount);

        VBox benchList = new VBox(8);

        for (Player sub : userTeam.getSubstitutes()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 12, 10, 12));
            row.setStyle("-fx-background-color: #111827; -fx-background-radius: 10; -fx-border-color: #1e2d45; -fx-border-radius: 10; -fx-border-width: 1; -fx-cursor: hand;");

            Label icon = new Label(sub.isInjured() ? "🚑" : "🔁");
            icon.setStyle("-fx-font-size: 15px;");

            VBox text = new VBox(2);
            Label name = new Label(sub.getName());
            name.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
            Label meta = new Label(shortPosition(sub.getPosition()) + " • OVR " + sub.getSkillLevel() + " • STM " + sub.getStamina() + "%");
            meta.setStyle("-fx-font-size: 10px; -fx-text-fill: #64748b;");
            text.getChildren().addAll(name, meta);

            row.getChildren().addAll(icon, text);

            row.setOnMouseClicked(e -> {
                if (selectedLiveLineupPlayer == null) {
                    selectedLabel.setText("First select an on-field player.");
                    selectedLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #f59e0b;");
                    return;
                }

                boolean changed = userTeam.substitutePlayer(selectedLiveLineupPlayer, sub);

                if (changed) {
                    String substitutionEvent = "System: Substitution - " + sub.getName() + " replaces " + selectedLiveLineupPlayer.getName() + ".";
                    selectedLiveLineupPlayer = null;
                    root.getChildren().remove(overlay);
                    updateLiveData();
                    eventList.getChildren().add(buildEventCard(substitutionEvent));
                } else {
                    selectedLabel.setText("Substitution failed. This player cannot enter.");
                    selectedLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #ef4444;");
                }
            });

            benchList.getChildren().add(row);
        }

        ScrollPane benchScroll = new ScrollPane(benchList);
        benchScroll.setFitToWidth(true);
        benchScroll.setPrefHeight(430);
        benchScroll.setStyle("-fx-background-color: transparent; -fx-background: #0d1222; -fx-border-color: transparent;");

        benchPanel.getChildren().addAll(benchHeader, benchScroll);

        body.getChildren().addAll(pitchPanel, benchPanel);

        Button close = new Button("CLOSE");
        close.setStyle("-fx-background-color: #1f2937; -fx-text-fill: #94a3b8; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 11 30 11 30; -fx-background-radius: 10;");
        close.setOnAction(e -> root.getChildren().remove(overlay));

        modal.getChildren().addAll(title, subtitle, selectedLabel, body, close);
        overlay.getChildren().add(modal);
        root.getChildren().add(overlay);
    }

    private boolean isVolleyballTeam(Team team) {
        for (Player player : team.getPlayers()) {
            String position = player.getPosition();
            if ("Setter".equals(position)
                    || "Outside Hitter".equals(position)
                    || "Middle Blocker".equals(position)
                    || "Opposite Hitter".equals(position)
                    || "Libero".equals(position)) {
                return true;
            }
        }
        return false;
    }

    private Button tacticCard(String icon, String tactic, String description, String[] selectedTactic) {
        Button button = new Button(icon + "\n" + tactic + "\n" + description);
        button.setWrapText(true);
        button.setMinSize(175, 135);
        button.setMaxSize(175, 135);
        button.setAlignment(Pos.CENTER);
        button.setStyle(tacticCardStyle(selectedTactic[0].equals(tactic), "#3b82f6"));
        return button;
    }

    private String tacticCardStyle(boolean selected, String accentColor) {
        String borderColor = selected ? accentColor : "#1e2d45";
        String background = selected ? "#13233d" : "#0d1222";
        return "-fx-background-color: " + background + ";" +
                "-fx-text-fill: #f0f4ff;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: " + (selected ? "2" : "1") + ";" +
                "-fx-padding: 14;" +
                "-fx-cursor: hand;";
    }

    private VBox buildEventCard(String event) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.setStyle("-fx-background-color: " + getEventBackground(event) + "; -fx-background-radius: 12; -fx-border-color: " + getEventBorder(event) + "; -fx-border-radius: 12; -fx-border-width: 1;");

        Label main = new Label(formatEvent(event));
        main.setWrapText(true);
        main.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + getEventColor(event) + ";");

        Label sub = new Label(getEventSubtitle(event));
        sub.setWrapText(true);
        sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        card.getChildren().addAll(main, sub);
        return card;
    }

    private String getEventBackground(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) return "#10251a";
        if (event.contains("Injury")) return "#2a1014";
        if (event.contains("Full time")) return "#0f1e35";
        if (event.contains("System:")) return "#1e1635";
        return "#0d1222";
    }

    private String getEventBorder(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) return "#14532d";
        if (event.contains("Injury")) return "#7f1d1d";
        if (event.contains("Full time")) return "#1d4ed8";
        if (event.contains("System:")) return "#7c3aed";
        return "#1e2d45";
    }

    private String getEventSubtitle(String event) {
        if (event.contains("GOAL")) return "Momentum shifts after an important scoring action.";
        if (event.contains("wins the set")) return "Set momentum recorded by the live match engine.";
        if (event.contains("Injury")) return "Squad availability will be affected after the match.";
        if (event.contains("Full time")) return "The match is complete. Review the final result.";
        if (event.contains("System:")) return "Manager action recorded during the match.";
        return "Match event recorded.";
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 22 12 22;" +
                        "-fx-background-radius: 12;"
        );
    }

    private String formatEvent(String event) {
        if (event.contains("GOAL")) return "⚽  " + event;
        if (event.contains("Injury")) return "🚑  " + event;
        if (event.contains("Full time")) return "🏁  " + event;
        if (event.contains("wins the set")) return "🏐  " + event;
        if (event.contains("System:")) return "🔁  " + event.replace("System: ", "");
        return "•  " + event;
    }

    private String getEventColor(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) return "#22c55e";
        if (event.contains("Injury")) return "#ef4444";
        if (event.contains("Full time")) return "#60a5fa";
        if (event.contains("System:")) return "#a78bfa";
        return "#cbd5e1";
    }
    private String shortPosition(String position) {
        if (position == null) {
            return "POS";
        }
        return switch (position) {
            case "Goalkeeper" -> "GK";
            case "Defender" -> "DEF";
            case "Midfielder" -> "MID";
            case "Forward" -> "FWD";
            case "Setter" -> "SET";
            case "Outside Hitter" -> "OH";
            case "Middle Blocker" -> "MB";
            case "Opposite Hitter" -> "OPP";
            case "Libero" -> "LIB";
            default -> position;
        };
    }

    private Label statTitle(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        return lbl;
    }

    private String statValueStyle() {
        return "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;";
    }

    private String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
    }

    public StackPane getRoot() {
        return root;
    }
}
