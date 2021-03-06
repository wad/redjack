package org.wadhome.redjack.strategy;

import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.cardcount.CardCountMethodUstonApcPerfect;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrueAndAces;
import org.wadhome.redjack.casino.*;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.DoubleDownRuleOptions;
import org.wadhome.redjack.rules.PlayerDecision;

public class PlayStrategyUstonApsPerfect extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    public PlayStrategyUstonApsPerfect(
            Table table,
            BettingStrategy bettingStrategy) {
        super(
                table,
                new CardCountMethodUstonApcPerfect(
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
        CardCountStatusRunningAndTrueAndAces count =
                (CardCountStatusRunningAndTrueAndAces) getCardCountMethod().getCardCountStatus();
        if (count.getTrueCountForPlay() >= 3) {
            return maximumInsuranceBet;
        }
        return CurrencyAmount.zero();
    }

    @Override
    public PlayerDecision choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard) {
        CardCountStatusRunningAndTrueAndAces count =
                (CardCountStatusRunningAndTrueAndAces) getCardCountMethod().getCardCountStatus();
        int trueCount = count.getTrueCountForPlay();
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
                        return new PlayerDecision(
                                BlackjackPlay.DoubleDown,
                                "Because the true count is one or more, I'm going to deviate from basic, and double down.");
                    }
                }

                if (dealerUpcard.getValue() == Value.Seven) {
                    if (trueCount >= 5) {
                        return new PlayerDecision(
                                BlackjackPlay.DoubleDown,
                                "Because the true count is five or more, I'm going to deviate from basic, and double down.");
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
                        return new PlayerDecision(
                                BlackjackPlay.Stand,
                                "Because the running count is positive, I'm going to deviate from basic, and stand.");
                    }
                }
                if (dealerUpcard.getValue() == Value.Nine) {
                    if (trueCount >= 6) {
                        return new PlayerDecision(
                                BlackjackPlay.Stand,
                                "Because the true count is six or more, I'm going to deviate from basic, and stand.");
                    }
                }
            }
        }

        if (handPointsMax == 15) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (surrenderIsPossible && trueCount >= 2) {
                    return new PlayerDecision(
                            BlackjackPlay.Surrender,
                            "Because the true count is two or more, I'm going to deviate from basic, and surrender.");
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && runningCount >= 0) {
                    return new PlayerDecision(
                            BlackjackPlay.Surrender,
                            "Because the running count is zero or more, I'm going to deviate from basic, and surrender.");
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (trueCount >= 4) {
                    return new PlayerDecision(
                            BlackjackPlay.Stand,
                            "Because the true count is four or more, I'm going to deviate from basic, and stand.");
                }
            }
            if (dealerUpcard.getValue() == Value.Nine) {
                if (surrenderIsPossible && trueCount >= 3) {
                    return new PlayerDecision(
                            BlackjackPlay.Surrender,
                            "Because the true count is three or more, I'm going to deviate from basic, and surrender.");
                }
            }
        }

        if (handPointsMax == 14) {
            if (dealerUpcard.getValue().isTen()) {
                if (surrenderIsPossible && trueCount >= 3) {
                    return new PlayerDecision(
                            BlackjackPlay.Surrender,
                            "Because the true count is three or more, I'm going to deviate from basic, and surrender.");
                }
            }
        }

        if (handPointsMax == 13) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (runningCount < 0) {
                    return new PlayerDecision(
                            BlackjackPlay.Hit,
                            "Because the running count is negative, I'm going to deviate from basic, and hit.");
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount < -1) {
                    return new PlayerDecision(
                            BlackjackPlay.Hit,
                            "Because the running count less than negative one, I'm going to deviate from basic, and hit.");
                }
            }
        }

        if (handPointsMax == 12) {
            if (dealerUpcard.getValue() == Value.Two) {
                if (trueCount >= 4) {
                    return new PlayerDecision(
                            BlackjackPlay.Stand,
                            "Because the true count four or more, I'm going to deviate from basic, and stand.");
                }
            }
            if (dealerUpcard.getValue() == Value.Three) {
                if (trueCount >= 2) {
                    return new PlayerDecision(
                            BlackjackPlay.Stand,
                            "Because the true count is two or more, I'm going to deviate from basic, and stand.");
                }
            }
            if (dealerUpcard.getValue() == Value.Four) {
                if (runningCount < 0) {
                    return new PlayerDecision(
                            BlackjackPlay.Hit,
                            "Because the running count is negative, I'm going to deviate from basic, and hit.");
                }
            }
            if (dealerUpcard.getValue() == Value.Five) {
                if (trueCount < -1) {
                    return new PlayerDecision(
                            BlackjackPlay.Hit,
                            "Because the true count is less than negative one, I'm going to deviate from basic, and hit.");
                }
            }
            if (dealerUpcard.getValue() == Value.Six) {
                if (runningCount < 0) {
                    return new PlayerDecision(
                            BlackjackPlay.Hit,
                            "Because the running count is negative, I'm going to deviate from basic, and hit.");
                }
            }
        }

        if (handPointsMax == 11) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 1) {
                    return new PlayerDecision(
                            BlackjackPlay.DoubleDown,
                            "Because the true count is one or more, I'm going to deviate from basic, and double down.");
                }
            }
        }

        boolean isPairOfTens = hand.isPair() && hand.getFirstCard().getValue().isTen();
        if (isPairOfTens) {
            if (basicStrategy.canHandBeSplit(hand, bankrollAvailable)) {
                if (dealerUpcard.getValue() == Value.Six) {
                    if (trueCount >= 6) {
                        return new PlayerDecision(
                                BlackjackPlay.Split,
                                "Because the true count is six or more, I'm going to deviate from basic, and split.");
                    }
                }
                if (dealerUpcard.getValue() == Value.Five) {
                    if (trueCount >= 6) {
                        return new PlayerDecision(
                                BlackjackPlay.Split,
                                "Because the true count is six or more, I'm going to deviate from basic, and split.");
                    }
                }
            }
        }

        if (handPointsMax == 10) {
            if (dealerUpcard.getValue() == Value.Ace) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 5) {
                    return new PlayerDecision(
                            BlackjackPlay.DoubleDown,
                            "Because the true count is five or more, I'm going to deviate from basic, and double down.");
                }
            }
            if (dealerUpcard.getValue().isTen()) {
                if (doubleDownIsPossibleOnTenOrEleven && trueCount >= 5) {
                    return new PlayerDecision(
                            BlackjackPlay.DoubleDown,
                            "Because the true count is five or more, I'm going to deviate from basic, and double down.");
                }
            }
        }

        if (handPointsMax == 9) {
            if (dealerUpcard.getValue() == Value.Seven) {
                if (doubleDownIsPossibleStuffOtherThanTenOrEleven && trueCount >= 5) {
                    return new PlayerDecision(
                            BlackjackPlay.DoubleDown,
                            "Because the true count is five or more, I'm going to deviate from basic, and double down.");
                }
            }
            if (dealerUpcard.getValue() == Value.Two) {
                if (doubleDownIsPossibleStuffOtherThanTenOrEleven && trueCount >= 1) {
                    return new PlayerDecision(
                            BlackjackPlay.DoubleDown,
                            "Because the true count is one or more, I'm going to deviate from basic, and double down.");
                }
            }
        }

        return basicStrategy.choosePlay(
                player,
                hand,
                dealerUpcard);
    }
}
