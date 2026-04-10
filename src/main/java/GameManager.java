import java.util.Random;

public class GameManager {

    private League league;
    private ISport selectedSport;
    private Team userTeam;
    private Training training;

    public GameManager() {
        this.league = null;
        this.selectedSport = null;
        this.userTeam = null;
        this.training = new Training("General Training");
    }

    public void selectSport(ISport sport) {
        this.selectedSport = sport;
    }

    public ISport getSelectedSport() {
        return selectedSport;
    }

    public League getLeague() {
        return league;
    }

    public Team getUserTeam() {
        return userTeam;
    }

    public void startNewGame(String userTeamName) {
        if (selectedSport == null) {
            return;
        }

        league = new League(selectedSport);
        userTeam = new Team(userTeamName);

        generatePlayersForTeam(userTeam);
        userTeam.addCoach(new Coach("User Coach", 45, 5));
        league.addTeam(userTeam);

        for (int i = 1; i <= 3; i++) {
            Team cpuTeam = new Team("CPU Team " + i);
            generatePlayersForTeam(cpuTeam);
            cpuTeam.addCoach(new Coach("Coach " + i, 40 + i, 3 + i));
            league.addTeam(cpuTeam);
        }

        league.generateFixtures();
    }

    private void generatePlayersForTeam(Team team) {
        Random random = new Random();
        int playerCount = selectedSport.getPlayersOnField() + selectedSport.getSubstitutesCount();

        for (int i = 1; i <= playerCount; i++) {
            String position = "Player";
            int skill = random.nextInt(41) + 60;
            Player player = new Player(team.getName() + " Player " + i, 18 + random.nextInt(15), position, skill);
            team.addPlayer(player);
        }
    }

    public void trainUserTeam() {
        if (userTeam != null) {
            training.applyTraining(userTeam);
        }
    }

    public void playNextWeek() {
        if (league != null) {
            league.playNextWeek();
            recoverAllPlayers();
        }
    }

    private void recoverAllPlayers() {
        if (league == null) {
            return;
        }

        for (Team team : league.getTeams()) {
            for (Player player : team.getPlayers()) {
                if (player.isInjured()) {
                    player.recover();
                }
            }
        }
    }

    public boolean isSeasonFinished() {
        return league != null && league.seasonFinished();
    }
}
