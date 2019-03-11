package org.wadhome.redjack;

public abstract class BettingStrategy {

    abstract MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            int trueCount,
            Player player);
}
