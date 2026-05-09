import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;
    private static GameManager gameManager;

    public static void init(Stage s) {
        stage = s;
        gameManager = new GameManager();
        stage.setTitle("Sports Manager");
        stage.setWidth(1100);
        stage.setHeight(750);
        stage.setResizable(false);
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static void resetGameManager() {
        gameManager = new GameManager();
    }

    public static void setGameManager(GameManager gm) {
        gameManager = gm;
    }

    public static void showSplash() {
        SplashScreen splash = new SplashScreen();
        Scene scene = new Scene(splash.getRoot(), 1100, 750);
        stage.setScene(scene);
        stage.show();
    }

    public static void showSportSelect() {
        SportSelectScreen screen = new SportSelectScreen();
        Scene scene = new Scene(screen.getRoot(), 1100, 750);
        stage.setScene(scene);
    }

    public static void showTeamSetup() {
        TeamSetupScreen screen = new TeamSetupScreen();
        Scene scene = new Scene(screen.getRoot(), 1100, 750);
        stage.setScene(scene);
    }

    public static void showMain() {
        MainScreen screen = new MainScreen();
        Scene scene = new Scene(screen.getRoot(), 1100, 750);
        stage.setScene(scene);
    }

    public static void showMatchResult(Match match) {
        MatchResultScreen screen = new MatchResultScreen(match);
        Scene scene = new Scene(screen.getRoot(), 1100, 750);
        stage.setScene(scene);
    }

    public static void showLiveMatch(Match match) {
        LiveMatchScreen screen = new LiveMatchScreen(match, gameManager);
        Scene scene = new Scene(screen.getRoot(), 1280, 820);
        stage.setScene(scene);
    }

    public static void showSeasonEnd() {
        SeasonEndScreen screen = new SeasonEndScreen();
        Scene scene = new Scene(screen.getRoot(), 1100, 750);
        stage.setScene(scene);
    }
}
