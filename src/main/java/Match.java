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
    private List<LivePeriodEvent> pendingLiveEvents;
    private String pendingSetScore;

    private int homeShots;
    private int awayShots;
    private int homeShotsOnTarget;
    private int awayShotsOnTarget;
    private int homePossession;
    private int awayPossession;
    private int homeFouls;
    private int awayFouls;

    private static class LivePeriodEvent implements java.io.Serializable {
        private String text;
        private int homeScoreDelta;
        private int awayScoreDelta;
        private Player scorer;

        private LivePeriodEvent(String text, int homeScoreDelta, int awayScoreDelta, Player scorer) {
            this.text = text;
            this.homeScoreDelta = homeScoreDelta;
            this.awayScoreDelta = awayScoreDelta;
            this.scorer = scorer;
        }
    }

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
        this.pendingLiveEvents = new ArrayList<>();
        this.pendingSetScore = null;
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

        prepareNextPeriodForLive();
        while (hasPendingLiveEvents()) {
            revealNextLiveEvent();
        }
    }

    public void prepareNextPeriodForLive() {
        if (played || hasPendingLiveEvents()) {
            return;
        }

        Random random = new Random();
        int playedPeriod = currentPeriod;
        lastPeriodEvents.clear();
        pendingLiveEvents.clear();
        pendingSetScore = null;
        ensureLineupsReady();

        int hAttackBonus = getTacticBonus(homeTeam.getTactic(), true);
        int aAttackBonus = getTacticBonus(awayTeam.getTactic(), true);

        int hDefBonus = getTacticBonus(homeTeam.getTactic(), false);
        int aDefBonus = getTacticBonus(awayTeam.getTactic(), false);

        int hStrength = Math.max(0, random.nextInt(3) + hAttackBonus - aDefBonus);
        int aStrength = Math.max(0, random.nextInt(3) + aAttackBonus - hDefBonus);

        if (!sport.getSportName().equals("Handball")) {
            updateMatchStats(hStrength, aStrength, random);
        }

        if (sport.getSportName().equals("Volleyball")) {
            prepareVolleyballSetEvents(playedPeriod, hStrength, aStrength, random);
        } else if (sport.getSportName().equals("Handball")) {
            prepareHandballPeriodEvents(playedPeriod, hAttackBonus, aAttackBonus, hDefBonus, aDefBonus, random);
        } else {
            prepareFootballPeriodEvents(playedPeriod, hStrength, aStrength, random);
        }
    }

    private void prepareFootballPeriodEvents(int playedPeriod, int hStrength, int aStrength, Random random) {
        addPreparedGoalEvents(homeTeam, hStrength, playedPeriod, random, true);
        addPreparedGoalEvents(awayTeam, aStrength, playedPeriod, random, false);

        if (hStrength == 0 && aStrength == 0) {
            pendingLiveEvents.add(new LivePeriodEvent(sport.getPeriodName() + " " + playedPeriod + ": Tight defensive period, no goals scored.", 0, 0, null));
        }
    }

    private void prepareHandballPeriodEvents(int playedPeriod, int hAttackBonus, int aAttackBonus, int hDefBonus, int aDefBonus, Random random) {
        int homeBaseGoals = 10 + random.nextInt(7);
        int awayBaseGoals = 10 + random.nextInt(7);

        int homePeriodGoals = clamp(homeBaseGoals + hAttackBonus - aDefBonus, 6, 20);
        int awayPeriodGoals = clamp(awayBaseGoals + aAttackBonus - hDefBonus, 6, 20);

        updateHandballMatchStats(homePeriodGoals, awayPeriodGoals, random);

        addPreparedHandballGoalEvents(homeTeam, homePeriodGoals, playedPeriod, random, true);
        addPreparedHandballGoalEvents(awayTeam, awayPeriodGoals, playedPeriod, random, false);

        if (homePeriodGoals == awayPeriodGoals) {
            pendingLiveEvents.add(new LivePeriodEvent(sport.getPeriodName() + " " + playedPeriod + ": End-to-end handball period, both teams stay level.", 0, 0, null));
        }
    }

    private void addPreparedHandballGoalEvents(Team scoringTeam, int scoreCount, int period, Random random, boolean homeScored) {
        String[] actions = {
                "Fast break goal",
                "Wing shot scores",
                "Backcourt shot finds the corner",
                "Pivot finish from six meters",
                "7-meter penalty scored",
                "Quick transition goal"
        };

        for (int i = 0; i < scoreCount; i++) {
            Player scorer = pickRandomAvailablePlayer(scoringTeam, random);
            String action = actions[random.nextInt(actions.length)];
            String event;

            if (scorer != null) {
                event = sport.getPeriodName() + " " + period + ": " + action + " - " + scoringTeam.getName() + " | " + scorer.getName();
            } else {
                event = sport.getPeriodName() + " " + period + ": " + action + " - " + scoringTeam.getName();
            }

            pendingLiveEvents.add(new LivePeriodEvent(event, homeScored ? 1 : 0, homeScored ? 0 : 1, scorer));

            if (i % 5 == 4 && random.nextBoolean()) {
                Team defendingTeam = homeScored ? awayTeam : homeTeam;
                Player keeper = pickRandomAvailablePlayer(defendingTeam, random);
                String saveEvent;
                if (keeper != null) {
                    saveEvent = sport.getPeriodName() + " " + period + ": Goalkeeper save - " + defendingTeam.getName() + " | " + keeper.getName();
                } else {
                    saveEvent = sport.getPeriodName() + " " + period + ": Goalkeeper save - " + defendingTeam.getName();
                }
                pendingLiveEvents.add(new LivePeriodEvent(saveEvent, 0, 0, null));
            }
        }
    }

    private void updateHandballMatchStats(int homePeriodGoals, int awayPeriodGoals, Random random) {
        int homeAttempts = homePeriodGoals + 6 + random.nextInt(7);
        int awayAttempts = awayPeriodGoals + 6 + random.nextInt(7);

        homeShots += homeAttempts;
        awayShots += awayAttempts;
        homeShotsOnTarget += Math.min(homeAttempts, homePeriodGoals + 3 + random.nextInt(4));
        awayShotsOnTarget += Math.min(awayAttempts, awayPeriodGoals + 3 + random.nextInt(4));

        homeFouls += 4 + random.nextInt(6);
        awayFouls += 4 + random.nextInt(6);

        int homeMomentum = calculateTeamMomentum(homeTeam);
        int awayMomentum = calculateTeamMomentum(awayTeam);
        homePossession = clamp(50 + homeMomentum - awayMomentum + random.nextInt(9) - 4, 42, 58);
        awayPossession = 100 - homePossession;
    }

    private void prepareVolleyballSetEvents(int playedPeriod, int hStrength, int aStrength, Random random) {
        if (hStrength == aStrength) {
            if (random.nextBoolean()) hStrength++; else aStrength++;
        }

        int targetScore = (playedPeriod == 5) ? 15 : 25;
        int hSetPoints;
        int aSetPoints;

        boolean isDeuce = random.nextInt(5) == 0;
        int winnerPts = targetScore;
        int loserPts = targetScore - 2 - random.nextInt(6);

        if (isDeuce) {
            winnerPts = targetScore + 1 + random.nextInt(4);
            loserPts = winnerPts - 2;
        }

        boolean homeWinsSet = hStrength > aStrength;
        Team winningTeam = homeWinsSet ? homeTeam : awayTeam;
        Team losingTeam = homeWinsSet ? awayTeam : homeTeam;

        if (homeWinsSet) {
            hSetPoints = winnerPts;
            aSetPoints = loserPts;
        } else {
            aSetPoints = winnerPts;
            hSetPoints = loserPts;
        }

        pendingSetScore = hSetPoints + " - " + aSetPoints;

        addPreparedVolleyballRallyEvent(playedPeriod, winningTeam, random, "Service ace", 0, 0);
        addPreparedVolleyballRallyEvent(playedPeriod, losingTeam, random, "Strong reception keeps the rally alive", 0, 0);
        addPreparedVolleyballRallyEvent(playedPeriod, winningTeam, random, "Power spike finds the floor", 0, 0);

        if (isDeuce) {
            addPreparedVolleyballRallyEvent(playedPeriod, losingTeam, random, "Clutch block forces deuce", 0, 0);
            addPreparedVolleyballRallyEvent(playedPeriod, winningTeam, random, "Set point converted after a long rally", homeWinsSet ? 1 : 0, homeWinsSet ? 0 : 1);
        } else {
            addPreparedVolleyballRallyEvent(playedPeriod, winningTeam, random, "Monster block shifts the momentum", 0, 0);
            addPreparedVolleyballRallyEvent(playedPeriod, winningTeam, random, "Set point converted", homeWinsSet ? 1 : 0, homeWinsSet ? 0 : 1);
        }

        String setSummary = sport.getPeriodName() + " " + playedPeriod + ": " + winningTeam.getName() +
                " wins the set (" + hSetPoints + " - " + aSetPoints + ").";
        pendingLiveEvents.add(new LivePeriodEvent(setSummary, 0, 0, null));
    }

    private void addPreparedVolleyballRallyEvent(int period, Team team, Random random, String action, int homeDelta, int awayDelta) {
        Player player = pickRandomAvailablePlayer(team, random);
        String event;

        if (player != null) {
            event = sport.getPeriodName() + " " + period + ": " + action + " - " + team.getName() + " | " + player.getName();
        } else {
            event = sport.getPeriodName() + " " + period + ": " + action + " - " + team.getName();
        }

        pendingLiveEvents.add(new LivePeriodEvent(event, homeDelta, awayDelta, null));
    }

    private void addPreparedGoalEvents(Team scoringTeam, int scoreCount, int period, Random random, boolean homeScored) {
        for (int i = 0; i < scoreCount; i++) {
            Player scorer = pickRandomAvailablePlayer(scoringTeam, random);
            String event;
            if (scorer != null) {
                event = sport.getPeriodName() + " " + period + ": GOAL - " + scoringTeam.getName() + " | " + scorer.getName();
            } else {
                event = sport.getPeriodName() + " " + period + ": GOAL - " + scoringTeam.getName();
            }

            pendingLiveEvents.add(new LivePeriodEvent(event, homeScored ? 1 : 0, homeScored ? 0 : 1, scorer));
        }
    }

    public boolean hasPendingLiveEvents() {
        return !pendingLiveEvents.isEmpty();
    }

    public int getPendingLiveEventCount() {
        return pendingLiveEvents.size();
    }

    public String revealNextLiveEvent() {
        if (pendingLiveEvents.isEmpty() || played) {
            return null;
        }

        LivePeriodEvent event = pendingLiveEvents.remove(0);

        homeScore += event.homeScoreDelta;
        awayScore += event.awayScoreDelta;

        if (event.scorer != null && (event.homeScoreDelta > 0 || event.awayScoreDelta > 0)) {
            event.scorer.scoreGoal();
        }

        addMatchEvent(event.text);

        if (pendingLiveEvents.isEmpty()) {
            finishPreparedPeriod();
        }

        return event.text;
    }

    private void finishPreparedPeriod() {
        if (pendingSetScore != null) {
            setScores.add(pendingSetScore);
            pendingSetScore = null;
        }

        currentPeriod++;

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

    private void ensureLineupsReady() {
        if (homeTeam.getStartingLineup().isEmpty()) {
            homeTeam.generateDefaultLineup();
        }

        if (awayTeam.getStartingLineup().isEmpty()) {
            awayTeam.generateDefaultLineup();
        }
    }

    private void injureRandomPlayer(Team team, Random random) {
        if (random.nextInt(5) == 0) { // %20 chance
            List<Player> available = team.getAvailableStartingLineup();
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
        List<Player> availablePlayers = team.getAvailableStartingLineup();
        if (availablePlayers.isEmpty()) {
            return null;
        }
        return availablePlayers.get(random.nextInt(availablePlayers.size()));
    }

    private void applyMatchFatigue(Team team) {
        for (Player player : team.getAvailableStartingLineup()) {
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
        List<Player> availablePlayers = team.getAvailableStartingLineup();

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

    public String getPendingSetScore() {
        return pendingSetScore;
    }
}