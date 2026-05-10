import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SportSelectScreen {
    private StackPane root;

    public SportSelectScreen() {
        root = new StackPane();
        root.setStyle("-fx-background-color: #080c18;");
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));

        Pane bg = new Pane(); bg.setPrefSize(1100,750);
        Circle c = new Circle(350, Color.web("#3b82f604"));
        c.setLayoutX(1100); c.setLayoutY(0);
        bg.getChildren().add(c);

        VBox main = new VBox(36);
        main.setAlignment(Pos.CENTER);
        main.setPadding(new Insets(60));

        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        Label title = new Label("Choose Your Sport");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label sub = new Label("Select the sport you want to manage");
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");
        header.getChildren().addAll(title, sub);

        HBox cards = new HBox(28);
        cards.setAlignment(Pos.CENTER);
        cards.getChildren().addAll(
                buildCard("⚽", "FOOTBALL", "11 vs 11  •  5 substitutes", "Win: 3pts  •  Draw: 1pt  •  Loss: 0pts", "#1d4ed8", "#3b82f6", new Football()),
                buildCard("🏐", "VOLLEYBALL", "6 vs 6  •  6 substitutes", "Win: 3pts  •  No draws  •  Loss: 0pts", "#6d28d9", "#a78bfa", new Volleyball()),
                buildCard("🤾", "HANDBALL", "7 vs 7  •  7 substitutes", "Win: 2pts  •  Draw: 1pt  •  Loss: 0pts", "#c2410c", "#f97316", new Handball())
        );

        Button back = new Button("← Back");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #334155; -fx-font-size: 13px; -fx-cursor: hand; -fx-border-width: 0;");
        back.setOnAction(e -> SceneManager.showSplash());

        main.getChildren().addAll(header, cards, back);
        root.getChildren().addAll(bg, main);
    }

    private VBox buildCard(String emoji, String name, String line1, String line2, String bgColor, String accent, ISport sport) {
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(44, 52, 44, 52));
        card.setMinWidth(300);
        card.setStyle("-fx-background-color: #111827; -fx-background-radius: 18; -fx-border-color: #1e2d45; -fx-border-radius: 18; -fx-border-width: 2; -fx-cursor: hand;");

        StackPane iconCircle = new StackPane();
        Circle circle = new Circle(40, Color.web(bgColor + "33"));
        Label iconLbl = new Label(emoji);
        iconLbl.setStyle("-fx-font-size: 44px;");
        iconCircle.getChildren().addAll(circle, iconLbl);

        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + accent + ";");

        Region div = new Region();
        div.setMinHeight(1); div.setMaxHeight(1);
        div.setMinWidth(160); div.setMaxWidth(160);
        div.setStyle("-fx-background-color: #1e2d45;");

        Label l1 = new Label(line1);
        l1.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");
        Label l2 = new Label(line2);
        l2.setStyle("-fx-font-size: 12px; -fx-text-fill: #334155;");

        Label selectHint = new Label("Click to select →");
        selectHint.setStyle("-fx-font-size: 12px; -fx-text-fill: #1e2d45;");

        card.getChildren().addAll(iconCircle, nameLbl, div, l1, l2, selectHint);

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #0f1e35; -fx-background-radius: 18; -fx-border-color: " + accent + "; -fx-border-radius: 18; -fx-border-width: 2; -fx-cursor: hand;");
            selectHint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + accent + ";");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #111827; -fx-background-radius: 18; -fx-border-color: #1e2d45; -fx-border-radius: 18; -fx-border-width: 2; -fx-cursor: hand;");
            selectHint.setStyle("-fx-font-size: 12px; -fx-text-fill: #1e2d45;");
        });
        card.setOnMouseClicked(e -> {
            SceneManager.resetGameManager();
            SceneManager.getGameManager().selectSport(sport);
            SceneManager.showTeamSetup();
        });

        return card;
    }

    public StackPane getRoot() { return root; }
}