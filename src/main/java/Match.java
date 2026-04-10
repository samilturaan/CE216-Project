import java.util.Random;

public class Match {

    private Team homeTeam;
    private Team awayTeam;
    private ISport sport;

    private int homeScore;
    private int awayScore;

    private boolean played;

    public Match(Team homeTeam, Team awayTeam, ISport sport) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.played = false;
    }

    public void simulateMatch() {

        if (played) {
            return;
        }

        Random random = new Random();

        homeScore = random.nextInt(5);
        awayScore = random.nextInt(5);

        if (homeScore > awayScore) {
            homeTeam.addPoints(sport.getPointsForWin());
            awayTeam.addPoints(sport.getPointsForLoss());
        }
        else if (awayScore > homeScore) {
            awayTeam.addPoints(sport.getPointsForWin());
            homeTeam.addPoints(sport.getPointsForLoss());
        }
        else {
            homeTeam.addPoints(sport.getPointsForDraw());
            awayTeam.addPoints(sport.getPointsForDraw());
        }

        played = true;
    }

    public String getResult() {
        return homeTeam.getName() + " " + homeScore + " - " +
               awayScore + " " + awayTeam.getName();
    }

    public boolean isPlayed() {
        return played;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
}
