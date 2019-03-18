package org.wadhome.redjack.strategy;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.cardcount.CardCountMethodHighLowPerfect;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.DoubleDownRuleOptions;

public class PlayStrategyHighLowPerfect extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    public PlayStrategyHighLowPerfect(
            Table table,
            BettingStrategy bettingStrategy) {
        super(
                table,
                new CardCountMethodHighLowPerfect(
                        table,
                        bettingStrategy));
        this.basicStrategy = new PlayStrategyBasic(table, bettingStrategy);
    }

    @Override
    public CurrencyAmount getInsuranceBet(
            CurrencyAmount maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            CurrencyAmount bankrollAvailable) {
        if (getTrueCount(table) > 3) {
            return maximumInsuranceBet;
        }
        return CurrencyAmount.zero();
    }

    @Override
    public BlackjackPlay choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard) {

        int trueCount = getTrueCount(table);
        int runningCount = getRunningCount();
        int handPoints = hand.computeMaxSum();
        boolean isFirstPlayOnHand = hand.getNumCards() == 2;
        boolean surrenderIsPossible = isFirstPlayOnHand && tableRules.canSurrender();
        CurrencyAmount bankrollAvailable = player.getBankroll().getCurrencyAmountCopy();
        boolean hasFundsToCoverDoubleDownsAndSplits = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        boolean doubleDownIsPossibleOnNine = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits
                && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any;
        boolean doubleDownIsPossibleOnTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits;

        if (handPoints == 16) {
            if (dealerUpcard.getValue().isTen()) {
                if (runningCount >= 0) {
                    player.say("Because the running count is positive, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
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
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && runningCount >= 2) {
                    player.say("Because the running count >= 2, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPoints == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    player.say("Because the true count >= 3, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPoints == 13) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (runningCount < 0) {
                    player.say("Because the running count is negative, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount < -1) {
                    player.say("Because the running count < -1, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
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
                    player.say("Because the true count >= 4, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Four) {
                if (runningCount < 0) {
                    player.say("Because the running count is negative, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Five) {
                if (trueCount < -1) {
                    player.say("Because the true count < -1, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Six) {
                if (runningCount < 0) {
                    player.say("Because the running count is negative, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
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

        boolean isPairOfTens = hand.isPair() && hand.getFirstCard().getValue().isTen();
        if (isPairOfTens) {
            if (basicStrategy.canHandBeSplit(hand, bankrollAvailable)) {
                if (dealerUpcard.getValue() == Value.Six) {
                    if (trueCount >= 5) {
                        player.say("Because the true count >= 5, I'm going to deviate from basic, and split.");
                        return BlackjackPlay.Split;
                    }
                }
                if (dealerUpcard.getValue() == Value.Five) {
                    if (trueCount >= 5) {
                        player.say("Because the true count >= 5, I'm going to deviate from basic, and split.");
                        return BlackjackPlay.Split;
                    }
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
                dealerUpcard);
    }

    private int getTrueCount(Table table) {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getShoe().numCards());
    }

    private int getRunningCount() {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getRunningCount();
    }
}
