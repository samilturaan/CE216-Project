import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class League {

    private List<Team> teams;
    private List<Match> fixtures;
    private int currentWeek;
    private ISport sport;

    public League(ISport sport) {
        this.sport = sport;
        this.teams = new ArrayList<>();
        this.fixtures = new ArrayList<>();
        this.currentWeek = 0;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Match> getFixtures() {
        return fixtures;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void generateFixtures() {

        fixtures.clear();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Match match = new Match(teams.get(i), teams.get(j), sport);
                fixtures.add(match);
            }
        }
    }

    public void playNextWeek() {

        if (currentWeek >= fixtures.size()) {
            return;
        }

        Match match = fixtures.get(currentWeek);
        match.simulateMatch();
        currentWeek++;
    }

    public List<Team> getStandings() {

        List<Team> table = new ArrayList<>(teams);

        Collections.sort(table, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                return t2.getPoints() - t1.getPoints();
            }
        });

        return table;
    }

    public boolean seasonFinished() {
        return currentWeek >= fixtures.size();
    }
}
