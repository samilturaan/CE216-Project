public class Volleyball implements ISport {

    @Override
    public String getSportName() {
        return "Volleyball";
    }

    @Override
    public int getPlayersOnField() {
        return 6;
    }

    @Override
    public int getSubstitutesCount() {
        return 6;
    }

    @Override
    public int getPointsForWin() {
        return 3;
    }

    @Override
    public int getPointsForDraw() {
        return 0;
    }

    @Override
    public int getPointsForLoss() {
        return 0;
    }
}
