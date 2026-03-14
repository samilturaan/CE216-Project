import java.util.Random;

public class Match {

    private Team homeTeam;
    private Team awayTeam;
    private ISport sport;

    public Match(Team homeTeam, Team awayTeam, ISport sport) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
    }

    public void simulateMatch() {

        Random rand = new Random();

        int homeScore = rand.nextInt(5);
        int awayScore = rand.nextInt(5);

        System.out.println("Match Result:");
        System.out.println(homeScore + " - " + awayScore);

        if (homeScore > awayScore) {
            homeTeam.addPoints(sport.getPointsForWin());
        }
        else if (awayScore > homeScore) {
            awayTeam.addPoints(sport.getPointsForWin());
        }
        else {
            homeTeam.addPoints(sport.getPointsForDraw());
            awayTeam.addPoints(sport.getPointsForDraw());
        }
    }
}