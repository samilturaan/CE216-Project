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

        VBox content = new VBox(20);
        content.setPadding(new Insets(28));
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(1200);

        Label title = new Label("LIVE MATCH CENTER");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        statusLabel = new Label(match.getLiveStatusText());
        statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #60a5fa;");

        HBox scoreCard = buildScoreCard();

        HBox centerLayout = new HBox(20);
        centerLayout.setAlignment(Pos.TOP_CENTER);

        VBox leftPanel = buildLiveFeedPanel();
        VBox rightPanel = buildStatsPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        centerLayout.getChildren().addAll(leftPanel, rightPanel);

        HBox controls = buildControls();

        content.getChildren().addAll(title, statusLabel, scoreCard, centerLayout, controls);

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

        eventList = new VBox(8);

        ScrollPane scroll = new ScrollPane(eventList);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-clean");
        scroll.setStyle("-fx-background-color: transparent; -fx-background: #111827; -fx-border-color: transparent;");

        scroll.setPrefHeight(420);
        scroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

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

    private HBox buildControls() {
        HBox controls = new HBox(16);
        controls.setAlignment(Pos.CENTER);

        Button tacticBtn = new Button("CHANGE TACTIC");
        tacticBtn.setOnAction(e -> showTacticDialog());

        Button continueBtn = new Button(match.isPlayed() ? "FINISH MATCH" : "CONTINUE NEXT PERIOD");

        styleButton(tacticBtn, "#1d4ed8");
        styleButton(continueBtn, "#22c55e");

        continueBtn.setOnAction(e -> {
            if (!match.isPlayed()) {
                match.playNextPeriod();
                updateLiveData();

                if (match.isPlayed()) {
                    continueBtn.setText("FINISH MATCH");
                }
            } else {
                gameManager.playNextWeek();
                SceneManager.showMatchResult(match);
            }
        });

        controls.getChildren().addAll(tacticBtn, continueBtn);
        return controls;
    }

    private void updateLiveData() {
        scoreLabel.setText(match.getHomeScore() + " - " + match.getAwayScore());
        statusLabel.setText(match.getLiveStatusText());

        eventList.getChildren().clear();

        for (String event : match.getMatchEvents()) {
            Label lbl = new Label(formatEvent(event));
            lbl.setWrapText(true);
            lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: " + getEventColor(event) + ";");
            eventList.getChildren().add(lbl);
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

        javafx.scene.control.ChoiceDialog<String> dialog =
                new javafx.scene.control.ChoiceDialog<>(
                        userTeam.getTactic(),
                        "Balanced",
                        "Attacking",
                        "Defensive"
                );

        dialog.setTitle("Tactics");
        dialog.setHeaderText("Select your tactic for the next period");

        dialog.showAndWait().ifPresent(userTeam::setTactic);
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
        return "•  " + event;
    }

    private String getEventColor(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) return "#ffffff";
        if (event.contains("Injury")) return "#ef4444";
        if (event.contains("Full time")) return "#60a5fa";
        return "#cbd5e1";
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
