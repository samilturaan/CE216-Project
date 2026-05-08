public interface ISport extends java.io.Serializable {

    String getSportName();

    int getPlayersOnField();

    int getSubstitutesCount();

    int getPointsForWin();

    int getPointsForDraw();

    int getPointsForLoss();

    int getMaxPeriods();

    String getPeriodName();

    boolean isMatchOver(int homeScore, int awayScore, int currentPeriod);
}
