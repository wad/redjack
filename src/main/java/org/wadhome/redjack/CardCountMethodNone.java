package org.wadhome.redjack;

class CardCountMethodNone extends CardCountMethod {

    CardCountMethodNone(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
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
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Player player) {
        return bettingStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet,
                0, // there is no count
                player);
    }
}
