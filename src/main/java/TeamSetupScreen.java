import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class TeamSetupScreen {
    private StackPane root;

    public TeamSetupScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #080c18;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));

        ISport sport = SceneManager.getGameManager().getSelectedSport();
        String sportName = sport != null ? sport.getSportName() : "Sport";
        String emoji = "Football".equals(sportName) ? "⚽" : "🏐";
        String accent = "Football".equals(sportName) ? "#3b82f6" : "#a78bfa";

        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(520);

        Label badge = new Label(emoji + "  " + sportName.toUpperCase());
        badge.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: " + accent + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 16 6 16;");

        Label title = new Label("Name Your Club");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");

        Label sub = new Label("Your club will compete in a 4-team league season.");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Galatasaray, Real Madrid...");
        nameField.setMaxWidth(420);
        nameField.setStyle("-fx-background-color: #111827; -fx-text-fill: #f0f4ff; -fx-prompt-text-fill: #334155; -fx-border-color: #1e2d45; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 12 16 12 16; -fx-font-size: 15px;");

        Label errLbl = new Label("");
        errLbl.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");

        Button startBtn = new Button("CREATE CLUB  →");
        startBtn.setMaxWidth(420);
        startBtn.setStyle("-fx-background-color: " + accent + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 13 0 13 0; -fx-border-width: 0;");
        startBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { errLbl.setText("Please enter a club name."); return; }
            SceneManager.getGameManager().startNewGame(name);
            SceneManager.showMain();
        });
        nameField.setOnAction(e -> startBtn.fire());

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #334155; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-width: 0;");
        backBtn.setOnAction(e -> SceneManager.showSportSelect());

        content.getChildren().addAll(badge, title, sub, nameField, errLbl, startBtn, backBtn);
        root.getChildren().add(content);
    }

    public StackPane getRoot() { return root; }
}