public class Player extends Person {

    private String position;
    private int skillLevel;
    private boolean injured;
    private int matchesUntilFit;

    public Player(String name, int age, String position, int skillLevel) {
        super(name, age);
        this.position = position;
        this.skillLevel = skillLevel;
        this.injured = false;
        this.matchesUntilFit = 0;
    }

    public String getPosition() {
        return position;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public boolean isInjured() {
        return injured;
    }

    public int getMatchesUntilFit() {
        return matchesUntilFit;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void train() {
        skillLevel++;
    }

    public void injure(int matches) {
        injured = true;
        matchesUntilFit = matches;
    }

    public void recover() {
        if (matchesUntilFit > 0) {
            matchesUntilFit--;
        }
        if (matchesUntilFit == 0) {
            injured = false;
        }
    }

    public boolean isAvailable() {
        return !injured;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Position: " + position +
                ", Skill: " + skillLevel +
                ", Injured: " + injured;
    }
}
