import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class League implements java.io.Serializable {

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
        List<Match> firstHalf = new ArrayList<>();
        List<Match> secondHalf = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {

            for (int j = i + 1; j < teams.size(); j++) {
                firstHalf.add(new Match(teams.get(i), teams.get(j), sport));
                secondHalf.add(new Match(teams.get(j), teams.get(i), sport));
            }
        }

        fixtures.addAll(firstHalf);
        fixtures.addAll(secondHalf);

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

                if (t1.getPoints() != t2.getPoints()) {
                    return t2.getPoints() - t1.getPoints();
                }

                int h2hT1 = 0;
                int h2hT2 = 0;
                for (Match m : fixtures) {
                    if (m.isPlayed()) {
                        if (m.getHomeTeam() == t1 && m.getAwayTeam() == t2) {
                            if (m.getHomeScore() > m.getAwayScore()) h2hT1 += sport.getPointsForWin();
                            else if (m.getHomeScore() < m.getAwayScore()) h2hT2 += sport.getPointsForWin();

                        } else if (m.getHomeTeam() == t2 && m.getAwayTeam() == t1) {
                            if (m.getAwayScore() > m.getHomeScore()) h2hT1 += sport.getPointsForWin();
                            else if (m.getAwayScore() < m.getHomeScore()) h2hT2 += sport.getPointsForWin();

                        }
                    }
                }

                if (h2hT1 != h2hT2) {
                    return h2hT2 - h2hT1;
                }

                if (t1.getGoalDifference() != t2.getGoalDifference()) {
                    return t2.getGoalDifference() - t1.getGoalDifference();
                }

                return t1.getName().compareTo(t2.getName());
            }
        });
        return table;
    }

    public boolean seasonFinished() {
        return currentWeek >= fixtures.size();
    }
}