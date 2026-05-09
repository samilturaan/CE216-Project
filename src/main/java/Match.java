import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Match implements java.io.Serializable{

    private Team homeTeam;
    private Team awayTeam;
    private ISport sport;

    private int homeScore;
    private int awayScore;

    private boolean played;
    private int currentPeriod;
    private List<String> matchEvents;
    private List<String> lastPeriodEvents;
    private List<String> setScores;

    private int homeShots;
    private int awayShots;
    private int homeShotsOnTarget;
    private int awayShotsOnTarget;
    private int homePossession;
    private int awayPossession;
    private int homeFouls;
    private int awayFouls;

    public Match(Team homeTeam, Team awayTeam, ISport sport) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.homeScore = 0;
        this.awayScore = 0;
        this.played = false;
        this.currentPeriod = 1;
        this.matchEvents = new ArrayList<>();
        this.lastPeriodEvents = new ArrayList<>();
        this.setScores = new ArrayList<>();
        this.homeShots = 0;
        this.awayShots = 0;
        this.homeShotsOnTarget = 0;
        this.awayShotsOnTarget = 0;
        this.homePossession = 50;
        this.awayPossession = 50;
        this.homeFouls = 0;
        this.awayFouls = 0;
    }

    public void playNextPeriod() {
        if (played) return;

        Random random = new Random();
        int playedPeriod = currentPeriod;
        lastPeriodEvents.clear();

        int hAttackBonus = getTacticBonus(homeTeam.getTactic(), true);
        int aAttackBonus = getTacticBonus(awayTeam.getTactic(), true);

        int hDefBonus = getTacticBonus(homeTeam.getTactic(), false);
        int aDefBonus = getTacticBonus(awayTeam.getTactic(), false);

        int hStrength = Math.max(0, random.nextInt(3) + hAttackBonus - aDefBonus);
        int aStrength = Math.max(0, random.nextInt(3) + aAttackBonus - hDefBonus);

        updateMatchStats(hStrength, aStrength, random);

        if (sport.getSportName().equals("Volleyball")) {
            if (hStrength == aStrength) {
                if (random.nextBoolean()) hStrength++; else aStrength++;
            }

            // 25 veya 5. set ise 15
            int targetScore = (playedPeriod == 5) ? 15 : 25;
            int hSetPoints = 0, aSetPoints = 0;

            // %20 İhtimalle set uzar (Deuce: 24-24 olur)
            boolean isDeuce = random.nextInt(5) == 0;
            int winnerPts = targetScore;
            int loserPts = targetScore - 2 - random.nextInt(6);

            if (isDeuce) {
                winnerPts = targetScore + 1 + random.nextInt(4);
                loserPts = winnerPts - 2;
            }

            if (hStrength > aStrength) {
                homeScore++;
                hSetPoints = winnerPts;
                aSetPoints = loserPts;
                addMatchEvent(sport.getPeriodName() + " " + playedPeriod + ": " + homeTeam.getName() + " wins the set (" + hSetPoints + " - " + aSetPoints + ").");
            } else {
                awayScore++;
                aSetPoints = winnerPts;
                hSetPoints = loserPts;
                addMatchEvent(sport.getPeriodName() + " " + playedPeriod + ": " + awayTeam.getName() + " wins the set (" + hSetPoints + " - " + aSetPoints + ").");
            }

            setScores.add(hSetPoints + " - " + aSetPoints);
        } else {
            homeScore += hStrength;
            awayScore += aStrength;
            addScoreEvents(homeTeam, hStrength, playedPeriod, random);
            addScoreEvents(awayTeam, aStrength, playedPeriod, random);

            if (hStrength == 0 && aStrength == 0) {
                addMatchEvent(sport.getPeriodName() + " " + playedPeriod + ": Tight defensive period, no goals scored.");
            }
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
        if (homeScore > awayScore) {
            homeTeam.addPoints(sport.getPointsForWin());
            awayTeam.addPoints(sport.getPointsForLoss());
            homeTeam.recordWin(homeScore, awayScore);
            awayTeam.recordLoss(awayScore, homeScore);
        } else if (awayScore > homeScore) {
            awayTeam.addPoints(sport.getPointsForWin());
            homeTeam.addPoints(sport.getPointsForLoss());
            awayTeam.recordWin(awayScore, homeScore);
            homeTeam.recordLoss(homeScore, awayScore);
        } else {
            homeTeam.addPoints(sport.getPointsForDraw());
            awayTeam.addPoints(sport.getPointsForDraw());
            homeTeam.recordDraw(homeScore, awayScore);
            awayTeam.recordDraw(awayScore, homeScore);
        }

        applyMatchFatigue(homeTeam);
        applyMatchFatigue(awayTeam);

        addMatchEvent("Full time: " + getResult());

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
                addMatchEvent("Injury: " + unlucky.getName() + " from " + team.getName() + " is out for " + unlucky.getMatchesUntilFit() + " match(es).");
            }
        }
    }

    private void addScoreEvents(Team scoringTeam, int scoreCount, int period, Random random) {
        for (int i = 0; i < scoreCount; i++) {
            Player scorer = pickRandomAvailablePlayer(scoringTeam, random);
            if (scorer != null) {
                scorer.scoreGoal();
                addMatchEvent(sport.getPeriodName() + " " + period + ": GOAL - " + scoringTeam.getName() + " | " + scorer.getName());
            } else {
                addMatchEvent(sport.getPeriodName() + " " + period + ": GOAL - " + scoringTeam.getName());
            }
        }
    }

    private Player pickRandomAvailablePlayer(Team team, Random random) {
        List<Player> availablePlayers = team.getAvailablePlayers();
        if (availablePlayers.isEmpty()) {
            return null;
        }
        return availablePlayers.get(random.nextInt(availablePlayers.size()));
    }

    private void applyMatchFatigue(Team team) {
        for (Player player : team.getAvailablePlayers()) {
            player.playMatch();
        }
    }

    private void addMatchEvent(String event) {
        matchEvents.add(event);
        lastPeriodEvents.add(event);
    }

    private void updateMatchStats(int homePeriodScore, int awayPeriodScore, Random random) {
        int homePeriodShots = homePeriodScore + random.nextInt(4) + 1;
        int awayPeriodShots = awayPeriodScore + random.nextInt(4) + 1;

        homeShots += homePeriodShots;
        awayShots += awayPeriodShots;
        homeShotsOnTarget += Math.min(homePeriodShots, homePeriodScore + random.nextInt(2) + 1);
        awayShotsOnTarget += Math.min(awayPeriodShots, awayPeriodScore + random.nextInt(2) + 1);

        homeFouls += random.nextInt(3);
        awayFouls += random.nextInt(3);

        int homeMomentum = calculateTeamMomentum(homeTeam);
        int awayMomentum = calculateTeamMomentum(awayTeam);
        homePossession = clamp(50 + homeMomentum - awayMomentum + random.nextInt(11) - 5, 35, 65);
        awayPossession = 100 - homePossession;
    }

    private int calculateTeamMomentum(Team team) {
        int totalMorale = 0;
        int totalStamina = 0;
        List<Player> availablePlayers = team.getAvailablePlayers();

        if (availablePlayers.isEmpty()) {
            return 0;
        }

        for (Player player : availablePlayers) {
            totalMorale += player.getMorale();
            totalStamina += player.getStamina();
        }

        int averageMorale = totalMorale / availablePlayers.size();
        int averageStamina = totalStamina / availablePlayers.size();
        return ((averageMorale - 70) / 10) + ((averageStamina - 70) / 15);
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public int getCurrentPeriod() { return currentPeriod; }

    public String getResult() {
        return homeTeam.getName() + " " + homeScore + " - " +
                awayScore + " " + awayTeam.getName();
    }

    public boolean isPlayed() {
        return played;
    }

    public List<String> getMatchEvents() {
        return matchEvents;
    }

    public List<String> getLastPeriodEvents() {
        return lastPeriodEvents;
    }

    public int getHomeShots() {
        return homeShots;
    }

    public int getAwayShots() {
        return awayShots;
    }

    public int getHomeShotsOnTarget() {
        return homeShotsOnTarget;
    }

    public int getAwayShotsOnTarget() {
        return awayShotsOnTarget;
    }

    public int getHomePossession() {
        return homePossession;
    }

    public int getAwayPossession() {
        return awayPossession;
    }

    public int getHomeFouls() {
        return homeFouls;
    }

    public int getAwayFouls() {
        return awayFouls;
    }

    public String getLiveStatusText() {
        if (played) {
            return "FULL TIME";
        }

        return sport.getPeriodName().toUpperCase() + " " + currentPeriod;
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

    public List<String> getSetScores() {
        return setScores;
    }
}