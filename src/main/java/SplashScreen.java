import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SplashScreen {
    private StackPane root;

    public SplashScreen() {
        root = new StackPane();
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n"," "));
        root.setStyle("-fx-background-color: #080c18;");

        Pane bg = new Pane();
        bg.setPrefSize(1100, 750);

        Circle c1 = new Circle(280, Color.web("#3b82f606"));
        c1.setLayoutX(1000); c1.setLayoutY(100);
        Circle c2 = new Circle(200, Color.web("#3b82f605"));
        c2.setLayoutX(100); c2.setLayoutY(650);
        Circle c3 = new Circle(100, Color.web("#3b82f608"));
        c3.setLayoutX(550); c3.setLayoutY(60);

        for (int i = 0; i < 12; i++) {
            Rectangle line = new Rectangle(1100, 1, Color.web("#1e2d4520"));
            line.setLayoutY(i * 70);
            bg.getChildren().add(line);
        }
        bg.getChildren().addAll(c1, c2, c3);

        VBox logoArea = new VBox(4);
        logoArea.setAlignment(Pos.CENTER);

        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(80, 80);
        iconBg.setArcWidth(20); iconBg.setArcHeight(20);
        iconBg.setFill(Color.web("#3b82f6"));
        Label iconLbl = new Label("⚽");
        iconLbl.setStyle("-fx-font-size: 40px;");
        iconBox.getChildren().addAll(iconBg, iconLbl);

        Label brandTop = new Label("SPORTS");
        brandTop.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: #f0f4ff;");
        Label brandBot = new Label("MANAGER");
        brandBot.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: #3b82f6;");

        logoArea.getChildren().addAll(iconBox, brandTop, brandBot);

        Label sub = new Label("Build your squad. Set your tactics. Win the league.");
        sub.setStyle("-fx-font-size: 15px; -fx-text-fill: #334155;");

        Region divider = new Region();
        divider.setMinHeight(1); divider.setMaxHeight(1);
        divider.setMinWidth(300); divider.setMaxWidth(300);
        divider.setStyle("-fx-background-color: #1e2d45;");

        Button startBtn = new Button("START NEW GAME");
        startBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 13 48 13 48; -fx-border-width: 0;");
        startBtn.setOnAction(e -> SceneManager.showSportSelect());

        Label version = new Label("CE216 Software Engineering  •  Team 6");
        version.setStyle("-fx-font-size: 11px; -fx-text-fill: #1e2d45;");

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(logoArea, sub, divider, startBtn, version);

        root.getChildren().addAll(bg, content);
    }

    public StackPane getRoot() { return root; }
}