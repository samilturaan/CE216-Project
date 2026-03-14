import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Player> players;
    private Coach coach;
    private int points;

    public Team(Coach coach) {
        this.players = new ArrayList<>();
        this.coach = coach;
        this.points = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int p) {
        points += p;
    }

    public void trainPlayers() {
        System.out.println("Training players...");
    }

    public void selectTactics() {
        System.out.println("Selecting tactics...");
    }

}