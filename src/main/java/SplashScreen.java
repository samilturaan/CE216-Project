import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SplashScreen {

    private StackPane root;

    public SplashScreen() {
        root = new StackPane();
        root.getStyleClass().add("root-dark");
        root.setStyle("-fx-background-color: #0d0f1a;");

        // Dekoratif arka plan şekilleri
        Pane decorPane = new Pane();
        decorPane.setPrefSize(1100, 750);

        Circle c1 = new Circle(200, Color.web("#4fc3f708"));
        c1.setLayoutX(900); c1.setLayoutY(150);
        Circle c2 = new Circle(300, Color.web("#1565c010"));
        c2.setLayoutX(100); c2.setLayoutY(600);
        Circle c3 = new Circle(120, Color.web("#4fc3f710"));
        c3.setLayoutX(550); c3.setLayoutY(80);

        Rectangle line1 = new Rectangle(2, 200, Color.web("#4fc3f720"));
        line1.setLayoutX(200); line1.setLayoutY(0);
        Rectangle line2 = new Rectangle(2, 150, Color.web("#4fc3f715"));
        line2.setLayoutX(850); line2.setLayoutY(500);

        decorPane.getChildren().addAll(c1, c2, c3, line1, line2);

        // İçerik
        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);

        // Logo / ikon alanı
        StackPane logoBox = new StackPane();
        Rectangle logoBg = new Rectangle(100, 100);
        logoBg.setArcWidth(24); logoBg.setArcHeight(24);
        logoBg.setFill(Color.web("#4fc3f7"));
        Label logoIcon = new Label("⚽");
        logoIcon.setStyle("-fx-font-size: 48px;");
        logoBox.getChildren().addAll(logoBg, logoIcon);

        Label title = new Label("SPORTS MANAGER");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, #4fc3f7, 24, 0.5, 0, 0);");

        Label sub = new Label("Build your team. Dominate the league.");
        sub.setStyle("-fx-font-size: 17px; -fx-text-fill: #546e7a;");

        // Version badge
        Label version = new Label("v1.0  •  Football & Volleyball");
        version.setStyle("-fx-font-size: 12px; -fx-text-fill: #37474f; -fx-padding: 0 0 16 0;");

        Button startBtn = new Button("NEW GAME");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setStyle("-fx-font-size: 16px; -fx-padding: 14 48 14 48; " +
                "-fx-background-color: #4fc3f7; -fx-text-fill: #0d0f1a; " +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");
        startBtn.setOnAction(e -> SceneManager.showSportSelect());

        content.getChildren().addAll(logoBox, title, sub, version, startBtn);
        content.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));

        root.getChildren().addAll(decorPane, content);
        root.getStylesheets().add("data:text/css," + Styles.BASE.replace("\n", " "));
    }

    public StackPane getRoot() { return root; }
}
