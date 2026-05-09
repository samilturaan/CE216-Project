import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class MatchResultScreen {
    private StackPane root;

    public MatchResultScreen(Match match) {
        root = new StackPane();
        root.setStyle("-fx-background-color: #080c18;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));

        Pane bg = new Pane(); bg.setPrefSize(1100,750);
        Circle c = new Circle(400, Color.web("#3b82f605"));
        c.setLayoutX(550); c.setLayoutY(375);
        bg.getChildren().add(c);

        GameManager gm = SceneManager.getGameManager();
        Team userTeam = gm.getUserTeam();
        boolean userHome = match.getHomeTeam().getName().equals(userTeam.getName());
        boolean userAway = match.getAwayTeam().getName().equals(userTeam.getName());
        int userScore = userHome ? match.getHomeScore() : match.getAwayScore();
        int oppScore  = userHome ? match.getAwayScore()  : match.getHomeScore();
        boolean involved = userHome || userAway;

        String outcome = !involved ? "Match Played" : userScore > oppScore ? "VICTORY" : userScore < oppScore ? "DEFEAT" : "DRAW";
        String outcomeColor = userScore > oppScore ? "#22c55e" : userScore < oppScore ? "#ef4444" : "#f59e0b";
        String outcomeEmoji = userScore > oppScore ? "🏆" : userScore < oppScore ? "😔" : "🤝";

        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(24, 42, 24, 42));

        Label badge = new Label("FULL TIME");
        badge.setStyle("-fx-background-color: #1e2d45; -fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 14 5 14;");

        HBox scoreCard = new HBox(70);
        scoreCard.setAlignment(Pos.CENTER);
        scoreCard.setMinWidth(980);
        scoreCard.setMaxWidth(1180);
        scoreCard.setPadding(new Insets(34, 90, 34, 90));
        scoreCard.setStyle("-fx-background-color: #111827; -fx-background-radius: 22; -fx-border-color: #1e2d45; -fx-border-radius: 22; -fx-border-width: 1;");

        VBox homeBox = new VBox(8);
        homeBox.setAlignment(Pos.CENTER);
        Label homeIcon = new Label("🏟");
        homeIcon.setStyle("-fx-font-size: 40px;");
        Label homeScore = new Label(String.valueOf(match.getHomeScore()));
        homeScore.setStyle("-fx-font-size: 76px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label homeName = new Label(match.getHomeTeam().getName());
        homeName.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        homeBox.getChildren().addAll(homeIcon, homeScore, homeName);

        Label dash = new Label("—");
        dash.setStyle("-fx-font-size: 36px; -fx-text-fill: #1e2d45;");

        VBox awayBox = new VBox(8);
        awayBox.setAlignment(Pos.CENTER);
        Label awayIcon = new Label("🏟");
        awayIcon.setStyle("-fx-font-size: 40px;");
        Label awayScore = new Label(String.valueOf(match.getAwayScore()));
        awayScore.setStyle("-fx-font-size: 76px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label awayName = new Label(match.getAwayTeam().getName());
        awayName.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        awayBox.getChildren().addAll(awayIcon, awayScore, awayName);

        scoreCard.getChildren().addAll(homeBox, dash, awayBox);

        Label outcomeLbl = new Label(outcomeEmoji + "  " + outcome);
        outcomeLbl.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + outcomeColor + ";");

        Label ptsLbl = new Label(userTeam.getName() + " now has " + userTeam.getPoints() + " points");
        ptsLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

        VBox otherResultsBox = buildOtherResultsBox(gm, match);
        VBox liveFeedBox = buildLiveFeedBox(match);
        HBox detailRow = new HBox(22);
        detailRow.setAlignment(Pos.CENTER);
        detailRow.getChildren().addAll(liveFeedBox, otherResultsBox);

        long injured = userTeam.getPlayers().stream().filter(Player::isInjured).count();
        if (injured > 0) {
            Label injLbl = new Label("⚠  " + injured + " player(s) injured after this match");
            injLbl.setStyle("-fx-background-color: #450a0a; -fx-text-fill: #ef4444; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 8 16 8 16;");
            content.getChildren().add(injLbl);
        }

        Button continueBtn = new Button("Continue  →");
        continueBtn.setMinWidth(180);
        continueBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 40 12 40; -fx-border-width: 0;");
        continueBtn.setOnAction(e -> {
            if (gm.isSeasonFinished()) SceneManager.showSeasonEnd();
            else SceneManager.showMain();
        });

        content.getChildren().addAll(badge, scoreCard, outcomeLbl, ptsLbl, detailRow, continueBtn);
        root.getChildren().addAll(bg, content);
    }

    private VBox buildLiveFeedBox(Match match) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMinWidth(680);
        box.setMaxWidth(760);
        box.setMinHeight(330);
        box.setMaxHeight(380);
        box.setPadding(new Insets(16, 18, 16, 18));
        box.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("LIVE MATCH CENTER");
        title.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #475569;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label count = new Label(match.getMatchEvents().size() + " EVENTS");
        count.setStyle("-fx-background-color: #0f1e35; -fx-text-fill: #60a5fa; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 4 10 4 10;");

        header.getChildren().addAll(title, spacer, count);
        box.getChildren().add(header);

        VBox eventList = new VBox(10);
        eventList.setPadding(new Insets(4));
        eventList.setStyle("-fx-background-color: transparent;");

        if (match.getMatchEvents().isEmpty()) {
            Label empty = new Label("No major events were recorded.");
            empty.setWrapText(true);
            empty.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-padding: 12;");
            eventList.getChildren().add(empty);
        } else {
            for (String event : match.getMatchEvents()) {
                eventList.getChildren().add(buildEventCard(event));
            }
        }

        ScrollPane scrollPane = new ScrollPane(eventList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(285);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #111827; -fx-border-color: transparent;");
        box.getChildren().add(scrollPane);

        return box;
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
        return "#0d1222";
    }

    private String getEventBorder(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) return "#14532d";
        if (event.contains("Injury")) return "#7f1d1d";
        if (event.contains("Full time")) return "#1d4ed8";
        return "#1e2d45";
    }

    private String getEventSubtitle(String event) {
        if (event.contains("GOAL")) return "Momentum shifts after an important scoring action.";
        if (event.contains("wins the set")) return "Set momentum recorded by the live match engine.";
        if (event.contains("Injury")) return "Squad availability will be affected after the match.";
        if (event.contains("Full time")) return "The match is complete. Review the final result.";
        return "Match event recorded.";
    }

    private String formatEvent(String event) {
        if (event.contains("GOAL")) {
            return "⚽  " + event;
        }
        if (event.contains("Injury")) {
            return "🚑  " + event;
        }
        if (event.contains("Full time")) {
            return "🏁  " + event;
        }
        if (event.contains("wins the set")) {
            return "🏐  " + event;
        }
        return "•  " + event;
    }

    private String getEventColor(String event) {
        if (event.contains("GOAL") || event.contains("wins the set")) {
            return "#22c55e";
        }
        if (event.contains("Injury")) {
            return "#ef4444";
        }
        if (event.contains("Full time")) {
            return "#60a5fa";
        }
        return "#cbd5e1";
    }

    private VBox buildOtherResultsBox(GameManager gm, Match userMatch) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMinWidth(360);
        box.setMaxWidth(420);
        box.setMinHeight(330);
        box.setPadding(new Insets(14, 18, 14, 18));
        box.setStyle("-fx-background-color: #111827; -fx-background-radius: 12; -fx-border-color: #1e2d45; -fx-border-radius: 12; -fx-border-width: 1;");

        Label title = new Label("OTHER RESULTS THIS WEEK");
        title.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        box.getChildren().add(title);

        boolean hasOtherResults = false;

        if (gm.getLeague() != null) {
            for (Match m : gm.getLeague().getLastWeekMatches()) {
                if (m == userMatch) {
                    continue;
                }

                Label result = new Label(m.getHomeTeam().getName() + "  " + m.getHomeScore() + " - " + m.getAwayScore() + "  " + m.getAwayTeam().getName());
                result.setWrapText(true);
                result.setStyle("-fx-font-size: 14px; -fx-text-fill: #94a3b8; -fx-padding: 3 0 3 0;");
                box.getChildren().add(result);
                hasOtherResults = true;
            }
        }

        if (!hasOtherResults) {
            Label empty = new Label("No other matches were played this week.");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #475569;");
            box.getChildren().add(empty);
        }

        return box;
    }

    public StackPane getRoot() { return root; }
}