package org.wadhome.redjack.cardcount;

public class CardCountStatusRunningAndTrue extends CardCountStatus {

    private int runningCount;
    private int trueCount;

    public CardCountStatusRunningAndTrue(
            int runningCount,
            int trueCount) {
        this.runningCount = runningCount;
        this.trueCount = trueCount;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public int getTrueCount() {
        return trueCount;
    }

    @Override
    public String getReport() {
        return "RC=" + runningCount + " TC=" + trueCount;
    }
}
