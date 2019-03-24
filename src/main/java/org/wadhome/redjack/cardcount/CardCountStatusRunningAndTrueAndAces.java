package org.wadhome.redjack.cardcount;

public class CardCountStatusRunningAndTrueAndAces extends CardCountStatus {

    private int runningCount;
    private int trueCount;
    private int aceCount;

    public CardCountStatusRunningAndTrueAndAces(
            int runningCount,
            int trueCount,
            int aceCount) {
        this.runningCount = runningCount;
        this.trueCount = trueCount;
        this.aceCount = aceCount;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public int getTrueCount() {
        return trueCount;
    }

    public int getAceCount() {
        return aceCount;
    }

    @Override
    public String getReport() {
        return "RC=" + runningCount + " TC=" + trueCount + " AC=" + aceCount;
    }
}
