package org.wadhome.redjack;

class BettingStrategyMaxOnGoodCount extends BettingStrategy {

    @Override
    void getBet(BetRequest betRequest) {
        if (betRequest.canPlaceBet()) {
            int trueCount = betRequest.getTrueCount();
            if (trueCount >= 3) {
                MoneyPile maxPossibleBet = betRequest.getMaxPossibleBet();
                betRequest.setConstrainedActualBetAmount(maxPossibleBet);
                betRequest.setBetComment("True count is " + trueCount + ", so I'm betting " + maxPossibleBet + ".");
            } else {
                betRequest.setConstrainedActualBetAmount(betRequest.getMinPossibleBet());
            }
        }
    }
}
