public class Player extends Person {

    private String position;
    private int skillLevel;
    private boolean injured;
    private int matchesUntilFit;
    private int stamina;
    private int morale;
    private int goalsScored;
    private int matchesPlayed;

    public Player(String name, int age, String position, int skillLevel) {
        super(name, age);
        this.position = position;
        this.skillLevel = skillLevel;
        this.injured = false;
        this.matchesUntilFit = 0;
        this.stamina = 100;
        this.morale = 70;
        this.goalsScored = 0;
        this.matchesPlayed = 0;
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

    public int getStamina() {
        return stamina;
    }

    public int getMorale() {
        return morale;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStamina(int stamina) {
        this.stamina = clamp(stamina, 0, 100);
    }

    public void setMorale(int morale) {
        this.morale = clamp(morale, 0, 100);
    }

    public void train() {
        if (stamina >= 15) {
            skillLevel++;
            stamina = clamp(stamina - 5, 0, 100);
            morale = clamp(morale + 1, 0, 100);
        }
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

    public void playMatch() {
        matchesPlayed++;
        stamina = clamp(stamina - 12, 0, 100);
        morale = clamp(morale - 1, 0, 100);
    }

    public void recoverStamina() {
        stamina = clamp(stamina + 20, 0, 100);
    }

    public void scoreGoal() {
        goalsScored++;
        morale = clamp(morale + 5, 0, 100);
    }

    public void increaseMorale(int amount) {
        morale = clamp(morale + amount, 0, 100);
    }

    public void decreaseMorale(int amount) {
        morale = clamp(morale - amount, 0, 100);
    }

    private int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Position: " + position +
                ", Skill: " + skillLevel +
                ", Stamina: " + stamina +
                ", Morale: " + morale +
                ", Goals: " + goalsScored +
                ", Injured: " + injured;
    }
}
