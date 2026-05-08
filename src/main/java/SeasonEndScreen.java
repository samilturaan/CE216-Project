import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.List;

public class SeasonEndScreen {
    private StackPane root;

    public SeasonEndScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #080c18;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));

        GameManager gm = SceneManager.getGameManager();
        Team userTeam = gm.getUserTeam();
        League league = gm.getLeague();
        List<Team> standings = league.getStandings();
        boolean champion = standings.get(0).getName().equals(userTeam.getName());

        Pane bg = new Pane(); bg.setPrefSize(1100,750);
        Circle c = new Circle(400, Color.web(champion ? "#f59e0b05" : "#3b82f604"));
        c.setLayoutX(550); c.setLayoutY(375);
        bg.getChildren().add(c);

        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));

        Label badge = new Label("SEASON COMPLETE");
        badge.setStyle("-fx-background-color: #1e2d45; -fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 14 5 14;");

        Label icon = new Label(champion ? "🏆" : "📋");
        icon.setStyle("-fx-font-size: 72px;");

        Label titleLbl = new Label(champion ? "LEAGUE CHAMPIONS!" : "SEASON OVER");
        titleLbl.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: " + (champion ? "#f59e0b" : "#f0f4ff") + ";" + (champion ? "-fx-effect: dropshadow(gaussian,#f59e0b,20,0.4,0,0);" : ""));

        Label subLbl = new Label(champion ? "Congratulations! You've won the league!" : "Better luck next season, manager.");
        subLbl.setStyle("-fx-font-size: 15px; -fx-text-fill: #334155;");

        VBox standingsCard = new VBox(10);
        standingsCard.setPadding(new Insets(20));
        standingsCard.setMaxWidth(480);
        standingsCard.setStyle("-fx-background-color: #111827; -fx-background-radius: 14; -fx-border-color: #1e2d45; -fx-border-radius: 14; -fx-border-width: 1;");
        Label stTitle = new Label("FINAL STANDINGS");
        stTitle.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        standingsCard.getChildren().add(stTitle);

        String[] medals = {"🥇","🥈","🥉","4th"};
        for (int i = 0; i < standings.size(); i++) {
            Team t = standings.get(i);
            boolean isUser = t.getName().equals(userTeam.getName());
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8,0,8,0));
            Label medal = new Label(i < 3 ? medals[i] : medals[3]);
            medal.setMinWidth(36);
            Label nameL = new Label((isUser ? "★  " : "") + t.getName());
            nameL.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isUser ? "#60a5fa" : "#94a3b8") + "; -fx-font-weight: " + (isUser ? "bold" : "normal") + ";");
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label pts = new Label(t.getPoints() + " pts");
            pts.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isUser ? "#3b82f6" : "#334155") + ";");
            row.getChildren().addAll(medal, nameL, sp, pts);
            standingsCard.getChildren().add(row);
        }

        Button newGameBtn = new Button("New Game");
        newGameBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 36 12 36; -fx-border-width: 0;");
        newGameBtn.setOnAction(e -> SceneManager.showSplash());

        content.getChildren().addAll(badge, icon, titleLbl, subLbl, standingsCard, newGameBtn);
        root.getChildren().addAll(bg, content);
    }

    public StackPane getRoot() { return root; }
}