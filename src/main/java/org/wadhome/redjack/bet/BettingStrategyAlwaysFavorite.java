package org.wadhome.redjack.bet;

public class BettingStrategyAlwaysFavorite extends BettingStrategy {

    @Override
    public void getBet(BetRequest betRequest) {
        if (betRequest.canPlaceBet()) {
            betRequest.setConstrainedActualBetAmount(betRequest.getDesiredBet());
        }
    }
}
