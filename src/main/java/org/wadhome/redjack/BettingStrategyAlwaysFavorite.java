package org.wadhome.redjack;

class BettingStrategyAlwaysFavorite extends BettingStrategy {
    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            int trueCount,
            Player player,
            Randomness randomness) {
        return favoriteBet;
    }
}
