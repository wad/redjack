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
                && tableRules.getDoubleDownOptions() == TableRules.DoubleDownOptions.Any;
        boolean doubleDownIsPossibleOnTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits;
        Randomness randomness = table.getCasino().getRandomness();

        if (handPoints == 16) {
            if (dealerUpcard.getValue().isTen()) {
                if (runningCount >= 0) {
                    if (randomness.checkRandomPercentChance(75)) {
                        return BlackjackPlay.Stand;
                    }
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (trueCount >= 5) {
                    return BlackjackPlay.Stand;
                }
            }
        }

        if (handPoints == 15) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (surrenderIsPossible && trueCount >= 1) {
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && runningCount >= 0) {
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (trueCount >= 4) {
                    if (randomness.checkRandomPercentChance(75)) {
                        return BlackjackPlay.Stand;
                    }
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && runningCount >= 2) {
                    if (randomness.checkRandomPercentChance(50)) {
                        return BlackjackPlay.Surrender;
                    }
                }
            }
        }

        if (handPoints == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    if (randomness.checkRandomPercentChance(50)) {
                        return BlackjackPlay.Surrender;
                    }
                }
            }
        }

        if (handPoints == 12) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (trueCount > 4) {
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount > 2) {
                    return BlackjackPlay.Stand;
                }
            }
        }

        if (handPoints == 11) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 1) {
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        if (handPoints == 10) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        if (handPoints == 9) {
            if (dealerUpcard.getValue() == Value.Seven) {
                if (doubleDownIsPossibleOnNine && trueCount >= 4) {
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue() == Value.Two) {
                if (doubleDownIsPossibleOnNine && trueCount >= 1) {
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        return basicStrategy.choosePlay(
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
