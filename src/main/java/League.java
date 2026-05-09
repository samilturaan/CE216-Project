import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class League implements java.io.Serializable {

    private List<Team> teams;
    private List<Match> fixtures;
    private List<Match> lastWeekMatches;
    private int currentWeek;
    private ISport sport;

    public League(ISport sport) {
        this.sport = sport;
        this.teams = new ArrayList<>();
        this.fixtures = new ArrayList<>();
        this.lastWeekMatches = new ArrayList<>();
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

    public List<Match> getLastWeekMatches() {
        return lastWeekMatches;
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

        if (seasonFinished()) {
            return;
        }

        lastWeekMatches.clear();
        List<Team> teamsPlayedThisWeek = new ArrayList<>();

        for (Match match : fixtures) {
            if (!match.isPlayed()
                    && !teamsPlayedThisWeek.contains(match.getHomeTeam())
                    && !teamsPlayedThisWeek.contains(match.getAwayTeam())) {

                match.simulateMatch();
                lastWeekMatches.add(match);
                teamsPlayedThisWeek.add(match.getHomeTeam());
                teamsPlayedThisWeek.add(match.getAwayTeam());
            }
        }

        currentWeek++;
    }

    public List<Team> getStandings() {

        List<Team> table = new ArrayList<>(teams);

        Collections.sort(table, new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {

                if (t1.getPoints() != t2.getPoints()) {
                    return Integer.compare(t2.getPoints(), t1.getPoints());
                }

                int h2hT1 = calculateHeadToHeadPoints(t1, t2);
                int h2hT2 = calculateHeadToHeadPoints(t2, t1);

                if (h2hT1 != h2hT2) {
                    return Integer.compare(h2hT2, h2hT1);
                }

                if (t1.getGoalDifference() != t2.getGoalDifference()) {
                    return Integer.compare(t2.getGoalDifference(), t1.getGoalDifference());
                }

                if (t1.getGoalsFor() != t2.getGoalsFor()) {
                    return Integer.compare(t2.getGoalsFor(), t1.getGoalsFor());
                }

                return t1.getName().compareTo(t2.getName());
            }
        });
        return table;
    }

    private int calculateHeadToHeadPoints(Team team, Team opponent) {
        int points = 0;

        for (Match match : fixtures) {
            if (!match.isPlayed()) {
                continue;
            }

            boolean teamHome = match.getHomeTeam() == team && match.getAwayTeam() == opponent;
            boolean teamAway = match.getAwayTeam() == team && match.getHomeTeam() == opponent;

            if (teamHome) {
                if (match.getHomeScore() > match.getAwayScore()) {
                    points += sport.getPointsForWin();
                } else if (match.getHomeScore() == match.getAwayScore()) {
                    points += sport.getPointsForDraw();
                }
            } else if (teamAway) {
                if (match.getAwayScore() > match.getHomeScore()) {
                    points += sport.getPointsForWin();
                } else if (match.getAwayScore() == match.getHomeScore()) {
                    points += sport.getPointsForDraw();
                }
            }
        }

        return points;
    }

    public boolean seasonFinished() {
        for (Match match : fixtures) {
            if (!match.isPlayed()) {
                return false;
            }
        }
        return true;
    }
}