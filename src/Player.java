public class Player extends Person {

    private boolean injured;

    public Player(String name, int age) {
        super(name, age);
        this.injured = false;
    }

    public boolean isInjured() {
        return injured;
    }

    public void setInjured(boolean injured) {
        this.injured = injured;
    }
}