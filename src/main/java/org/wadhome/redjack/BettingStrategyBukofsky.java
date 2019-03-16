package org.wadhome.redjack;

class BettingStrategyBukofsky extends BettingStrategy {

    private boolean beSuspiciouslyPerfect;

    BettingStrategyBukofsky(boolean beSuspiciouslyPerfect) {
        this.beSuspiciouslyPerfect = beSuspiciouslyPerfect;
    }

    @Override
    void getBet(BetRequest betRequest) {
        if (betRequest.canPlaceBet()) {
            int trueCount = betRequest.getTrueCount();

            if (trueCount < 3) {
                if (beSuspiciouslyPerfect) {
                    betRequest.setConstrainedActualBetAmount(betRequest.getMinPossibleBet());
                } else {
                    if (betRequest.getRandomness().checkRandomPercentChance(20)) {
                        betRequest.setBetComment("True count is only " + trueCount + ", so I'd like to bet the minimum."
                                + " But that looks suspicious, so I'll bet a little more.");
                        betRequest.setConstrainedActualBetAmount(betRequest.getMinPossibleBet().computeDouble());
                    } else {
                        betRequest.setConstrainedActualBetAmount(betRequest.getMinPossibleBet());
                    }
                }
            } else {
                Player player = betRequest.getPlayer();
                BukofskyBankrollLevel level = player
                        .getPlayStrategy()
                        .getCardCountMethod()
                        .getBukofskyBankrollLevelDesired();
                if (level == null) {
                    level = BukofskyBankrollLevel.determine(player.getInitialBankroll());
                }

                MoneyPile desiredBet = level.getBet(trueCount, beSuspiciouslyPerfect);
                MoneyPile actualBet = betRequest.setConstrainedActualBetAmount(desiredBet);
                if (actualBet.equals(desiredBet)) {
                    betRequest.setBetComment("True count is " + trueCount + ", so I'm betting " + desiredBet + ".");
                } else {
                    betRequest.setBetComment("True count is " + trueCount + ", so I'd like to bet "
                            + desiredBet + ", but I must bet " + actualBet + " instead.");
                }
            }
        }
    }
}
