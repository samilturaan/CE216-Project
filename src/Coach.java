public class Coach extends Person {

    private int experienceLevel;

    public Coach(String name, int age, int experienceLevel) {
        super(name, age);
        this.experienceLevel = experienceLevel;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void improveExperience() {
        experienceLevel++;
    }

    @Override
    public String toString() {
        return super.toString() + ", Experience Level: " + experienceLevel;
    }
}
