package org.wadhome.redjack.strategy;

import org.wadhome.redjack.Randomness;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.cardcount.CardCountMethodHighLowRealistic;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrue;
import org.wadhome.redjack.casino.*;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.DoubleDownRuleOptions;

public class PlayStrategyHighLowRealistic extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    public PlayStrategyHighLowRealistic(
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
        boolean hasFundsToCoverDoubleDownsAndSplits = player.getBankroll()
                .getCurrencyAmountCopy().isGreaterThanOrEqualTo(hand.getBetAmount());
        boolean doubleDownIsPossibleStuffOtherThanTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits
                && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any;
        boolean doubleDownIsPossibleOnTenOrEleven = isFirstPlayOnHand
                && hasFundsToCoverDoubleDownsAndSplits;
        Randomness randomness = table.getCasino().getRandomness();

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
                    if (randomness.checkRandomPercentChance(75)) {
                        player.say("Because the true count is four or more, I'm going to deviate from basic, and stand, risking getting exposed as a card counter.");
                        return BlackjackPlay.Stand;
                    } else {
                        player.say("Because the true count is four or more, I'd like to deviate from basic, and stand, but that looks too suspicious.");
                    }
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && trueCount >= 2) {
                    if (randomness.checkRandomPercentChance(50)) {
                        player.say("Because the true count is two or more, I'm going to deviate from basic, and surrender, risking getting exposed as a card counter.");
                        return BlackjackPlay.Surrender;
                    } else {
                        player.say("Because the true count is two or more, I'd like to deviate from basic, and surrender, but that looks too suspicious.");
                    }
                }
            }
        }

        if (handPointsMax == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    if (randomness.checkRandomPercentChance(50)) {
                        player.say("Because the true count is three or more, I'm going to deviate from basic, and surrender, risking getting caught.");
                        return BlackjackPlay.Surrender;
                    } else {
                        player.say("Because the true count is three or more, I'd like to deviate from basic, and surrender, but it's too risky, I don't want to get caught.");
                    }
                }
            }
        }

        if (handPointsMax == 12) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (trueCount >= 4) {
                    player.say("Because the true count is four or more, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount >= 2) {
                    player.say("Because the true count is two or more, I'm going to deviate from basic, and stand.");
                    return BlackjackPlay.Stand;
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

        if (handPointsMax == 10) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 4) {
                    player.say("Because the true count is four or more, I'm going to deviate from basic, and double down.");
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
