package org.wadhome.redjack;

class PlayStrategyHighLowRealistic extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowRealistic(
            Table table,
            BettingStrategy bettingStrategy) {
        super(
                table,
                new CardCountMethodHighLowRealistic(
                        table,
                        bettingStrategy));
        this.basicStrategy = new PlayStrategyBasic(table, bettingStrategy);
    }

    @Override
    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {
        if (getTrueCount(table) > 3) {
            return maximumInsuranceBet;
        }
        return MoneyPile.zero();
    }

    @Override
    BlackjackPlay choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {

        int trueCount = getTrueCount(table);
        int runningCount = getRunningCount();
        int handPoints = hand.computeMaxSum();
        boolean isFirstPlayOnHand = hand.getNumCards() == 2;
        boolean surrenderIsPossible = isFirstPlayOnHand && tableRules.canSurrender();
        boolean hasFundsToCoverDoubleDownsAndSplits = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        boolean doubleDownIsPossibleOnNine = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits
                && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any;
        boolean doubleDownIsPossibleOnTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits;
        Randomness randomness = table.getCasino().getRandomness();

        if (handPoints == 16) {
            if (dealerUpcard.getValue().isTen()) {
                if (runningCount >= 0) {
                    if (randomness.checkRandomPercentChance(75)) {
                        player.say("Because the running count is positive, I'm going to deviate from basic, and stand, risking getting exposed as a card counter.");
                        return BlackjackPlay.Stand;
                    } else {
                        player.say("Because the running count is positive, I'd like to deviate from basic, and stand, but that looks too suspicious.");
                    }
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (trueCount >= 5) {
                    player.say("Because the true count is more than 5, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
        }

        if (handPoints == 15) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (surrenderIsPossible && trueCount >= 1) {
                    player.say("Because the true count >= 1, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && runningCount >= 0) {
                    player.say("Because the running count >= 0, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (trueCount >= 4) {
                    if (randomness.checkRandomPercentChance(75)) {
                        player.say("Because the true count >= 4, I'm going to deviate from basic, and stand, risking getting exposed as a card counter.");
                        return BlackjackPlay.Stand;
                    } else {
                        player.say("Because the true count >= 4, I'd like to deviate from basic, and stand, but that looks too suspicious.");
                    }
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && runningCount >= 2) {
                    if (randomness.checkRandomPercentChance(50)) {
                        player.say("Because the running count >= 2, I'm going to deviate from basic, and surrender, risking getting exposed as a card counter.");
                        return BlackjackPlay.Surrender;
                    } else {
                        player.say("Because the running count >= 2, I'd like to deviate from basic, and surrender, but that looks too suspicious.");
                    }
                }
            }
        }

        if (handPoints == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    if (randomness.checkRandomPercentChance(50)) {
                        player.say("Because the true count >= 3, I'm going to deviate from basic, and surrender, risking getting caught.");
                        return BlackjackPlay.Surrender;
                    } else {
                        player.say("Because the true count >= 3, I'd like to deviate from basic, and surrender, but it's too risky, I don't want to get caught.");
                    }
                }
            }
        }

        if (handPoints == 12) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (trueCount >= 4) {
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount >= 2) {
                    player.say("Because the true count >= 2, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
        }

        if (handPoints == 11) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 1) {
                    player.say("Because the true count >= 1, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        if (handPoints == 10) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        if (handPoints == 9) {
            if (dealerUpcard.getValue() == Value.Seven) {
                if (doubleDownIsPossibleOnNine && trueCount >= 4) {
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue() == Value.Two) {
                if (doubleDownIsPossibleOnNine && trueCount >= 1) {
                    player.say("Because the true count >= 1, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        return basicStrategy.choosePlay(
                player,
                hand,
                dealerUpcard,
                bankrollAvailable);
    }

    private int getTrueCount(Table table) {
        CardCountMethodHighLowRealistic cardCountMethod = (CardCountMethodHighLowRealistic) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getDiscardTray());
    }

    private int getRunningCount() {
        CardCountMethodHighLowRealistic cardCountMethod = (CardCountMethodHighLowRealistic) getCardCountMethod();
        return cardCountMethod.getRunningCount();
    }
}
