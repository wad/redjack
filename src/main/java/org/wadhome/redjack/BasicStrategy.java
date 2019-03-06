package org.wadhome.redjack;

import static org.wadhome.redjack.BlackjackPlay.*;
import static org.wadhome.redjack.Value.*;

class BasicStrategy {
    static BlackjackPlay compute(
            PlayerHand hand,
            Card dealerUpcard,
            int numSplitsSoFar,
            MoneyPile bankrollAvailable,
            TableRules tableRules) {
        validateHand(hand);

        if (hand.isPair()) {
            return handlePair(
                    hand,
                    dealerUpcard,
                    numSplitsSoFar,
                    bankrollAvailable,
                    tableRules);
        }

        if (hand.hasAtLeastOneAce()) {
            return handleSoftHand(
                    hand,
                    dealerUpcard,
                    false,
                    bankrollAvailable,
                    tableRules);
        }

        return handleHardHand(
                hand,
                dealerUpcard,
                false,
                bankrollAvailable,
                tableRules);
    }

    private static BlackjackPlay handleHardHand(
            PlayerHand hand,
            Card dealerUpcard,
            boolean isAfterSplit,
            MoneyPile bankrollAvailable,
            TableRules tableRules) {
        Value upcardValue = dealerUpcard.getValue();
        int sum = hand.computeMaxSum();

        if (sum == TableRules.MAX_VALID_HAND_POINTS) {
            return Stand;
        }

        boolean isDoubleDownPermittedHere = true;
        if (isAfterSplit && !tableRules.canDoubleDownAfterSplit()) {
            isDoubleDownPermittedHere = false;
        }

        boolean hasFundsToCoverDoubleDown = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        if (!hasFundsToCoverDoubleDown) {
            isDoubleDownPermittedHere = false;
        }

        if (sum <= 8) {
            return Hit;
        }

        if (sum == 9) {
            if (upcardValue.ordinal() >= Three.ordinal() && upcardValue.ordinal() <= Six.ordinal()) {
                if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == TableRules.DoubleDownOptions.Any) {
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

    private static BlackjackPlay handleSoftHand(
            PlayerHand hand,
            Card dealerUpcard,
            boolean isAfterSplit,
            MoneyPile bankrollAvailable,
            TableRules tableRules) {
        int sumWithLowAces = 0;
        for (Card card : hand.cards) {
            sumWithLowAces = card.getValue().getPoints();
        }

        int eleven = TableRules.MAX_VALID_HAND_POINTS - OPTIONAL_EXTRA_ACE_POINTS;
        boolean notReallySoftHand = sumWithLowAces >= eleven;
        if (notReallySoftHand) {
            return handleHardHand(
                    hand,
                    dealerUpcard,
                    isAfterSplit,
                    bankrollAvailable,
                    tableRules);
        }

        Value upcardValue = dealerUpcard.getValue();

        boolean isDoubleDownPermittedHere = true;
        if (isAfterSplit && !tableRules.canDoubleDownAfterSplit()) {
            isDoubleDownPermittedHere = false;
        }

        boolean hasFundsToCoverDoubleDown = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        if (!hasFundsToCoverDoubleDown) {
            isDoubleDownPermittedHere = false;
        }

        int sumWithoutOneAce = sumWithLowAces - 1;

        if (sumWithoutOneAce == 8) {
            if (upcardValue == Six) {
                if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == TableRules.DoubleDownOptions.Any) {
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
            if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == TableRules.DoubleDownOptions.Any) {
                return DoubleDown;
            }
            return Stand;
        }

        if (upcardValue.ordinal() >= Seven.ordinal()) {
            return Hit;
        }

        boolean tryDoubling = false;
        if (sumWithoutOneAce <= 3
                && (upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if (sumWithoutOneAce <= 5
                && (upcardValue == Four || upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if (sumWithoutOneAce == 6
                && (upcardValue == Three || upcardValue == Four || upcardValue == Five || upcardValue == Six)) {
            tryDoubling = true;
        }
        if (tryDoubling) {
            if (isDoubleDownPermittedHere && tableRules.getDoubleDownOptions() == TableRules.DoubleDownOptions.Any) {
                return DoubleDown;
            }
        }

        return Hit;
    }

    private static BlackjackPlay handlePair(
            PlayerHand hand,
            Card dealerUpcard,
            int numSplitsSoFar,
            MoneyPile bankrollAvailable,
            TableRules tableRules) {
        Value value = hand.getFirstCard().getValue();
        Value upcardValue = dealerUpcard.getValue();
        boolean splitsAllUsedUp = numSplitsSoFar >= tableRules.getMaxNumSplits();

        boolean isDoubleDownPermittedHere = true;
        boolean hasFundsToCoverDoubleDown = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        if (!hasFundsToCoverDoubleDown) {
            isDoubleDownPermittedHere = false;
        }

        switch (value) {
            case Two:
            case Three:
                if (upcardValue.ordinal() > Seven.ordinal()) {
                    return Hit;
                }
                if (splitsAllUsedUp) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            case Four:
                if (upcardValue.ordinal() < Five.ordinal()) {
                    return Hit;
                }
                if (upcardValue == Five || upcardValue == Six) {
                    if (splitsAllUsedUp) {
                        return handleHardHand(
                                hand,
                                dealerUpcard,
                                true,
                                bankrollAvailable,
                                tableRules);
                    }
                    return Split;
                }
                return Hit;
            case Five:
                if (upcardValue.isTen() || upcardValue == Ace) {
                    return Hit;
                }
                if (numSplitsSoFar > 0 && !tableRules.canDoubleDownAfterSplit()) {
                    return Hit;
                }
                if (isDoubleDownPermittedHere) {
                    return DoubleDown;
                }
                return Hit;
            case Six:
                if (upcardValue.ordinal() > Six.ordinal()) {
                    return Hit;
                }
                if (splitsAllUsedUp) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            case Seven:
                if (upcardValue.isTen()) {
                    if (tableRules.canSurrender() && hand.getNumCards() == 2) {
                        return Surrender;
                    }
                }
                if (upcardValue.ordinal() > Seven.ordinal()) {
                    return Hit;
                }
                if (splitsAllUsedUp) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            case Eight:
                if (splitsAllUsedUp) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            case Nine:
                if (upcardValue == Seven || upcardValue.isTen() || upcardValue == Ace) {
                    return Stand;
                }
                if (splitsAllUsedUp) {
                    return handleHardHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            case Ten:
            case Jack:
            case Queen:
            case King:
                return Stand;
            case Ace:
                boolean acesHaveAlreadyBeenSplit = !tableRules.canHitSplitAces() && numSplitsSoFar > 0;
                if (acesHaveAlreadyBeenSplit) {
                    return Hit;
                }
                if (splitsAllUsedUp) {
                    return handleSoftHand(
                            hand,
                            dealerUpcard,
                            true,
                            bankrollAvailable,
                            tableRules);
                }
                return Split;
            default:
                throw new RuntimeException("Bug!");
        }
    }

    private static void validateHand(PlayerHand hand) {
        if (hand.getNumCards() < 2) {
            throw new RuntimeException("Less than 2 cards.");
        }
        if (hand.isBust()) {
            throw new RuntimeException("Already busted.");
        }
        if (hand.isBlackjack()) {
            throw new RuntimeException("Already blackjack.");
        }
    }
}
