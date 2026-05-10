public class Handball implements ISport {

    @Override
    public String getSportName() {
        return "Handball";
    }

    @Override
    public int getPlayersOnField() {
        return 7;
    }

    @Override
    public int getSubstitutesCount() {
        return 7;
    }

    @Override
    public int getPointsForWin() {
        return 2;
    }

    @Override
    public int getPointsForDraw() {
        return 1;
    }

    @Override
    public int getPointsForLoss() {
        return 0;
    }

    @Override
    public int getMaxPeriods() {
        return 2;
    }

    @Override
    public String getPeriodName() {
        return "Half";
    }

    @Override
    public boolean isMatchOver(int homeScore, int awayScore, int currentPeriod) {
        return currentPeriod > getMaxPeriods();
    }
}
