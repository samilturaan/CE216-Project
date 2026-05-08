public class Football implements ISport {

    @Override
    public String getSportName() {
        return "Football";
    }

    @Override
    public int getPlayersOnField() {
        return 11;
    }

    @Override
    public int getSubstitutesCount() {
        return 5;
    }

    @Override
    public int getPointsForWin() {
        return 3;
    }

    @Override
    public int getPointsForDraw() {
        return 1;
    }

    @Override
    public int getPointsForLoss() {
        return 0;
    }
}
