package org.wadhome.redjack.cardcount;

public class CardCountStatusRunningAndTrueAndAces extends CardCountStatus {

    private int runningCount;
    private int aceCount;
    private int trueCountForPlay;
    private int trueCountForBet;

    public CardCountStatusRunningAndTrueAndAces(
            int runningCount,
            int aceCount,
            int trueCountForPlay,
            int trueCountForBet) {
        this.runningCount = runningCount;
        this.trueCountForPlay = trueCountForPlay;
        this.trueCountForBet = trueCountForBet;
        this.aceCount = aceCount;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public int getAceCount() {
        return aceCount;
    }

    public int getTrueCountForPlay() {
        return trueCountForPlay;
    }

    public int getTrueCountForBet() {
        return trueCountForBet;
    }

    @Override
    public String getReport() {
        return "RC=" + runningCount
                + " AC=" + aceCount
                + " TCP=" + trueCountForPlay
                + " TCB=" + trueCountForBet;
    }
}
