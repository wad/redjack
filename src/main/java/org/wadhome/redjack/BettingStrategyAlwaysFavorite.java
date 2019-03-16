package org.wadhome.redjack;

class BettingStrategyAlwaysFavorite extends BettingStrategy {
    @Override
    void getBet(BetRequest betRequest) {
        if (betRequest.canPlaceBet()) {
            betRequest.setConstrainedActualBetAmount(betRequest.getDesiredBet());
        }
    }
}
