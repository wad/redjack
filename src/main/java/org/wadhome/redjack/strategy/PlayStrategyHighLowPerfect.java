package org.wadhome.redjack.strategy;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.cardcount.CardCountMethodHighLowPerfect;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrue;
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
        CardCountStatusRunningAndTrue count = (CardCountStatusRunningAndTrue) getCardCountMethod().getCardCountStatus();
        if (count.getTrueCount() > 3) {
            return maximumInsuranceBet;
        }
        return CurrencyAmount.zero();
    }

    @Override
    public BlackjackPlay choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard) {
        CardCountStatusRunningAndTrue count = (CardCountStatusRunningAndTrue) getCardCountMethod().getCardCountStatus();
        int trueCount = count.getTrueCount();
        int runningCount = count.getRunningCount();
        int handPointsMax = hand.computeMaxSum();
        boolean isSoft = handPointsMax != hand.computeMinSum();
        boolean isFirstPlayOnHand = hand.getNumCards() == 2;
        boolean surrenderIsPossible = isFirstPlayOnHand && tableRules.getCanSurrender();
        CurrencyAmount bankrollAvailable = player.getBankroll().getCurrencyAmountCopy();
        boolean hasFundsToCoverDoubleDownsAndSplits = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        boolean doubleDownIsPossibleStuffOtherThanTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits
                && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any;
        boolean doubleDownIsPossibleOnTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits;

        if (handPointsMax == 19) {
            boolean isAceWithEight = isSoft && isFirstPlayOnHand;
            if (isAceWithEight && doubleDownIsPossibleStuffOtherThanTenOrEleven) {
                if (dealerUpcard.getValue() == Value.Two) {
                    if (trueCount >= 1) {
                        player.say("Because the true count is one or more, I'm going to deviate from basic, and double down.");
                        return BlackjackPlay.DoubleDown;
                    }
                }
                if (dealerUpcard.getValue() == Value.Seven) {
                    if (trueCount >= 4) {
                        player.say("Because the true count is four or more, I'm going to deviate from basic, and double down.");
                        return BlackjackPlay.DoubleDown;
                    }
                }
            }
        }

        if (handPointsMax == 16) {
            boolean isPairOfEights = hand.isPair();
            // We don't want to deviate from Basic Strategy if it's an Ace with five other points.
            if (!isSoft && !isPairOfEights) {
                if (dealerUpcard.getValue().isTen()) {
                    if (runningCount >= 0) {
                        player.say("Because the running count is positive, I'm going to deviate from basic, and stand.");
                        return BlackjackPlay.Stand;
                    }
                }
                if (dealerUpcard.getValue() == Value.Nine) {
                    if (trueCount >= 5) {
                        player.say("Because the true count is five or more, I'm going to deviate from basic, and stand.");
                        return BlackjackPlay.Stand;
                    }
                }
            }
        }

        if (handPointsMax == 15) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (surrenderIsPossible && trueCount >= 1) {
                    player.say("Because the true count is one or more, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && runningCount >= 0) {
                    player.say("Because the running count is zero or more, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (trueCount >= 4) {
                    player.say("Because the true count is four or more, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && trueCount >= 2) {
                    player.say("Because the true count is two or more, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPointsMax == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    player.say("Because the true count is three or more, I'm going to deviate from basic, and surrender.");
                    return BlackjackPlay.Surrender;
                }
            }
        }

        if (handPointsMax == 13) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (runningCount < 0) {
                    player.say("Because the running count is negative, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount < -1) {
                    player.say("Because the running count less than negative one, I'm going to deviate from basic, and hit.");
                    return BlackjackPlay.Hit;
                }
            }
        }

        if (handPointsMax == 12) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (trueCount >= 4) {
                    player.say("Because the true count four or more, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount >= 2) {
                    player.say("Because the true count is two or more, I'm going to deviate from basic, and stand.");
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
                    player.say("Because the true count is less than negative one, I'm going to deviate from basic, and hit.");
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

        if (handPointsMax == 11) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 1) {
                    player.say("Because the true count is one or more, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        boolean isPairOfTens = hand.isPair() && hand.getFirstCard().getValue().isTen();
        if (isPairOfTens) {
            if (basicStrategy.canHandBeSplit(hand, bankrollAvailable)) {
                if (dealerUpcard.getValue() == Value.Six) {
                    if (trueCount >= 5) {
                        player.say("Because the true count is five or more, I'm going to deviate from basic, and split.");
                        return BlackjackPlay.Split;
                    }
                }
                if (dealerUpcard.getValue() == Value.Five) {
                    if (trueCount >= 5) {
                        player.say("Because the true count is five or more, I'm going to deviate from basic, and split.");
                        return BlackjackPlay.Split;
                    }
                }
            }
        }

        if (handPointsMax == 10) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count is for our more, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count is four or more, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        if (handPointsMax == 9) {
            if (dealerUpcard.getValue() == Value.Seven) {
                if (doubleDownIsPossibleStuffOtherThanTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count is four or more, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
            if (dealerUpcard.getValue() == Value.Two) {
                if (doubleDownIsPossibleStuffOtherThanTenOrEleven && trueCount >= 1) {
                    player.say("Because the true count is one or more, I'm going to deviate from basic, and double down.");
                    return BlackjackPlay.DoubleDown;
                }
            }
        }

        return basicStrategy.choosePlay(
                player,
                hand,
                dealerUpcard);
    }
}
