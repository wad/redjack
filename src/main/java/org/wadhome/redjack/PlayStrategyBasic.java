package org.wadhome.redjack;

import static org.wadhome.redjack.BlackjackPlay.*;
import static org.wadhome.redjack.Value.*;

class PlayStrategyBasic extends PlayStrategy {

    PlayStrategyBasic(
            Table table,
            BettingStrategy bettingStrategy) {
        super(
                table,
                new CardCountMethodNone(
                        table,
                        bettingStrategy));
    }

    @Override
    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {

        // When you don't know the true count, don't get insurance.
        return MoneyPile.zero();
    }

    BlackjackPlay choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {

        validateHand(hand);

        if (hand.isPair()) {
            return handlePair(
                    hand,
                    dealerUpcard,
                    bankrollAvailable);
        }

        if (hand.hasAtLeastOneAce()) {
            return handleSoftHand(
                    hand,
                    dealerUpcard,
                    false,
                    bankrollAvailable);
        }

        return handleHardHand(
                hand,
                dealerUpcard,
                false,
                bankrollAvailable);
    }

    private BlackjackPlay handleHardHand(
            PlayerHand hand,
            Card dealerUpcard,
            boolean isAfterSplit,
            MoneyPile bankrollAvailable) {
        Value upcardValue = dealerUpcard.getValue();
        int sum = hand.computeMaxSum();

        if (sum == Blackjack.MAX_VALID_HAND_POINTS) {
            return Stand;
        }

        boolean isDoubleDownPermittedHere = canDoubleDown(
                hand, isAfterSplit,
                bankrollAvailable);

        if (sum <= 8) {
            return Hit;
        }

        if (sum == 9) {
            if (upcardValue.ordinal() >= Three.ordinal() && upcardValue.ordinal() <= Six.ordinal()) {
                if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any) {
                    return DoubleDown;
                }
            }
            return Hit;
        }

        if (sum == 10) {
            if (upcardValue.isTen() || upcardValue == Ace) {
                return Hit;
            }
            return isDoubleDownPermittedHere ? DoubleDown : Hit;
        }

        if (sum == 11) {
            return isDoubleDownPermittedHere ? DoubleDown : Hit;
        }

        if (sum == 12) {
            if (upcardValue.ordinal() > Three.ordinal() && upcardValue.ordinal() < Seven.ordinal()) {
                return Stand;
            }
            return Hit;
        }

        // Note: sum will be >= 13 at this point.
        if (upcardValue.ordinal() <= Six.ordinal()) {
            return Stand;
        }

        // take care of the surrender scenarios now
        if (tableRules.canSurrender() && hand.getNumCards() == 2) {
            if (sum == 15 && (upcardValue.isTen() || upcardValue == Ace)) {
                return Surrender;
            }
            if (sum == 16 && (upcardValue == Nine || upcardValue.isTen() || upcardValue == Ace)) {
                return Surrender;
            }
            if (sum == 17 && upcardValue == Ace) {
                return Surrender;
            }
        }

        if (sum == 16 && upcardValue.isTen()) {
            return Stand;
        }

        if (sum >= 17) {
            return Stand;
        }

        if (upcardValue.ordinal() >= Seven.ordinal()) {
            return Hit;
        }

        return Stand;
    }

    private BlackjackPlay handleSoftHand(
            PlayerHand hand,
            Card dealerUpcard,
            boolean isAfterSplit,
            MoneyPile bankrollAvailable) {
        int sumWithLowAces = 0;
        for (Card card : hand.cards) {
            sumWithLowAces += card.getValue().getPoints();
        }

        int eleven = Blackjack.MAX_VALID_HAND_POINTS - OPTIONAL_EXTRA_ACE_POINTS;
        boolean notReallySoftHand = sumWithLowAces >= eleven;
        if (notReallySoftHand) {
            return handleHardHand(
                    hand,
                    dealerUpcard,
                    isAfterSplit,
                    bankrollAvailable);
        }

        Value upcardValue = dealerUpcard.getValue();

        boolean isDoubleDownPermittedHere = canDoubleDown(
                hand,
                isAfterSplit,
                bankrollAvailable);

        int sumWithoutOneAce = sumWithLowAces - 1;

        if (sumWithoutOneAce == 8) {
            if (upcardValue == Six) {
                if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any) {
                    return DoubleDown;
                }
                return Stand;
            }
        }
        if (sumWithoutOneAce >= 8) {
            return Stand;
        }

        if (sumWithoutOneAce == 7) {
            if (upcardValue == Seven || upcardValue == Eight) {
                return Stand;
            }
            if (upcardValue.ordinal() >= Nine.ordinal()) {
                return Hit;
            }
            if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any) {
                return DoubleDown;
            }
            return Stand;
        }

        if (upcardValue.ordinal() >= Seven.ordinal()) {
            return Hit;
        }

        boolean tryDoubling = false;
        if ((sumWithoutOneAce == 2 || sumWithoutOneAce == 3)
                && (upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if ((sumWithoutOneAce == 4 || sumWithoutOneAce == 5)
                && (upcardValue == Four || upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if (sumWithoutOneAce == 6
                && (upcardValue == Three || upcardValue == Four || upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if (tryDoubling) {
            if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == DoubleDownRuleOptions.Any) {
                return DoubleDown;
            }
        }

        return Hit;
    }

    private BlackjackPlay handlePair(
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {

        if (hand.getNumCards() != 2) {
            throw new RuntimeException("Bug! Hand: " + hand);
        }

        Value value = hand.getFirstCard().getValue();
        Value upcardValue = dealerUpcard.getValue();

        boolean canSplit = canHandBeSplit(hand, bankrollAvailable);
        boolean canDoubleDown = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());

        switch (value) {
            case Two:
            case Three:
                if (upcardValue.ordinal() > Seven.ordinal()) {
                    return Hit;
                }
                if (!canSplit) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            case Four:
                if (upcardValue.ordinal() < Five.ordinal()) {
                    return Hit;
                }
                if (upcardValue == Five || upcardValue == Six) {
                    if (!canSplit) {
                        return handleHardHand(
                                hand,
                                dealerUpcard,
                                true,
                                bankrollAvailable);
                    }
                    return Split;
                }
                return Hit;
            case Five:
                if (upcardValue.isTen() || upcardValue == Ace) {
                    return Hit;
                }
                if (hand.getSeat().getNumSplitsSoFar() > 0 && !tableRules.canDoubleDownAfterSplit()) {
                    return Hit;
                }
                if (canDoubleDown) {
                    return DoubleDown;
                }
                return Hit;
            case Six:
                if (upcardValue.ordinal() > Six.ordinal()) {
                    return Hit;
                }
                if (!canSplit) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            case Seven:
                if (upcardValue.isTen()) {
                    if (tableRules.canSurrender()) {
                        return Surrender;
                    }
                }
                if (upcardValue.ordinal() > Seven.ordinal()) {
                    return Hit;
                }
                if (!canSplit) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            case Eight:
                if (!canSplit) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            case Nine:
                if (upcardValue == Seven || upcardValue.isTen() || upcardValue == Ace) {
                    return Stand;
                }
                if (!canSplit) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            case Ten:
            case Jack:
            case Queen:
            case King:
                return Stand;
            case Ace:
                boolean acesHaveAlreadyBeenSplit = !tableRules.canHitSplitAces() && hand.getSeat().getNumSplitsSoFar() > 0;
                if (acesHaveAlreadyBeenSplit) {
                    return Hit;
                }
                if (!canSplit) {
                    return handleSoftHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable);
                }
                return Split;
            default:
                throw new RuntimeException("Bug!");
        }
    }
}
