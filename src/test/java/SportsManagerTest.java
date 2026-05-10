import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SportsManagerTest {

    private Football football;
    private Volleyball volleyball;
    private Handball handball;
    private Team team;
    private Player player;
    private Coach coach;
    private League league;
    private GameManager gameManager;

    @BeforeEach
    public void setUp() {
        football   = new Football();
        volleyball = new Volleyball();
        handball   = new Handball();
        team       = new Team("Test Team");
        player     = new Player("Ali Yilmaz", 22, "Forward", 75);
        coach      = new Coach("Ahmet Hoca", 45, 5);
        league     = new League(football);
        gameManager = new GameManager();
    }

    // Football tests

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

    // volleyball tests

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

    // Handball tests

    @Test
    public void testHandballSportName() {
        assertEquals("Handball", handball.getSportName());
    }

    @Test
    public void testHandballPlayersOnField() {
        assertEquals(7, handball.getPlayersOnField());
    }

    @Test
    public void testHandballSubstitutesCount() {
        assertEquals(7, handball.getSubstitutesCount());
    }

    @Test
    public void testHandballPointsForWinDrawLoss() {
        assertEquals(2, handball.getPointsForWin());
        assertEquals(1, handball.getPointsForDraw());
        assertEquals(0, handball.getPointsForLoss());
    }

    // Player Tests
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

    @Test
    public void testPlayerInitialStaminaMoraleAndStats() {
        assertEquals(100, player.getStamina());
        assertEquals(70, player.getMorale());
        assertEquals(0, player.getGoalsScored());
        assertEquals(0, player.getMatchesPlayed());
    }

    @Test
    public void testPlayerPlayMatchUpdatesFatigueStats() {
        player.playMatch();
        assertEquals(1, player.getMatchesPlayed());
        assertTrue(player.getStamina() < 100);
        assertTrue(player.getMorale() < 70);
    }

    @Test
    public void testPlayerScoreGoalUpdatesGoalAndMorale() {
        player.scoreGoal();
        assertEquals(1, player.getGoalsScored());
        assertTrue(player.getMorale() > 70);
    }

    @Test
    public void testPlayerStaminaAndMoraleAreClamped() {
        player.setStamina(150);
        player.setMorale(-20);
        assertEquals(100, player.getStamina());
        assertEquals(0, player.getMorale());
    }

    @Test
    public void testPlayerRecoverStaminaDoesNotExceedHundred() {
        player.setStamina(95);
        player.recoverStamina();
        assertEquals(100, player.getStamina());
    }

    // Coach Tests
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

    // team tests
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

    @Test
    public void testTeamRecordWinUpdatesStats() {
        team.recordWin(3, 1);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
        assertEquals(3, team.getGoalsFor());
        assertEquals(1, team.getGoalsAgainst());
        assertEquals(2, team.getGoalDifference());
    }

    @Test
    public void testTeamRecordDrawUpdatesStats() {
        team.recordDraw(2, 2);
        assertEquals(0, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(0, team.getLosses());
        assertEquals(2, team.getGoalsFor());
        assertEquals(2, team.getGoalsAgainst());
        assertEquals(0, team.getGoalDifference());
    }

    @Test
    public void testTeamRecordLossUpdatesStats() {
        team.recordLoss(0, 4);
        assertEquals(0, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(1, team.getLosses());
        assertEquals(0, team.getGoalsFor());
        assertEquals(4, team.getGoalsAgainst());
        assertEquals(-4, team.getGoalDifference());
    }

    @Test
    public void testTeamRecentFormKeepsOnlyLastFiveResults() {
        team.addFormResult("W");
        team.addFormResult("D");
        team.addFormResult("L");
        team.addFormResult("W");
        team.addFormResult("W");
        team.addFormResult("D");

        assertEquals(5, team.getRecentForm().size());
        assertEquals("D", team.getRecentForm().get(0));
        assertEquals("D", team.getRecentForm().get(4));
    }

    @Test
    public void testTeamFormString() {
        team.addFormResult("W");
        team.addFormResult("D");
        team.addFormResult("L");
        assertEquals("W D L", team.getFormString());
    }

    @Test
    public void testTeamTopScorer() {
        Player p1 = new Player("Player One", 24, "Forward", 80);
        Player p2 = new Player("Player Two", 26, "Forward", 82);
        p1.scoreGoal();
        p2.scoreGoal();
        p2.scoreGoal();
        team.addPlayer(p1);
        team.addPlayer(p2);

        assertEquals("Player Two", team.getTopScorer().getName());
    }

    @Test
    public void testTeamTotalGoalsScoredByPlayers() {
        Player p1 = new Player("Player One", 24, "Forward", 80);
        Player p2 = new Player("Player Two", 26, "Forward", 82);
        p1.scoreGoal();
        p2.scoreGoal();
        p2.scoreGoal();
        team.addPlayer(p1);
        team.addPlayer(p2);

        assertEquals(3, team.getTotalGoalsScoredByPlayers());
    }


    @Test
    public void testFootballDefaultLineupCreatesElevenStartersAndBench() {
        Team footballTeam = createFootballTeam("Football Team");
        footballTeam.generateDefaultLineup();

        assertEquals(11, footballTeam.getStartingLineup().size());
        assertEquals(11, footballTeam.getSubstitutes().size());
        assertEquals(22, footballTeam.getPlayers().size());
        assertEquals(1, footballTeam.getStartingLineup().stream()
                .filter(p -> p.getPosition().equals("Goalkeeper"))
                .count());
    }

    @Test
    public void testVolleyballDefaultLineupCreatesStartingSixAndBench() {
        Team volleyballTeam = createVolleyballTeam("Volleyball Team");
        volleyballTeam.generateDefaultLineup();

        assertEquals(6, volleyballTeam.getStartingLineup().size());
        assertEquals(8, volleyballTeam.getSubstitutes().size());
        assertEquals(14, volleyballTeam.getPlayers().size());
        assertTrue(volleyballTeam.getStartingLineup().stream()
                .anyMatch(p -> p.getPosition().equals("Setter")));
        assertTrue(volleyballTeam.getStartingLineup().stream()
                .anyMatch(p -> p.getPosition().equals("Libero")));
    }

    @Test
    public void testHandballDefaultLineupCreatesStartingSevenAndBench() {
        Team handballTeam = createHandballTeam("Handball Team");
        handballTeam.generateDefaultLineup();

        assertEquals(7, handballTeam.getStartingLineup().size());
        assertEquals(7, handballTeam.getSubstitutes().size());
        assertEquals(14, handballTeam.getPlayers().size());
        assertTrue(handballTeam.getStartingLineup().stream()
                .anyMatch(p -> p.getPosition().equals("Goalkeeper")));
        assertTrue(handballTeam.getStartingLineup().stream()
                .anyMatch(p -> p.getPosition().equals("Pivot")));
    }

    @Test
    public void testTeamSubstitutionSwapsStarterAndBenchPlayer() {
        Team footballTeam = createFootballTeam("Sub Team");
        footballTeam.generateDefaultLineup();

        Player starter = footballTeam.getStartingLineup().get(0);
        Player substitute = footballTeam.getSubstitutes().get(0);

        assertTrue(footballTeam.substitutePlayer(starter, substitute));
        assertTrue(footballTeam.getStartingLineup().contains(substitute));
        assertTrue(footballTeam.getSubstitutes().contains(starter));
        assertFalse(footballTeam.getStartingLineup().contains(starter));
    }

    @Test
    public void testTeamSubstitutionRejectsInjuredBenchPlayer() {
        Team footballTeam = createFootballTeam("Injury Sub Team");
        footballTeam.generateDefaultLineup();

        Player starter = footballTeam.getStartingLineup().get(0);
        Player substitute = footballTeam.getSubstitutes().get(0);
        substitute.injure(2);

        assertFalse(footballTeam.substitutePlayer(starter, substitute));
        assertTrue(footballTeam.getStartingLineup().contains(starter));
        assertTrue(footballTeam.getSubstitutes().contains(substitute));
    }
    // Match Tests

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

    @Test
    public void testMatchSimulationUpdatesWinDrawLossStats() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Match match = new Match(home, away, football);
        match.simulateMatch();

        assertEquals(1, home.getWins() + home.getDraws() + home.getLosses());
        assertEquals(1, away.getWins() + away.getDraws() + away.getLosses());
        assertEquals(match.getHomeScore(), home.getGoalsFor());
        assertEquals(match.getAwayScore(), away.getGoalsFor());
        assertEquals(match.getAwayScore(), home.getGoalsAgainst());
        assertEquals(match.getHomeScore(), away.getGoalsAgainst());
    }

    @Test
    public void testMatchSimulationCreatesMatchEvents() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        home.addPlayer(new Player("Home Player", 24, "Forward", 80));
        away.addPlayer(new Player("Away Player", 25, "Forward", 78));

        Match match = new Match(home, away, football);
        match.simulateMatch();

        assertFalse(match.getMatchEvents().isEmpty());
        assertTrue(match.getMatchEvents().get(match.getMatchEvents().size() - 1).contains("Full time"));
    }

    @Test
    public void testMatchSimulationUpdatesPlayerMatchStats() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        Player homePlayer = new Player("Home Player", 24, "Forward", 80);
        Player awayPlayer = new Player("Away Player", 25, "Forward", 78);
        home.addPlayer(homePlayer);
        away.addPlayer(awayPlayer);

        Match match = new Match(home, away, football);
        match.simulateMatch();

        assertEquals(1, homePlayer.getMatchesPlayed());
        assertEquals(1, awayPlayer.getMatchesPlayed());
        assertTrue(homePlayer.getStamina() <= 100);
        assertTrue(awayPlayer.getStamina() <= 100);
    }

    @Test
    public void testMatchScorerTrackingDoesNotExceedMatchGoals() {
        Team home = new Team("Home");
        Team away = new Team("Away");
        home.addPlayer(new Player("Home Player", 24, "Forward", 80));
        away.addPlayer(new Player("Away Player", 25, "Forward", 78));

        Match match = new Match(home, away, football);
        match.simulateMatch();

        int playerGoals = home.getTotalGoalsScoredByPlayers() + away.getTotalGoalsScoredByPlayers();
        int matchGoals = match.getHomeScore() + match.getAwayScore();
        assertTrue(playerGoals <= matchGoals);
    }


    @Test
    public void testLiveFootballPeriodRevealsEventsGradually() {
        Team home = createFootballTeam("Home Live");
        Team away = createFootballTeam("Away Live");
        home.generateDefaultLineup();
        away.generateDefaultLineup();

        Match match = new Match(home, away, football);
        match.prepareNextPeriodForLive();

        assertTrue(match.hasPendingLiveEvents());
        int beforeEvents = match.getMatchEvents().size();
        String revealed = match.revealNextLiveEvent();

        assertNotNull(revealed);
        assertEquals(beforeEvents + 1, match.getMatchEvents().size());
    }

    @Test
    public void testLiveFootballPeriodCanBeFullyRevealed() {
        Team home = createFootballTeam("Home Full Live");
        Team away = createFootballTeam("Away Full Live");
        home.generateDefaultLineup();
        away.generateDefaultLineup();

        Match match = new Match(home, away, football);
        match.prepareNextPeriodForLive();

        while (match.hasPendingLiveEvents()) {
            match.revealNextLiveEvent();
        }

        assertEquals(2, match.getCurrentPeriod());
        assertFalse(match.getMatchEvents().isEmpty());
    }

    @Test
    public void testVolleyballLiveSetCreatesRallyStyleEventsAndSetScore() {
        Team home = createVolleyballTeam("Home Volley");
        Team away = createVolleyballTeam("Away Volley");
        home.generateDefaultLineup();
        away.generateDefaultLineup();

        Match match = new Match(home, away, volleyball);
        match.prepareNextPeriodForLive();

        assertTrue(match.hasPendingLiveEvents());

        while (match.hasPendingLiveEvents()) {
            match.revealNextLiveEvent();
        }

        assertEquals(2, match.getCurrentPeriod());
        assertEquals(1, match.getSetScores().size());
        assertTrue(match.getMatchEvents().stream().anyMatch(event ->
                event.contains("Service ace")
                        || event.contains("Power spike")
                        || event.contains("Monster block")
                        || event.contains("Set point converted")));
    }

    @Test
    public void testHandballLivePeriodCreatesHandballEventsAndRealisticScore() {
        Team home = createHandballTeam("Home Handball");
        Team away = createHandballTeam("Away Handball");
        home.generateDefaultLineup();
        away.generateDefaultLineup();

        Match match = new Match(home, away, handball);
        match.prepareNextPeriodForLive();

        assertTrue(match.hasPendingLiveEvents());

        while (match.hasPendingLiveEvents()) {
            match.revealNextLiveEvent();
        }

        assertEquals(2, match.getCurrentPeriod());
        assertTrue(match.getHomeScore() >= 6);
        assertTrue(match.getAwayScore() >= 6);
        assertTrue(match.getMatchEvents().stream().anyMatch(event ->
                event.contains("Fast break")
                        || event.contains("Wing shot")
                        || event.contains("Backcourt")
                        || event.contains("Pivot finish")
                        || event.contains("7-meter")
                        || event.contains("Quick transition")));
    }

    // League Tests

    @Test
    public void testLeagueFixtureCountForThreeTeams() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.addTeam(new Team("T3"));
        league.generateFixtures();
        assertEquals(6, league.getFixtures().size()); // Double round-robin: 3C2 * 2 = 6
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
    public void testLeagueStandingsUseGoalDifferenceAsTieBreaker() {
        Team t1 = new Team("Better GD");
        Team t2 = new Team("Worse GD");

        t1.addPoints(6);
        t2.addPoints(6);
        t1.recordWin(5, 1);
        t2.recordWin(2, 1);

        league.addTeam(t1);
        league.addTeam(t2);
        league.generateFixtures();

        List<Team> standings = league.getStandings();
        assertEquals("Better GD", standings.get(0).getName());
    }

    @Test
    public void testLeagueStandingsUseGoalsForAsTieBreaker() {
        Team t1 = new Team("More Goals");
        Team t2 = new Team("Fewer Goals");

        t1.addPoints(6);
        t2.addPoints(6);
        t1.recordWin(4, 2);
        t2.recordWin(3, 1);

        league.addTeam(t1);
        league.addTeam(t2);
        league.generateFixtures();

        List<Team> standings = league.getStandings();
        assertEquals("More Goals", standings.get(0).getName());
    }

    @Test
    public void testLeagueStandingsUseNameAsFinalFallback() {
        Team t1 = new Team("Alpha FC");
        Team t2 = new Team("Beta FC");

        t1.addPoints(6);
        t2.addPoints(6);
        t1.recordWin(2, 1);
        t2.recordWin(2, 1);

        league.addTeam(t2);
        league.addTeam(t1);
        league.generateFixtures();

        List<Team> standings = league.getStandings();
        assertEquals("Alpha FC", standings.get(0).getName());
    }

    @Test
    public void testLeagueSeasonFinishedAfterAllMatches() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.generateFixtures();
        assertFalse(league.seasonFinished());
        league.playNextWeek();
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

    @Test
    public void testLeaguePlayNextWeekPlaysMultipleMatches() {
        league.addTeam(new Team("T1"));
        league.addTeam(new Team("T2"));
        league.addTeam(new Team("T3"));
        league.addTeam(new Team("T4"));
        league.generateFixtures();

        league.playNextWeek();

        assertEquals(2, league.getLastWeekMatches().size());
        assertEquals(2, league.getFixtures().stream().filter(Match::isPlayed).count());
    }

    // Training Tests

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

    @Test
    public void testNameGeneratorReturnsNames() {
        NameGenerator generator = new NameGenerator();
        assertNotNull(generator.getRandomPlayerName());
        assertNotNull(generator.getRandomCoachName());
        assertNotNull(generator.getRandomTeamName());
    }

    @Test
    public void testNameGeneratorReturnsNonEmptyNames() {
        NameGenerator generator = new NameGenerator();
        assertFalse(generator.getRandomPlayerName().isBlank());
        assertFalse(generator.getRandomCoachName().isBlank());
        assertFalse(generator.getRandomTeamName().isBlank());
    }
    // GameManager Tests

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
        assertDoesNotThrow(() -> gameManager.trainUserTeam("Fitness"));
    }

    @Test
    public void testGameManagerTrainingLimitIsFivePerWeek() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");

        for (int i = 0; i < 5; i++) {
            assertTrue(gameManager.trainUserTeam("Fitness"));
        }

        assertFalse(gameManager.trainUserTeam("Fitness"));
        assertEquals(5, gameManager.getTrainingsThisWeek());
    }

    @Test
    public void testGameManagerTrainingLimitResetsAfterWeek() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");

        for (int i = 0; i < 5; i++) {
            gameManager.trainUserTeam("Fitness");
        }

        assertFalse(gameManager.canTrainUserTeam());
        gameManager.playNextWeek();
        assertEquals(0, gameManager.getTrainingsThisWeek());
        assertTrue(gameManager.canTrainUserTeam());
    }

    @Test
    public void testGameManagerCreatesEighteenTeams() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");
        assertEquals(18, gameManager.getLeague().getTeams().size());
    }

    @Test
    public void testGameManagerCreatesSportSpecificFootballPositions() {
        gameManager.selectSport(football);
        gameManager.startNewGame("My Team");

        List<Player> players = gameManager.getUserTeam().getPlayers();
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Goalkeeper")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Defender")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Midfielder")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Forward")));
    }

    @Test
    public void testGameManagerCreatesSportSpecificVolleyballPositions() {
        gameManager.selectSport(volleyball);
        gameManager.startNewGame("My Team");

        List<Player> players = gameManager.getUserTeam().getPlayers();
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Setter")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Outside Hitter")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Middle Blocker")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Opposite Hitter")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Libero")));
    }


    @Test
    public void testGameManagerCreatesSportSpecificHandballPositions() {
        gameManager.selectSport(handball);
        gameManager.startNewGame("My Team");

        List<Player> players = gameManager.getUserTeam().getPlayers();
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Goalkeeper")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Left Wing")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Left Back")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Center Back")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Right Back")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Right Wing")));
        assertTrue(players.stream().anyMatch(p -> p.getPosition().equals("Pivot")));
    }

    @Test
    public void testGameManagerCreatesFullHandballSquadAndStartingSeven() {
        gameManager.selectSport(handball);
        gameManager.startNewGame("My Team");

        Team userTeam = gameManager.getUserTeam();
        assertEquals(14, userTeam.getPlayers().size());
        assertEquals(7, userTeam.getStartingLineup().size());
        assertEquals(7, userTeam.getSubstitutes().size());
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
    private Team createFootballTeam(String name) {
        Team footballTeam = new Team(name);
        addPlayers(footballTeam, "Goalkeeper", 2);
        addPlayers(footballTeam, "Defender", 7);
        addPlayers(footballTeam, "Midfielder", 8);
        addPlayers(footballTeam, "Forward", 5);
        return footballTeam;
    }

    private Team createVolleyballTeam(String name) {
        Team volleyballTeam = new Team(name);
        addPlayers(volleyballTeam, "Setter", 2);
        addPlayers(volleyballTeam, "Outside Hitter", 4);
        addPlayers(volleyballTeam, "Middle Blocker", 4);
        addPlayers(volleyballTeam, "Opposite Hitter", 2);
        addPlayers(volleyballTeam, "Libero", 2);
        return volleyballTeam;
    }

    private Team createHandballTeam(String name) {
        Team handballTeam = new Team(name);
        addPlayers(handballTeam, "Goalkeeper", 2);
        addPlayers(handballTeam, "Left Wing", 2);
        addPlayers(handballTeam, "Left Back", 2);
        addPlayers(handballTeam, "Center Back", 2);
        addPlayers(handballTeam, "Right Back", 2);
        addPlayers(handballTeam, "Right Wing", 2);
        addPlayers(handballTeam, "Pivot", 2);
        return handballTeam;
    }

    private void addPlayers(Team targetTeam, String position, int count) {
        for (int i = 1; i <= count; i++) {
            targetTeam.addPlayer(new Player(position + " " + i, 20 + i, position, 60 + i));
        }
    }
}
