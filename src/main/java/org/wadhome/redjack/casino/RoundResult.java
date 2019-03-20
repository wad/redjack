package org.wadhome.redjack.casino;

class RoundResult {
    private boolean allPlayersQuit;
    private boolean cutCardDrawn;

    private RoundResult(
            boolean allPlayersQuit,
            boolean cutCardDrawn) {
        this.allPlayersQuit = allPlayersQuit;
        this.cutCardDrawn = cutCardDrawn;
    }

    static RoundResult normal() {
        return new RoundResult(false, false);
    }

    static RoundResult cutCardDrawn() {
        return new RoundResult(false, true);
    }

    static RoundResult allPlayersQuit() {
        return new RoundResult(true, false);
    }

    boolean haveAllPlayersQuit() {
        return allPlayersQuit;
    }

    boolean isCutCardDrawn() {
        return cutCardDrawn;
    }
}
