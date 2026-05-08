import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class NameGenerator implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> playerNames;
    private List<String> coachNames;
    private List<String> teamNames;

    private Random random;

    public NameGenerator() {
        random = new Random();

        playerNames = loadNames("/data/player_names.txt");
        coachNames = loadNames("/data/coach_names.txt");
        teamNames = loadNames("/data/team_names.txt");

        addFallbackNamesIfNeeded();
    }

    private List<String> loadNames(String resourcePath) {
        List<String> names = new ArrayList<>();

        try {
            InputStream inputStream = getClass().getResourceAsStream(resourcePath);

            if (inputStream == null) {
                return names;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    names.add(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }

    private void addFallbackNamesIfNeeded() {

        if (playerNames.isEmpty()) {
            playerNames.add("Emir Kaya");
            playerNames.add("Arda Demir");
            playerNames.add("Can Yildiz");
            playerNames.add("Mert Aydin");
            playerNames.add("Ege Kurt");
        }

        if (coachNames.isEmpty()) {
            coachNames.add("Murat Ozkan");
            coachNames.add("Selim Arslan");
            coachNames.add("Kaan Demir");
        }

        if (teamNames.isEmpty()) {
            teamNames.add("Izmir Falcons");
            teamNames.add("Ankara Lions");
            teamNames.add("Istanbul Storm");
            teamNames.add("Bursa Wolves");
        }
    }

    public String getRandomPlayerName() {
        return getRandomName(playerNames, "Player");
    }

    public String getRandomCoachName() {
        return getRandomName(coachNames, "Coach");
    }

    public String getRandomTeamName() {
        return getRandomName(teamNames, "Team");
    }

    private String getRandomName(List<String> names, String fallbackPrefix) {
        if (names.isEmpty()) {
            return fallbackPrefix + " " + (random.nextInt(9000) + 1000);
        }

        int index = random.nextInt(names.size());
        return names.remove(index);
    }
}
