import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TeamSetupScreen {

    private StackPane root;

    public TeamSetupScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #0d0f1a;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        Pane decor = new Pane();
        decor.setPrefSize(1100, 750);
        Circle c = new Circle(300, Color.web("#4fc3f705"));
        c.setLayoutX(900); c.setLayoutY(400);
        decor.getChildren().add(c);

        ISport sport = SceneManager.getGameManager().getSelectedSport();
        String sportName = sport != null ? sport.getSportName() : "Sport";
        String sportEmoji = "Football".equals(sportName) ? "⚽" : "🏐";

        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));
        content.setMaxWidth(560);

        Label sportBadge = new Label(sportEmoji + "  " + sportName.toUpperCase());
        sportBadge.setStyle("-fx-background-color: #1a3a5c; -fx-text-fill: #4fc3f7; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 20; " +
                "-fx-padding: 6 16 6 16;");

        Label title = new Label("Name Your Team");
        title.setStyle("-fx-font-size: 38px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label sub = new Label("Your team will be placed in a 4-team league.\nGood luck, manager!");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #546e7a; -fx-text-alignment: center;");
        sub.setWrapText(true);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Galatasaray, Real Madrid...");
        nameField.getStyleClass().add("text-field-dark");
        nameField.setMaxWidth(380);
        nameField.setStyle("-fx-background-color: #1e2a3a; -fx-text-fill: #eceff1; " +
                "-fx-prompt-text-fill: #546e7a; -fx-border-color: #2e3e52; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-padding: 12 16 12 16; -fx-font-size: 15px;");

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #ef5350; -fx-font-size: 12px;");

        Button startBtn = new Button("START GAME →");
        startBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; " +
                "-fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 10; " +
                "-fx-cursor: hand; -fx-padding: 12 40 12 40;");
        startBtn.setMaxWidth(380);

        startBtn.setOnAction(e -> {
            String teamName = nameField.getText().trim();
            if (teamName.isEmpty()) {
                errorLabel.setText("Please enter a team name.");
                return;
            }
            SceneManager.getGameManager().startNewGame(teamName);
            SceneManager.showMain();
        });

        nameField.setOnAction(e -> startBtn.fire());

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #546e7a; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-border-width: 0;");
        backBtn.setOnAction(e -> SceneManager.showSportSelect());

        content.getChildren().addAll(sportBadge, title, sub, nameField, errorLabel, startBtn, backBtn);

        root.getChildren().addAll(decor, content);
    }

    public StackPane getRoot() { return root; }
}
