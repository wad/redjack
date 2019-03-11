package org.wadhome.redjack;

class PlayStrategyHighLowPerfect extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowPerfect(Table table) {
        super(table, new CardCountMethodHighLowPerfect(table.getTableRules()));
        this.basicStrategy = new PlayStrategyBasic(table);
    }

    int getTrueCount(Table table) {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getShoe().cards.size());
    }

    int getRunningCount() {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getRunningCount();
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

        if (handPoints == 16) {
            if (dealerUpcard.getValue().isTen()) {
                if (runningCount >= 0) {
                    return BlackjackPlay.Stand;
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
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && runningCount >= 2) {
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPoints == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPoints == 13) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (runningCount < 0) {
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount < -1) {
                    return BlackjackPlay.Hit;
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
            if (dealerUpcard.getValue() == Value.Four) {
                if (runningCount < 0) {
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Five) {
                if (trueCount < -1) {
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Six) {
                if (runningCount < 0) {
                    return BlackjackPlay.Hit;
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

        boolean isPairOfTens = hand.isPair() && hand.getFirstCard().getValue().isTen();
        if (isPairOfTens) {
            if (basicStrategy.canHandBeSplit(hand, bankrollAvailable)) {
                if (dealerUpcard.getValue() == Value.Six) {
                    if (trueCount >= 5) {
                        return BlackjackPlay.Split;
                    }
                }
                if (dealerUpcard.getValue() == Value.Five) {
                    if (trueCount >= 5) {
                        return BlackjackPlay.Split;
                    }
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
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet) {
        // todo
    }
}
