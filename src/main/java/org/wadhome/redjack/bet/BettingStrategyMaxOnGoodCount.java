package org.wadhome.redjack.bet;

import org.wadhome.redjack.money.CurrencyAmount;

public class BettingStrategyMaxOnGoodCount extends BettingStrategy {

    @Override
    public void getBet(BetRequest betRequest) {
        if (betRequest.canPlaceBet()) {
            Integer trueCount = betRequest.getTrueCount();
            if (trueCount >= 3) {
                CurrencyAmount maxPossibleBet = betRequest.getMaxPossibleBet();
                betRequest.setConstrainedActualBetAmount(maxPossibleBet);
                betRequest.setBetComment("True count is " + trueCount + ", so I'm betting " + maxPossibleBet + ".");
            } else {
                betRequest.setConstrainedActualBetAmount(betRequest.getMinPossibleBet());
            }
        }
    }
}
