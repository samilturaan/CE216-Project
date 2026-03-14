import java.util.ArrayList;
import java.util.List;

public class League {

    private List<Team> teams;

    public League() {
        teams = new ArrayList<>();
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void generateFixtures(ISport sport) {

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {

                Match match = new Match(teams.get(i), teams.get(j), sport);
                match.simulateMatch();

            }
        }
    }

    public void updateStandings() {

        for (Team t : teams) {
            System.out.println("Points: " + t.getPoints());
        }

    }

}