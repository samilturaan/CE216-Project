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

        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        Label badge = new Label("FULL TIME");
        badge.setStyle("-fx-background-color: #1e2d45; -fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 14 5 14;");

        HBox scoreCard = new HBox(48);
        scoreCard.setAlignment(Pos.CENTER);
        scoreCard.setPadding(new Insets(40, 80, 40, 80));
        scoreCard.setStyle("-fx-background-color: #111827; -fx-background-radius: 20; -fx-border-color: #1e2d45; -fx-border-radius: 20; -fx-border-width: 1;");

        VBox homeBox = new VBox(8);
        homeBox.setAlignment(Pos.CENTER);
        Label homeIcon = new Label("🏟");
        homeIcon.setStyle("-fx-font-size: 40px;");
        Label homeScore = new Label(String.valueOf(match.getHomeScore()));
        homeScore.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
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
        awayScore.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label awayName = new Label(match.getAwayTeam().getName());
        awayName.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        awayBox.getChildren().addAll(awayIcon, awayScore, awayName);

        scoreCard.getChildren().addAll(homeBox, dash, awayBox);

        Label outcomeLbl = new Label(outcomeEmoji + "  " + outcome);
        outcomeLbl.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + outcomeColor + ";");

        Label ptsLbl = new Label(userTeam.getName() + " now has " + userTeam.getPoints() + " points");
        ptsLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

        VBox otherResultsBox = buildOtherResultsBox(gm, match);

        long injured = userTeam.getPlayers().stream().filter(Player::isInjured).count();
        if (injured > 0) {
            Label injLbl = new Label("⚠  " + injured + " player(s) injured after this match");
            injLbl.setStyle("-fx-background-color: #450a0a; -fx-text-fill: #ef4444; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 8 16 8 16;");
            content.getChildren().add(injLbl);
        }

        Button continueBtn = new Button("Continue  →");
        continueBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 40 12 40; -fx-border-width: 0;");
        continueBtn.setOnAction(e -> {
            if (gm.isSeasonFinished()) SceneManager.showSeasonEnd();
            else SceneManager.showMain();
        });

        content.getChildren().addAll(badge, scoreCard, outcomeLbl, ptsLbl, otherResultsBox, continueBtn);
        root.getChildren().addAll(bg, content);
    }

    private VBox buildOtherResultsBox(GameManager gm, Match userMatch) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(520);
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
                result.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");
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