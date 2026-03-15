import java.util.Random;

public class Training {

    private String trainingType;

    public Training(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public void applyTraining(Team team) {
        Random random = new Random();

        for (Player player : team.getPlayers()) {
            if (player.isAvailable()) {
                int improvement = random.nextInt(2) + 1;
                for (int i = 0; i < improvement; i++) {
                    player.train();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Training Type: " + trainingType;
    }
}
