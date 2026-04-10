import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SportsManagerTest {

    private Football football;
    private Volleyball volleyball;
    private Team team;
    private Player player;
    private Coach coach;
    private League league;
    private GameManager gameManager;

    @BeforeEach
    public void setUp() {
        football   = new Football();
        volleyball = new Volleyball();
        team       = new Team("Test Team");
        player     = new Player("Ali Yilmaz", 22, "Forward", 75);
        coach      = new Coach("Ahmet Hoca", 45, 5);
        league     = new League(football);
        gameManager = new GameManager();
    }

    // ===========================
    // Football (ISport) Tests
    // ===========================

    @Test
    public void testFootballSportName() {
        assertEquals("Football", football.getSportName());
    }

    @Test
    public void testFootballPlayersOnField() {
        assertEquals(11, football.getPlayersOnField());
    }

    @Test
    public void testFootballSubstitutesCount() {
        assertEquals(5, football.getSubstitutesCount());
    }

    @Test
    public void testFootballPointsForWin() {
        assertEquals(3, football.getPointsForWin());
    }

    @Test
    public void testFootballPointsForDraw() {
        assertEquals(1, football.getPointsForDraw());
    }

    @Test
    public void testFootballPointsForLoss() {
        assertEquals(0, football.getPointsForLoss());
    }

    // ===========================
    // Volleyball Tests
    // ===========================

    @Test
    public void testVolleyballSportName() {
        assertEquals("Volleyball", volleyball.getSportName());
    }

    @Test
    public void testVolleyballPlayersOnField() {
        assertEquals(6, volleyball.getPlayersOnField());
    }

    @Test
    public void testVolleyballNoDrawPoints() {
        assertEquals(0, volleyball.getPointsForDraw());
    }

    @Test
    public void testVolleyballWinPoints() {
        assertEquals(3, volleyball.getPointsForWin());
    }

    // ===========================
    // Player Tests
    // ===========================

    @Test
    public void testPlayerInitialAvailability() {
        assertTrue(player.isAvailable());
        assertFalse(player.isInjured());
    }

    @Test
    public void testPlayerTrainIncreasesSkill() {
        int before = player.getSkillLevel();
        player.train();
        assertEquals(before + 1, player.getSkillLevel());
    }

    @Test
    public void testPlayerMultipleTrains() {
        int before = player.getSkillLevel();
        player.train();
        player.train();
        player.train();
        assertEquals(before + 3, player.getSkillLevel());
    }

    @Test
    public void testPlayerInjuryMakesUnavailable() {
        player.injure(3);
        assertTrue(player.isInjured());
        assertFalse(player.isAvailable());
        assertEquals(3, player.getMatchesUntilFit());
    }

    @Test
    public void testPlayerRecoversSingleMatch() {
        player.injure(1);
        player.recover();
        assertFalse(player.isInjured());
        assertTrue(player.isAvailable());
    }

    @Test
    public void testPlayerRecoveryIsGradual() {
        player.injure(3);
        player.recover();
        assertTrue(player.isInjured()); // 2 matches left
        assertEquals(2, player.getMatchesUntilFit());
        player.recover();
        player.recover();
        assertFalse(player.isInjured()); // now fit
    }

    @Test
    public void testPlayerPositionAndName() {
        assertEquals("Forward", player.getPosition());
        assertEquals("Ali Yilmaz", player.getName());
        assertEquals(22, player.getAge());
    }

    // ===========================
    // Coach Tests
    // ===========================

    @Test
    public void testCoachInitialState() {
        assertEquals("Ahmet Hoca", coach.getName());
        assertEquals(45, coach.getAge());
        assertEquals(5, coach.getExperienceLevel());
    }

    @Test
    public void testCoachImproveExperience() {
        coach.improveExperience();
        assertEquals(6, coach.getExperienceLevel());
    }

    @Test
    public void testCoachMultipleImprovements() {
        coach.improveExperience();
        coach.improveExperience();
        assertEquals(7, coach.getExperienceLevel());
    }

    // ===========================
    // Team Tests
    // ===========================

    @Test
    public void testTeamStartsWithZeroPoints() {
        assertEquals(0, team.getPoints());
    }

    @Test
    public void testTeamAddPoints() {
        team.addPoints(3);
        team.addPoints(1);
        assertEquals(4, team.getPoints());
    }

    @Test
    public void testTeamAddPlayer() {
        team.addPlayer(player);
        assertEquals(1, team.getPlayers().size());
        assertEquals("Ali Yilmaz", team.getPlayers().get(0).getName());
    }

    @Test
    public void testTeamAddCoach() {
        team.addCoach(coach);
        assertEquals(1, team.getCoaches().size());
    }

    @Test
    public void testTeamAvailablePlayersExcludesInjured() {
        Player injured = new Player("Mehmet", 25, "Defender", 70);
        injured.injure(2);
        team.addPlayer(player);
        team.addPlayer(injured);
        List<Player> available = team.getAvailablePlayers();
        assertEquals(1, available.size());
        assertEquals("Ali Yilmaz", available.get(0).getName());
    }

    @Test
    public void testTeamSetTactic() {
        team.setTactic("Attacking");
        assertEquals("Attacking", team.getTactic());
    }

    @Test
    public void testTeamDefaultTactic() {
        assertEquals("Balanced", team.getTactic());
    }

    // ===========================
    // Match Tests
    // ===========================

    @Test
    public void testMatchInitiallyNotPlayed() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Match match = new Match(home, away, football);
        assertFalse(match.isPlayed());
    }

    @Test
    public void testMatchSimulationSetsPlayed() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Match match = new Match(home, away, football);
        match.simulateMatch();
        assertTrue(match.isPlayed());
    }

    @Test
    public void testMatchNotSimulatedTwice() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Match match = new Match(home, away, football);
        match.simulateMatch();
        int h = match.getHomeScore();
        int a = match.getAwayScore();
        match.simulateMatch(); // ikinci çağrı bir şey değiştirmemeli
        assertEquals(h, match.getHomeScore());
        assertEquals(a, match.getAwayScore());
    }

    @Test
    public void testMatchResultContainsTeamNames() {
        Team home = new Team("HomeFC");
        Team away = new Team("AwayFC");
        Match match = new Match(home, away, football);
        match.simulateMatch();
        assertTrue(match.getResult().contains("HomeFC"));
        assertTrue(match.getResult().contains("AwayFC"));
    }

    @Test
    public void testMatchTotalPointsAreValid() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Match match = new Match(home, away, football);
        match.simulateMatch();
        int total = home.getPoints() + away.getPoints();
        // Ya galibiyet (3+0=3) ya da beraberlik (1+1=2)
        assertTrue(total == 2 || total == 3);
    }

    // ===========================
    // League Tests
    // ===========================

    @Test
    public void testLeagueFixtureCountForThreeTeams() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.addTeam(new Team("T3"));
        league.generateFixtures();
        assertEquals(3, league.getFixtures().size()); // 3C2 = 3
    }

    @Test
    public void testLeagueStandingsOrderedByPoints() {
        Team t1 = new Team("Leader");
        Team t2 = new Team("Follower");
        t1.addPoints(9);
        t2.addPoints(3);
        league.addTeam(t1);
        league.addTeam(t2);
        league.generateFixtures();
        List<Team> standings = league.getStandings();
        assertEquals("Leader", standings.get(0).getName());
    }

    @Test
    public void testLeagueSeasonFinishedAfterAllMatches() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.generateFixtures();
        assertFalse(league.seasonFinished());
        league.playNextWeek();
        assertTrue(league.seasonFinished());
    }

    @Test
    public void testLeagueCurrentWeekAdvances() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.addTeam(new Team("T3"));
        league.generateFixtures();
        assertEquals(0, league.getCurrentWeek());
        league.playNextWeek();
        assertEquals(1, league.getCurrentWeek());
    }

    // ===========================
    // Training Tests
    // ===========================

    @Test
    public void testTrainingImprovesSkill() {
        team.addPlayer(player);
        int before = player.getSkillLevel();
        Training training = new Training("Fitness");
        training.applyTraining(team);
        assertTrue(player.getSkillLevel() > before);
    }

    @Test
    public void testTrainingSkipsInjuredPlayers() {
        player.injure(3);
        team.addPlayer(player);
        int before = player.getSkillLevel();
        new Training("Fitness").applyTraining(team);
        assertEquals(before, player.getSkillLevel()); // yaralı oyuncu antrenman yapmaz
    }

    // ===========================
    // GameManager Tests
    // ===========================

    @Test
    public void testGameManagerSelectSport() {
        gameManager.selectSport(football);
        assertEquals("Football", gameManager.getSelectedSport().getSportName());
    }

    @Test
    public void testGameManagerStartCreatesTeam() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertNotNull(gameManager.getUserTeam());
        assertEquals("My Team", gameManager.getUserTeam().getName());
    }

    @Test
    public void testGameManagerStartCreatesLeague() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertNotNull(gameManager.getLeague());
    }

    @Test
    public void testGameManagerSeasonNotFinishedAtStart() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertFalse(gameManager.isSeasonFinished());
    }

    @Test
    public void testGameManagerPlayNextWeekAdvancesLeague() {
        gameManager.selectSport(volleyball);
        gameManager.startNewGame("My Team");
        int before = gameManager.getLeague().getCurrentWeek();
        gameManager.playNextWeek();
        assertEquals(before + 1, gameManager.getLeague().getCurrentWeek());
    }

    @Test
    public void testGameManagerTrainDoesNotThrow() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertDoesNotThrow(() -> gameManager.trainUserTeam());
    }

    @Test
    public void testGameManagerTeamHasPlayers() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertFalse(gameManager.getUserTeam().getPlayers().isEmpty());
    }

    @Test
    public void testGameManagerSwitchSport() {
        gameManager.selectSport(football);
        gameManager.selectSport(volleyball);
        assertEquals("Volleyball", gameManager.getSelectedSport().getSportName());
    }
}
