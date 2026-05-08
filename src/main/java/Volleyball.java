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
    @Override
    public int getMaxPeriods() {
        return 5;
    }
    @Override
    public String getPeriodName() {
        return "Set";
    }
    @Override
    public boolean isMatchOver(int homeScore, int awayScore, int currentPeriod) {
        return homeScore == 3 || awayScore == 3 || currentPeriod > getMaxPeriods();
    }
}
