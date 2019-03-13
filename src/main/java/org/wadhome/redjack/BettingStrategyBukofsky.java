package org.wadhome.redjack;

class BettingStrategyBukofsky extends BettingStrategy {

    private boolean beSuspiciouslyPerfect;

    BettingStrategyBukofsky(boolean beSuspiciouslyPerfect) {
        this.beSuspiciouslyPerfect = beSuspiciouslyPerfect;
    }

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            int trueCount,
            Player player,
            Randomness randomness) {
        if (trueCount < 3) {
            if (beSuspiciouslyPerfect) {
                return minPossibleBet;
            }

            if (randomness.checkRandomPercentChance(20)) {
                player.say("True count is only " + trueCount + ", so I'd like to bet the minimum."
                        + " But that looks suspicious, so I'll bet a little more.");
                return constrainBet(
                        minPossibleBet.computeDouble(),
                        minPossibleBet,
                        maxPossibleBet);
            }
            return minPossibleBet;
        }

        BukofskyBankrollLevel level = player.getPlayStrategy().getCardCountMethod().getBukofskyBankrollLevelDesired();
        if (level == null) {
            level = BukofskyBankrollLevel.determine(player.getInitialBankroll());
        }
        MoneyPile desiredBet = level.getBet(trueCount, beSuspiciouslyPerfect);
        MoneyPile constrainedBet = constrainBet(
                desiredBet,
                minPossibleBet,
                maxPossibleBet);

        if (desiredBet.equals(constrainedBet)) {
            player.say("True count is " + trueCount + ", so I'm betting " + desiredBet + ".");
        } else {
            player.say("True count is " + trueCount + ", so I'd like to bet "
                    + desiredBet + ", but I must bet " + constrainedBet + " instead.");
        }
        return constrainedBet;
    }
}
