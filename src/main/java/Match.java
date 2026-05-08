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
        homeTeam.addMatchStats(homeScore, awayScore);
        awayTeam.addMatchStats(awayScore, homeScore);

        // Rastgele sakatlık: %20 ihtimalle 1 oyuncu 1-3 maç sakatlanır
        Random injuryRand = new Random();
        injureRandomPlayer(homeTeam, injuryRand);
        injureRandomPlayer(awayTeam, injuryRand);

        played = true;
    }

    private void injureRandomPlayer(Team team, Random random) {
        if (random.nextInt(5) == 0) { // %20 ihtimal
            java.util.List<Player> available = team.getAvailablePlayers();
            if (!available.isEmpty()) {
                Player unlucky = available.get(random.nextInt(available.size()));
                unlucky.injure(random.nextInt(3) + 1);
            }
        }
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