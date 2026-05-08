import java.util.Random;

public class Match {

    private Team homeTeam;
    private Team awayTeam;
    private ISport sport;

    private int homeScore;
    private int awayScore;

    private boolean played;
    private int currentPeriod;

    public Match(Team homeTeam, Team awayTeam, ISport sport) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.played = false;
        this.currentPeriod = 1;
    }

    public void playNextPeriod() {
        if (played) return;

        Random random = new Random();

        int hAttackBonus = getTacticBonus(homeTeam.getTactic(), true);
        int aAttackBonus = getTacticBonus(awayTeam.getTactic(), true);

        int hDefBonus = getTacticBonus(homeTeam.getTactic(), false);
        int aDefBonus = getTacticBonus(awayTeam.getTactic(), false);

        int hStrength = Math.max(0, random.nextInt(3) + hAttackBonus - aDefBonus);
        int aStrength = Math.max(0, random.nextInt(3) + aAttackBonus - hDefBonus);

        if (sport.getSportName().equals("Volleyball")) {
            // Voleybolda set berabere bitemez, eşitse birine ver
            if (hStrength == aStrength) {
                if (random.nextBoolean()) hStrength++; else aStrength++;
            }
            if (hStrength > aStrength) homeScore++;
            else awayScore++;
        } else {
            // Futbol ise golleri direkt ekle
            homeScore += hStrength;
            awayScore += aStrength;
        }

        currentPeriod++;

        //Voleybolda 3 sete ulaşılmış mı?
        if (sport.isMatchOver(homeScore, awayScore, currentPeriod)) {
            finalizeMatch();
        }
    }
    private int getTacticBonus(String tactic, boolean isAttack) {
        if (tactic == null) return 0;
        switch (tactic.toLowerCase()) {
            case "attacking": return isAttack ? 2 : -1;
            case "defensive": return isAttack ? -1 : 2;
            default: return 0;
        }
    }
    private void finalizeMatch() {
        homeTeam.addMatchStats(homeScore, awayScore);
        awayTeam.addMatchStats(awayScore, homeScore);

        if (homeScore > awayScore) {
            homeTeam.addPoints(sport.getPointsForWin());
            awayTeam.addPoints(sport.getPointsForLoss());
        } else if (awayScore > homeScore) {
            awayTeam.addPoints(sport.getPointsForWin());
            homeTeam.addPoints(sport.getPointsForLoss());
        } else {
            homeTeam.addPoints(sport.getPointsForDraw());
            awayTeam.addPoints(sport.getPointsForDraw());
        }

        Random injuryRand = new Random();
        injureRandomPlayer(homeTeam, injuryRand);
        injureRandomPlayer(awayTeam, injuryRand);

        played = true;
    }

    public void simulateMatch() {
        while (!played) {
            playNextPeriod();
        }
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
    public int getCurrentPeriod() { return currentPeriod; }

    public String getResult() {
        return homeTeam.getName() + " " + homeScore + " - " +
                awayScore + " " + awayTeam.getName();
    }

    public boolean isPlayed() {
        return played;
    }

    public ISport getSport() {
        return sport;
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