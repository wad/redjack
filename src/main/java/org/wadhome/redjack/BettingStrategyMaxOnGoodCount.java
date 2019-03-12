package org.wadhome.redjack;

class BettingStrategyMaxOnGoodCount extends BettingStrategy {

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            int trueCount,
            Player player,
            Randomness randomness) {
        if (trueCount >= 3) {
            return maxPossibleBet;
        }
        return favoriteBet;
    }
}
