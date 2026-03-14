public class GameManager {

    private ISport sport;
    private League league;

    public void startNewGame() {

        selectSport();

        league = createLeague();

        manageWeeklyCycle();

    }

    public void selectSport() {

        sport = new Football();

    }

    public League createLeague() {

        League league = new League();

        Coach c1 = new Coach("Coach1",45);
        Coach c2 = new Coach("Coach2",50);

        Team t1 = new Team(c1);
        Team t2 = new Team(c2);

        league.addTeam(t1);
        league.addTeam(t2);

        return league;
    }

    public void manageWeeklyCycle() {

        coordinateMatchesTraining();

    }

    public void coordinateMatchesTraining() {

        league.generateFixtures(sport);

        league.updateStandings();

    }

}