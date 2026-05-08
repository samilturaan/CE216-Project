import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MatchResultScreen {

    private StackPane root;

    public MatchResultScreen(Match match) {
        root = new StackPane();
        root.setStyle("-fx-background-color: #0d0f1a;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        // Dekor
        Pane decor = new Pane();
        decor.setPrefSize(1100, 750);
        Circle c1 = new Circle(300, Color.web("#4fc3f708"));
        c1.setLayoutX(550); c1.setLayoutY(375);
        decor.getChildren().add(c1);

        VBox content = new VBox(32);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        // Başlık
        Label header = new Label("MATCH RESULT");
        header.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7; " +
                "-fx-background-color: #1a3a5c; -fx-background-radius: 20; -fx-padding: 6 16 6 16;");

        // Skor kutusu
        HBox scoreBox = new HBox(40);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPadding(new Insets(40, 60, 40, 60));
        scoreBox.setStyle("-fx-background-color: #151929; -fx-background-radius: 20; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 20; -fx-border-width: 1;");

        // Home team
        VBox homeBox = new VBox(8);
        homeBox.setAlignment(Pos.CENTER);
        Label homeName = new Label(match.getHomeTeam().getName());
        homeName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label homeScore = new Label(String.valueOf(match.getHomeScore()));
        homeScore.setStyle("-fx-font-size: 72px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        homeBox.getChildren().addAll(homeName, homeScore);

        Label vsLabel = new Label("–");
        vsLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: #37474f;");

        // Away team
        VBox awayBox = new VBox(8);
        awayBox.setAlignment(Pos.CENTER);
        Label awayName = new Label(match.getAwayTeam().getName());
        awayName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label awayScore = new Label(String.valueOf(match.getAwayScore()));
        awayScore.setStyle("-fx-font-size: 72px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        awayBox.getChildren().addAll(awayName, awayScore);

        scoreBox.getChildren().addAll(homeBox, vsLabel, awayBox);

        // Sonuç açıklaması
        String outcome = getOutcome(match);
        Label outcomeLabel = new Label(outcome);
        outcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " +
                getOutcomeColor(match) + ";");

        // Points info
        GameManager gm = SceneManager.getGameManager();
        Team userTeam = gm.getUserTeam();
        Label pointsLabel = new Label("Your team now has " + userTeam.getPoints() + " points");
        pointsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #546e7a;");

        // Injured players
        long injuredCount = userTeam.getPlayers().stream().filter(Player::isInjured).count();
        if (injuredCount > 0) {
            Label injuryLabel = new Label("⚠  " + injuredCount + " player(s) got injured!");
            injuryLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #ef9a9a; " +
                    "-fx-background-color: #3e1212; -fx-background-radius: 8; -fx-padding: 8 16 8 16;");
            content.getChildren().add(injuryLabel);
        }

        Button continueBtn = new Button("Continue →");
        continueBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; " +
                "-fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 10; " +
                "-fx-cursor: hand; -fx-padding: 12 40 12 40;");
        continueBtn.setOnAction(e -> {
            if (gm.isSeasonFinished()) {
                SceneManager.showSeasonEnd();
            } else {
                SceneManager.showMain();
            }
        });

        content.getChildren().addAll(header, scoreBox, outcomeLabel, pointsLabel, continueBtn);
        root.getChildren().addAll(decor, content);
    }

    private String getOutcome(Match match) {
        GameManager gm = SceneManager.getGameManager();
        Team userTeam = gm.getUserTeam();
        boolean userIsHome = match.getHomeTeam().getName().equals(userTeam.getName());
        boolean userIsAway = match.getAwayTeam().getName().equals(userTeam.getName());

        if (!userIsHome && !userIsAway) return "Match Played";

        int userScore = userIsHome ? match.getHomeScore() : match.getAwayScore();
        int oppScore = userIsHome ? match.getAwayScore() : match.getHomeScore();

        if (userScore > oppScore) return "🏆  Victory!";
        if (userScore < oppScore) return "😔  Defeat";
        return "🤝  Draw";
    }

    private String getOutcomeColor(Match match) {
        String outcome = getOutcome(match);
        if (outcome.contains("Victory")) return "#a5d6a7";
        if (outcome.contains("Defeat")) return "#ef9a9a";
        return "#fff176";
    }

    public StackPane getRoot() { return root; }
}
