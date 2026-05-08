import java.util.ArrayList;
import java.util.List;

public class Team implements java.io.Serializable {

    private int goalsFor;
    private int goalsAgainst;

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
    public void addMatchStats(int scored, int conceded) {
        this.goalsFor += scored;
        this.goalsAgainst += conceded;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    @Override
    public String toString() {
        return "Team: " + name +
                ", Points: " + points +
                ", Players: " + players.size();
    }
}