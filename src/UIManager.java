import java.util.List;
import java.util.Scanner;

public class UIManager {

    private GameManager gameManager;
    private Scanner scanner;

    public UIManager() {
        this.gameManager = new GameManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Sports League Management Game ===");
        selectSportMenu();
        createTeamMenu();
        mainMenu();
    }

    private void selectSportMenu() {
        while (true) {
            System.out.println("Select a sport:");
            System.out.println("1. Football");
            System.out.println("2. Volleyball");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                gameManager.selectSport(new Football());
                System.out.println("Football selected.\n");
                break;
            } else if (choice.equals("2")) {
                gameManager.selectSport(new Volleyball());
                System.out.println("Volleyball selected.\n");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private void createTeamMenu() {
        System.out.print("Enter your team name: ");
        String teamName = scanner.nextLine();
        gameManager.startNewGame(teamName);
        System.out.println("Season started with sport: " + gameManager.getSelectedSport().getSportName());
        System.out.println("Your team: " + gameManager.getUserTeam().getName() + "\n");
    }

    private void mainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("=== Main Menu ===");
            System.out.println("1. Show my team");
            System.out.println("2. Show available players");
            System.out.println("3. Train my team");
            System.out.println("4. Play next week");
            System.out.println("5. Show standings");
            System.out.println("6. Show fixtures");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1":
                    showMyTeam();
                    break;
                case "2":
                    showAvailablePlayers();
                    break;
                case "3":
                    trainMyTeam();
                    break;
                case "4":
                    playNextWeek();
                    break;
                case "5":
                    showStandings();
                    break;
                case "6":
                    showFixtures();
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting game...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();

            if (gameManager.isSeasonFinished()) {
                System.out.println("Season finished!");
                showStandings();
                running = false;
            }
        }
    }

    private void showMyTeam() {
        Team userTeam = gameManager.getUserTeam();

        System.out.println("Team Name: " + userTeam.getName());
        System.out.println("Points: " + userTeam.getPoints());
        System.out.println("Tactic: " + userTeam.getTactic());
        System.out.println("Coaches:");
        for (Coach coach : userTeam.getCoaches()) {
            System.out.println("- " + coach);
        }

        System.out.println("Players:");
        for (Player player : userTeam.getPlayers()) {
            System.out.println("- " + player);
        }
    }

    private void showAvailablePlayers() {
        Team userTeam = gameManager.getUserTeam();
        List<Player> availablePlayers = userTeam.getAvailablePlayers();

        System.out.println("Available Players:");
        for (Player player : availablePlayers) {
            System.out.println("- " + player);
        }
    }

    private void trainMyTeam() {
        gameManager.trainUserTeam();
        System.out.println("Training completed for your team.");
    }

    private void playNextWeek() {
        League league = gameManager.getLeague();

        if (league == null || league.seasonFinished()) {
            System.out.println("No more matches left.");
            return;
        }

        Match nextMatch = league.getFixtures().get(league.getCurrentWeek());
        System.out.println("Playing: " + nextMatch.getHomeTeam().getName() + " vs " + nextMatch.getAwayTeam().getName());

        gameManager.playNextWeek();

        System.out.println("Result: " + nextMatch.getResult());
    }

    private void showStandings() {
        League league = gameManager.getLeague();
        System.out.println("=== Standings ===");
        for (Team team : league.getStandings()) {
            System.out.println(team.getName() + " - " + team.getPoints() + " pts");
        }
    }

    private void showFixtures() {
        League league = gameManager.getLeague();
        System.out.println("=== Fixtures ===");
        int matchNumber = 1;
        for (Match match : league.getFixtures()) {
            String status = match.isPlayed() ? match.getResult() : "Not played yet";
            System.out.println(matchNumber + ". " + match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName() + " -> " + status);
            matchNumber++;
        }
    }
}
