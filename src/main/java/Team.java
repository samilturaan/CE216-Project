import java.util.ArrayList;
import java.util.List;

public class Team implements java.io.Serializable {

    private int goalsFor;
    private int goalsAgainst;
    private int wins;
    private int draws;
    private int losses;
    private List<String> recentForm;

    private String name;
    private List<Player> players;
    private List<Player> startingLineup;
    private List<Player> substitutes;
    private List<Coach> coaches;
    private int points;
    private String tactic;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.startingLineup = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.coaches = new ArrayList<>();
        this.points = 0;
        this.tactic = "Balanced";

        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.recentForm = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getStartingLineup() {
        return startingLineup;
    }

    public List<Player> getSubstitutes() {
        return substitutes;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public int getPoints() {
        return points;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public List<String> getRecentForm() {
        return recentForm;
    }

    public String getTactic() {
        return tactic;
    }

    public void setTactic(String tactic) {
        this.tactic = tactic;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void generateDefaultLineup() {
        startingLineup.clear();
        substitutes.clear();

        List<Player> availablePlayers = getAvailablePlayers();
        int targetLineupSize = getTargetLineupSize();

        if (isFootballTeam()) {
            addBestPlayersByPosition("Goalkeeper", 1, targetLineupSize);
            addBestPlayersByPosition("Defender", 4, targetLineupSize);
            addBestPlayersByPosition("Midfielder", 3, targetLineupSize);
            addBestPlayersByPosition("Forward", 3, targetLineupSize);
        } else if (isVolleyballTeam()) {
            addBestPlayersByPosition("Setter", 1, targetLineupSize);
            addBestPlayersByPosition("Outside Hitter", 2, targetLineupSize);
            addBestPlayersByPosition("Middle Blocker", 1, targetLineupSize);
            addBestPlayersByPosition("Opposite Hitter", 1, targetLineupSize);
            addBestPlayersByPosition("Libero", 1, targetLineupSize);
        }

        for (Player player : availablePlayers) {
            if (startingLineup.size() < targetLineupSize && !startingLineup.contains(player)) {
                startingLineup.add(player);
            }
        }

        for (Player player : players) {
            if (!startingLineup.contains(player)) {
                substitutes.add(player);
            }
        }
    }

    private int getTargetLineupSize() {
        if (isVolleyballTeam()) {
            return 6;
        }
        return 11;
    }

    private boolean isFootballTeam() {
        return hasPosition("Goalkeeper") || hasPosition("Defender") || hasPosition("Midfielder") || hasPosition("Forward");
    }

    private boolean isVolleyballTeam() {
        return hasPosition("Setter") || hasPosition("Outside Hitter") || hasPosition("Middle Blocker") || hasPosition("Opposite Hitter") || hasPosition("Libero");
    }

    private boolean hasPosition(String position) {
        for (Player player : players) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                return true;
            }
        }
        return false;
    }

    private void addBestPlayersByPosition(String position, int count, int targetLineupSize) {
        List<Player> matchingPlayers = new ArrayList<>();

        for (Player player : getAvailablePlayers()) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                matchingPlayers.add(player);
            }
        }

        matchingPlayers.sort((p1, p2) -> Integer.compare(p2.getSkillLevel(), p1.getSkillLevel()));

        for (Player player : matchingPlayers) {
            if (startingLineup.size() >= targetLineupSize || count <= 0) {
                return;
            }

            if (!startingLineup.contains(player)) {
                startingLineup.add(player);
                count--;
            }
        }
    }

    public boolean substitutePlayer(Player playerOut, Player playerIn) {
        if (playerOut == null || playerIn == null) {
            return false;
        }

        if (playerIn.isInjured()) {
            return false;
        }

        if (!startingLineup.contains(playerOut) || !substitutes.contains(playerIn)) {
            return false;
        }

        int outIndex = startingLineup.indexOf(playerOut);
        int inIndex = substitutes.indexOf(playerIn);

        startingLineup.set(outIndex, playerIn);
        substitutes.set(inIndex, playerOut);
        return true;
    }

    public boolean isInStartingLineup(Player player) {
        return startingLineup.contains(player);
    }

    public boolean isSubstitute(Player player) {
        return substitutes.contains(player);
    }

    public List<Player> getAvailableStartingLineup() {
        List<Player> available = new ArrayList<>();
        for (Player player : startingLineup) {
            if (player.isAvailable()) {
                available.add(player);
            }
        }
        return available;
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void addPoints(int pts) {
        points += pts;
    }

    public List<Player> getAvailablePlayers() {
        List<Player> available = new ArrayList<>();
        for (Player p : players) {
            if (p.isAvailable()) {
                available.add(p);
            }
        }
        return available;
    }
    public void recordWin(int scored, int conceded) {
        wins++;
        addMatchStats(scored, conceded);
        addFormResult("W");

        for (Player player : getAvailablePlayers()) {
            player.increaseMorale(3);
            player.recoverStamina();
        }
    }

    public void recordDraw(int scored, int conceded) {
        draws++;
        addMatchStats(scored, conceded);
        addFormResult("D");

        for (Player player : getAvailablePlayers()) {
            player.increaseMorale(1);
            player.recoverStamina();
        }
    }

    public void recordLoss(int scored, int conceded) {
        losses++;
        addMatchStats(scored, conceded);
        addFormResult("L");

        for (Player player : getAvailablePlayers()) {
            player.decreaseMorale(3);
            player.recoverStamina();
        }
    }

    public void addMatchStats(int scored, int conceded) {
        this.goalsFor += scored;
        this.goalsAgainst += conceded;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public void addFormResult(String result) {
        recentForm.add(result);

        if (recentForm.size() > 5) {
            recentForm.remove(0);
        }
    }

    public String getFormString() {
        if (recentForm.isEmpty()) {
            return "-";
        }

        StringBuilder builder = new StringBuilder();
        for (String result : recentForm) {
            builder.append(result).append(" ");
        }

        return builder.toString().trim();
    }

    public int getTotalGoalsScoredByPlayers() {
        int total = 0;
        for (Player player : players) {
            total += player.getGoalsScored();
        }
        return total;
    }

    public Player getTopScorer() {
        if (players.isEmpty()) {
            return null;
        }

        Player top = players.get(0);
        for (Player player : players) {
            if (player.getGoalsScored() > top.getGoalsScored()) {
                top = player;
            }
        }

        return top;
    }

    @Override
    public String toString() {
        return "Team: " + name +
                ", Points: " + points +
                ", W-D-L: " + wins + "-" + draws + "-" + losses +
                ", GD: " + getGoalDifference() +
                ", Players: " + players.size() +
                ", Starting Lineup: " + startingLineup.size() +
                ", Subs: " + substitutes.size();
    }
}