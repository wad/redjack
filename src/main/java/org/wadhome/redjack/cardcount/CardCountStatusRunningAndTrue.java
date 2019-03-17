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

    @Override
    public String getReport() {
        return "RC=" + runningCount + " TC=" + trueCount;
    }
}
