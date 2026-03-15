public class Main {
    public static void main(String[] args) {

        GameManager gameManager = new GameManager();

        // Select sport (example: Football)
        ISport sport = new Football();
        gameManager.selectSport(sport);

        // Start new game
        gameManager.startNewGame("My Team");

        System.out.println("Season started with sport: " + sport.getSportName());

        // Simple simulation loop
        while (!gameManager.isSeasonFinished()) {

            System.out.println("Training week...");
            gameManager.trainUserTeam();

            System.out.println("Playing next match...");
            gameManager.playNextWeek();

            System.out.println("Current standings:");

            League league = gameManager.getLeague();

            for (Team team : league.getStandings()) {
                System.out.println(team.getName() + " - " + team.getPoints() + " pts");
            }

            System.out.println("----------------------");
        }

        System.out.println("Season finished!");
    }
}
