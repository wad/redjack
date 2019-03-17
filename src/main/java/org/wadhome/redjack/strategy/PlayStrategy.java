package org.wadhome.redjack.strategy;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.cardcount.CardCountMethod;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRules;

import static org.wadhome.redjack.Value.Ace;

public abstract class PlayStrategy {

    protected Table table;
    protected TableRules tableRules;
    private CardCountMethod cardCountMethod;

    PlayStrategy(
            Table table,
            CardCountMethod cardCountMethod) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.cardCountMethod = cardCountMethod;
    }

    public abstract MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable);

    public void getBet(BetRequest betRequest) {
        getCardCountMethod().getBet(betRequest);
    }

    public abstract BlackjackPlay choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable);

    boolean canHandBeSplit(
            PlayerHand hand,
            MoneyPile bankrollAvailable) {
        int numSplitsSoFar = hand.getSeat().getNumSplitsSoFar();

        boolean canAffordToSplit = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        if (!canAffordToSplit) {
            return false;
        }

        boolean splitsAllUsedUp = numSplitsSoFar >= tableRules.getMaxNumSplits();
        if (splitsAllUsedUp) {
            return false;
        }

        boolean isPairOfAces = hand.getFirstCard().getValue() == Ace;
        if (isPairOfAces) {
            if (!tableRules.canHitSplitAces()) {
                //noinspection RedundantIfStatement
                if (numSplitsSoFar > 0) {
                    // can't split aces if it's not the first split, and the table rules say you can't hit split aces.
                    return false;
                }
            }
        }

        return true;
    }

    boolean canDoubleDown(
            PlayerHand hand,
            boolean isAfterSplit,
            MoneyPile bankrollAvailable) {

        if (isAfterSplit && !tableRules.canDoubleDownAfterSplit()) {
            return false;
        }

        boolean hasFundsToCoverDoubleDown = bankrollAvailable.isGreaterThanOrEqualTo(hand.getBetAmount());
        if (!hasFundsToCoverDoubleDown) {
            return false;
        }

        return hand.getNumCards() == 2;
    }

    void validateHand(PlayerHand hand) {
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

    public CardCountMethod getCardCountMethod() {
        return this.cardCountMethod;
    }
}
