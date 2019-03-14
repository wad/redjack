package org.wadhome.redjack;

class CardCountStatusRunningAndTrue extends CardCountStatus {

    private int runningCount;
    private int trueCount;

    CardCountStatusRunningAndTrue(
            int runningCount,
            int trueCount) {
        this.runningCount = runningCount;
        this.trueCount = trueCount;
    }

    @Override
    String getReport() {
        return "RC=" + runningCount + " TC=" + trueCount;
    }
}
