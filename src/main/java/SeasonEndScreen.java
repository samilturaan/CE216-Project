import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class SeasonEndScreen {

    private StackPane root;

    public SeasonEndScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #0d0f1a;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        Pane decor = new Pane();
        decor.setPrefSize(1100, 750);
        Circle c1 = new Circle(350, Color.web("#ffd70008"));
        c1.setLayoutX(550); c1.setLayoutY(375);
        decor.getChildren().add(c1);

        GameManager gm = SceneManager.getGameManager();
        Team userTeam = gm.getUserTeam();
        League league = gm.getLeague();
        List<Team> standings = league.getStandings();
        boolean isChampion = standings.get(0).getName().equals(userTeam.getName());

        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        Label icon = new Label(isChampion ? "🏆" : "📋");
        icon.setStyle("-fx-font-size: 80px;");

        Label title = new Label(isChampion ? "CHAMPIONS!" : "SEASON OVER");
        title.setStyle("-fx-font-size: 46px; -fx-font-weight: bold; -fx-text-fill: " +
                (isChampion ? "#ffd700" : "white") + ";" +
                (isChampion ? "-fx-effect: dropshadow(gaussian, #ffd700, 24, 0.5, 0, 0);" : ""));

        Label sub = new Label(isChampion
                ? "Congratulations! Your team won the league!"
                : "Better luck next season.");
        sub.setStyle("-fx-font-size: 16px; -fx-text-fill: #78909c;");

        // Final standings
        VBox standingsBox = new VBox(8);
        standingsBox.setStyle("-fx-background-color: #151929; -fx-background-radius: 12; " +
                "-fx-border-color: #1e2a3a; -fx-border-radius: 12; -fx-border-width: 1; -fx-padding: 20;");
        standingsBox.setMaxWidth(500);
        Label stTitle = new Label("FINAL STANDINGS");
        stTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");
        standingsBox.getChildren().add(stTitle);

        String[] medals = {"🥇", "🥈", "🥉", "4th"};
        for (int i = 0; i < standings.size(); i++) {
            Team t = standings.get(i);
            boolean isUser = t.getName().equals(userTeam.getName());
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 0, 6, 0));

            Label medal = new Label(i < 3 ? medals[i] : medals[3]);
            medal.setStyle("-fx-min-width: 32;");
            Label name = new Label((isUser ? "★ " : "") + t.getName());
            name.setStyle("-fx-text-fill: " + (isUser ? "#4fc3f7" : "#cfd8dc") + "; -fx-font-size: 14px;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label pts = new Label(t.getPoints() + " pts");
            pts.setStyle("-fx-text-fill: " + (isUser ? "#4fc3f7" : "#546e7a") + "; -fx-font-size: 14px;");

            row.getChildren().addAll(medal, name, spacer, pts);
            standingsBox.getChildren().add(row);
        }

        HBox btnRow = new HBox(16);
        btnRow.setAlignment(Pos.CENTER);

        Button newGameBtn = new Button("New Game");
        newGameBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 32 12 32;");
        newGameBtn.setOnAction(e -> SceneManager.showSplash());

        btnRow.getChildren().add(newGameBtn);

        content.getChildren().addAll(icon, title, sub, standingsBox, btnRow);
        root.getChildren().addAll(decor, content);
    }

    public StackPane getRoot() { return root; }
}
