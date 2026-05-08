import java.util.Random;
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

    public GameManager() {
        this.league = null;
        this.selectedSport = null;
        this.userTeam = null;
        this.training = new Training("General Training");
        this.nameGenerator = new NameGenerator();
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
        userTeam.addCoach(new Coach(nameGenerator.getRandomCoachName(), 45, 5));
        league.addTeam(userTeam);

        for (int i = 1; i <= 7; i++) {
            Team cpuTeam = new Team(nameGenerator.getRandomTeamName());
            generatePlayersForTeam(cpuTeam);
            cpuTeam.addCoach(new Coach(nameGenerator.getRandomCoachName(), 40 + i, 3 + i));
            league.addTeam(cpuTeam);
        }

        league.generateFixtures();
    }

    private void generatePlayersForTeam(Team team) {
        Random random = new Random();
        int playerCount = selectedSport.getPlayersOnField() + selectedSport.getSubstitutesCount();

        for (int i = 1; i <= playerCount; i++) {
            String position = generatePositionForSport(i);
            int skill = random.nextInt(41) + 60;
            Player player = new Player(nameGenerator.getRandomPlayerName(), 18 + random.nextInt(15), position, skill);
            team.addPlayer(player);
        }
    }

    private String generatePositionForSport(int playerNumber) {
        String sportName = selectedSport.getSportName();

        if (sportName.equalsIgnoreCase("Football")) {
            if (playerNumber == 1) {
                return "Goalkeeper";
            } else if (playerNumber <= 5) {
                return "Defender";
            } else if (playerNumber <= 9) {
                return "Midfielder";
            } else {
                return "Forward";
            }
        }

        if (sportName.equalsIgnoreCase("Volleyball")) {
            String[] volleyballPositions = {
                    "Setter",
                    "Outside Hitter",
                    "Middle Blocker",
                    "Opposite Hitter",
                    "Libero"
            };
            return volleyballPositions[(playerNumber - 1) % volleyballPositions.length];
        }

        return "Player";
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
