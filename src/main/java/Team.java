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
    private List<Coach> coaches;
    private int points;
    private String tactic;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
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
                ", Players: " + players.size();
    }
}