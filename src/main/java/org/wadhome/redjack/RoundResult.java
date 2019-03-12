package org.wadhome.redjack;

class RoundResult {
    private boolean allPlayersBankrupt;
    private boolean cutCardDrawn;

    private RoundResult(
            boolean allPlayersBankrupt,
            boolean cutCardDrawn) {
        this.allPlayersBankrupt = allPlayersBankrupt;
        this.cutCardDrawn = cutCardDrawn;
    }

    static RoundResult normal() {
        return new RoundResult(false, false);
    }

    static RoundResult cutCardDrawn() {
        return new RoundResult(false, true);
    }

    static RoundResult allPlayersBankrupt() {
        return new RoundResult(true, false);
    }

    boolean areAllPlayersBankrupt() {
        return allPlayersBankrupt;
    }

    boolean isCutCardDrawn() {
        return cutCardDrawn;
    }
}
