import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class GameManager implements java.io.Serializable{

    private League league;
    private ISport selectedSport;
    private Team userTeam;
    private Training training;
    private NameGenerator nameGenerator;
    private List<String> newsItems;
    private int trainingsThisWeek;
    private static final int MAX_TRAININGS_PER_WEEK = 5;

    public GameManager() {
        this.league = null;
        this.selectedSport = null;
        this.userTeam = null;
        this.training = new Training("General Training");
        this.nameGenerator = new NameGenerator();
        this.newsItems = new ArrayList<>();
        this.trainingsThisWeek = 0;
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
        if (selectedSport == null) return;

        userTeam = new Team(userTeamName);

        league = new League(selectedSport);
        league.addTeam(userTeam);

        int onField = selectedSport.getPlayersOnField();
        int subsCount = selectedSport.getSubstitutesCount();

        for (int i = 0; i < onField; i++) {
            String pos = getInitialPositionForSport(i, selectedSport.getSportName());
            Player p = new Player(nameGenerator.getRandomPlayerName(), 20 + new Random().nextInt(12), pos, 70 + new Random().nextInt(10));
            userTeam.getPlayers().add(p);
            userTeam.getStartingLineup().add(p);
        }

        for (int i = 0; i < subsCount; i++) {
            String pos = getInitialPositionForSport(new Random().nextInt(onField), selectedSport.getSportName());
            Player p = new Player(nameGenerator.getRandomPlayerName(), 18 + new Random().nextInt(15), pos, 60 + new Random().nextInt(10));
            userTeam.getPlayers().add(p);
            userTeam.getSubstitutes().add(p);
        }
        userTeam.getCoaches().add(new Coach(nameGenerator.getRandomCoachName(), 45, 70));

        for (int k = 0; k < 17; k++) {
            Team aiTeam = new Team(nameGenerator.getRandomTeamName() + " FC");

            for (int i = 0; i < onField; i++) {
                String pos = getInitialPositionForSport(i, selectedSport.getSportName());
                Player p = new Player(nameGenerator.getRandomPlayerName(), 20 + new Random().nextInt(12), pos, 50 + new Random().nextInt(25));
                aiTeam.getPlayers().add(p);
                aiTeam.getStartingLineup().add(p);
            }
            for (int i = 0; i < subsCount; i++) {
                String pos = getInitialPositionForSport(new Random().nextInt(onField), selectedSport.getSportName());
                Player p = new Player(nameGenerator.getRandomPlayerName(), 18 + new Random().nextInt(15), pos, 45 + new Random().nextInt(20));
                aiTeam.getPlayers().add(p);
                aiTeam.getSubstitutes().add(p);
            }
            aiTeam.getCoaches().add(new Coach(nameGenerator.getRandomCoachName(), 50, 65));
            league.addTeam(aiTeam);
        }

        league.generateFixtures();
        trainingsThisWeek = 0;
        newsItems.clear();
        newsItems.add("Breaking: " + userTeamName + " joins the " + selectedSport.getSportName() + " league!");
    }


    private String getInitialPositionForSport(int index, String sportName) {
        if ("Handball".equals(sportName)) {
            return switch (index) {
                case 0 -> "Goalkeeper";
                case 1 -> "Left Wing";
                case 2 -> "Left Back";
                case 3 -> "Center Back";
                case 4 -> "Right Back";
                case 5 -> "Right Wing";
                case 6 -> "Pivot";
                default -> "Player";
            };
        } else if ("Volleyball".equals(sportName)) {
            return switch (index) {
                case 0 -> "Setter";
                case 1 -> "Outside Hitter";
                case 2 -> "Middle Blocker";
                case 3 -> "Opposite Hitter";
                case 4 -> "Libero";
                case 5 -> "Middle Blocker";
                default -> "Player";
            };
        }

        return switch (index) {
            case 0 -> "Goalkeeper";
            case 1, 2, 3, 4 -> "Defender";
            case 5, 6, 7 -> "Midfielder";
            default -> "Forward";
        };
    }

    private void generatePlayersForTeam(Team team) {
        Random random = new Random();
        List<String> positions = generatePositionPoolForSelectedSport();

        for (String position : positions) {
            int skill = random.nextInt(41) + 60;
            Player player = new Player(nameGenerator.getRandomPlayerName(), 18 + random.nextInt(15), position, skill);
            team.addPlayer(player);
        }
    }

    private List<String> generatePositionPoolForSelectedSport() {
        List<String> positions = new ArrayList<>();
        String sportName = selectedSport.getSportName();

        if (sportName.equalsIgnoreCase("Football")) {
            addPositions(positions, "Goalkeeper", 2);
            addPositions(positions, "Defender", 7);
            addPositions(positions, "Midfielder", 8);
            addPositions(positions, "Forward", 5);
            return positions;
        }

        if (sportName.equalsIgnoreCase("Volleyball")) {
            addPositions(positions, "Setter", 2);
            addPositions(positions, "Outside Hitter", 4);
            addPositions(positions, "Middle Blocker", 4);
            addPositions(positions, "Opposite Hitter", 2);
            addPositions(positions, "Libero", 2);
            return positions;
        }

        int defaultCount = selectedSport.getPlayersOnField() + Math.max(selectedSport.getSubstitutesCount(), selectedSport.getPlayersOnField());
        for (int i = 0; i < defaultCount; i++) {
            positions.add("Player");
        }
        return positions;
    }

    private void addPositions(List<String> positions, String position, int count) {
        for (int i = 0; i < count; i++) {
            positions.add(position);
        }
    }

    public void refreshLineups() {
        if (league == null) {
            return;
        }

        for (Team team : league.getTeams()) {
            if (team.getStartingLineup().isEmpty()) {
                team.generateDefaultLineup();
            }
        }
    }


    public boolean trainUserTeam(String type) {
        if (userTeam == null || isSeasonFinished() || trainingsThisWeek >= MAX_TRAININGS_PER_WEEK) {
            return false;
        }
        training.setTrainingType(type);
        training.applyTraining(userTeam);
        trainingsThisWeek++;
        addNews(userTeam.getName() + " completed a " + type + " training session. Weekly sessions: " + trainingsThisWeek + "/" + MAX_TRAININGS_PER_WEEK + ".");
        return true;
    }

    public boolean canTrainUserTeam() {
        return userTeam != null && !isSeasonFinished() && trainingsThisWeek < MAX_TRAININGS_PER_WEEK;
    }

    public int getTrainingsThisWeek() {
        return trainingsThisWeek;
    }

    public int getMaxTrainingsPerWeek() {
        return MAX_TRAININGS_PER_WEEK;
    }

    public void playNextWeek() {
        if (league != null && !league.seasonFinished()) {
            league.playNextWeek(userTeam);
            generateWeeklyNews();
            recoverAllPlayers();
            trainingsThisWeek = 0;
        }
    }

    private void generateWeeklyNews() {
        if (league == null) {
            return;
        }

        for (Match match : league.getLastWeekMatches()) {
            addNews(buildMatchNews(match));

            for (String event : match.getMatchEvents()) {
                if (event.contains("Injury")) {
                    addNews(event);
                }
            }
        }

        if (userTeam != null) {
            int rank = getUserTeamRank();
            addNews(userTeam.getName() + " is currently ranked " + rank + " in the league with " + userTeam.getPoints() + " points.");
        }
    }

    private String buildMatchNews(Match match) {
        Team home = match.getHomeTeam();
        Team away = match.getAwayTeam();
        String score = home.getName() + " " + match.getHomeScore() + " - " + match.getAwayScore() + " " + away.getName();

        if (match.getHomeScore() > match.getAwayScore()) {
            return home.getName() + " defeated " + away.getName() + ". Final score: " + score + ".";
        }

        if (match.getAwayScore() > match.getHomeScore()) {
            return away.getName() + " defeated " + home.getName() + ". Final score: " + score + ".";
        }

        return home.getName() + " and " + away.getName() + " shared the points. Final score: " + score + ".";
    }

    public void addNews(String news) {
        if (news == null || news.isBlank()) {
            return;
        }

        newsItems.add(0, news);

        if (newsItems.size() > 8) {
            newsItems.remove(newsItems.size() - 1);
        }
    }

    public List<String> getNewsItems() {
        return newsItems;
    }

    public int getUserTeamRank() {
        if (league == null || userTeam == null) {
            return 0;
        }

        List<Team> standings = league.getStandings();
        for (int i = 0; i < standings.size(); i++) {
            if (standings.get(i).getName().equals(userTeam.getName())) {
                return i + 1;
            }
        }

        return 0;
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

    public void saveGame(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Oyun başarıyla kaydedildi: " + fileName);
        } catch (IOException e) {
            System.err.println("Kaydetme hatası: " + e.getMessage());
        }
    }

    public static GameManager loadGame(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GameManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Yükleme hatası: " + e.getMessage());
            return null;
        }
    }
}
