package org.wadhome.redjack;

abstract class BettingStrategy {

    abstract MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            int trueCount,
            Player player,
            Randomness randomness);

    static MoneyPile constrainBet(
            MoneyPile desiredBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet) {
        if (desiredBet.isGreaterThan(maxPossibleBet)) {
            return maxPossibleBet;
        }
        if (desiredBet.isLessThan(minPossibleBet)) {
            return minPossibleBet;
        }
        return desiredBet;
    }
}
