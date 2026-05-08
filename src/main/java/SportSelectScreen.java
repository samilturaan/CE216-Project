import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SportSelectScreen {

    private StackPane root;

    public SportSelectScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #0d0f1a;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        // Dekor
        Pane decor = new Pane();
        decor.setPrefSize(1100, 750);
        Circle bg1 = new Circle(250, Color.web("#4fc3f706"));
        bg1.setLayoutX(1050); bg1.setLayoutY(700);
        Circle bg2 = new Circle(180, Color.web("#4fc3f706"));
        bg2.setLayoutX(50); bg2.setLayoutY(100);
        decor.getChildren().addAll(bg1, bg2);

        VBox mainContent = new VBox(40);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(60));

        Label title = new Label("SELECT YOUR SPORT");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label sub = new Label("Choose a sport to begin your managerial career");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #546e7a;");

        HBox cards = new HBox(40);
        cards.setAlignment(Pos.CENTER);

        VBox footballCard = buildSportCard("⚽", "FOOTBALL",
                "11 players  •  5 subs\nWin: 3pts  •  Draw: 1pt  •  Loss: 0pts",
                "#1565c0", "#4fc3f7");

        VBox volleyballCard = buildSportCard("🏐", "VOLLEYBALL",
                "6 players  •  6 subs\nWin: 3pts  •  No draws  •  Loss: 0pts",
                "#4a148c", "#ce93d8");

        footballCard.setOnMouseClicked(e -> {
            SceneManager.resetGameManager();
            SceneManager.getGameManager().selectSport(new Football());
            SceneManager.showTeamSetup();
        });

        volleyballCard.setOnMouseClicked(e -> {
            SceneManager.resetGameManager();
            SceneManager.getGameManager().selectSport(new Volleyball());
            SceneManager.showTeamSetup();
        });

        cards.getChildren().addAll(footballCard, volleyballCard);

        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setOnAction(e -> SceneManager.showSplash());

        mainContent.getChildren().addAll(title, sub, cards, backBtn);

        root.getChildren().addAll(decor, mainContent);
    }

    private VBox buildSportCard(String emoji, String name, String desc, String gradFrom, String gradTo) {
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(48, 56, 48, 56));
        card.setStyle(String.format(
            "-fx-background-color: #151929; -fx-background-radius: 20; " +
            "-fx-border-radius: 20; -fx-border-width: 2; -fx-border-color: #1e2a3a; " +
            "-fx-cursor: hand; -fx-min-width: 280;"));

        Label icon = new Label(emoji);
        icon.setStyle("-fx-font-size: 64px;");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + gradTo + ";");

        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #78909c; -fx-text-alignment: center;");
        descLabel.setWrapText(true);

        Label selectLabel = new Label("Click to Select");
        selectLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #37474f; -fx-padding: 8 0 0 0;");

        card.getChildren().addAll(icon, nameLabel, descLabel, selectLabel);

        // Hover efekti
        card.setOnMouseEntered(e -> card.setStyle(String.format(
            "-fx-background-color: #0d1a2e; -fx-background-radius: 20; " +
            "-fx-border-radius: 20; -fx-border-width: 2; -fx-border-color: " + gradTo + "; " +
            "-fx-cursor: hand; -fx-min-width: 280; " +
            "-fx-effect: dropshadow(gaussian, " + gradTo + "40, 24, 0.3, 0, 0);")));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: #151929; -fx-background-radius: 20; " +
            "-fx-border-radius: 20; -fx-border-width: 2; -fx-border-color: #1e2a3a; " +
            "-fx-cursor: hand; -fx-min-width: 280;"));

        return card;
    }

    public StackPane getRoot() { return root; }
}
