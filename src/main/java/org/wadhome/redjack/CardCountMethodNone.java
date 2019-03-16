package org.wadhome.redjack;

class CardCountMethodNone extends CardCountMethod {

    CardCountMethodNone(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
    }

    @Override
    CardCountStatus getCardCountStatus() {
        return new CardCountStatusNone();
    }

    @Override
    void observeCard(Card card) {
        // do nothing
    }

    @Override
    void observeShuffle() {
        // do nothing
    }

    @Override
    void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(0); // there really is none, but whatever.
        bettingStrategy.getBet(betRequest);
    }
}
