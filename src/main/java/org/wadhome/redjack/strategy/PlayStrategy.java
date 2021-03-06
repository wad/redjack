package org.wadhome.redjack.strategy;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.cardcount.CardCountMethod;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.PlayerHand;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.rules.PlayerDecision;
import org.wadhome.redjack.rules.TableRules;

import static org.wadhome.redjack.casino.Value.Ace;

public abstract class PlayStrategy {

    Table table;
    TableRules tableRules;
    private CardCountMethod cardCountMethod;

    PlayStrategy(
            Table table,
            CardCountMethod cardCountMethod) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.cardCountMethod = cardCountMethod;
    }

    public abstract CurrencyAmount getInsuranceBet(
            CurrencyAmount maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            CurrencyAmount bankrollAvailable);

    public void getBet(BetRequest betRequest) {
        getCardCountMethod().getBet(betRequest);
    }

    public abstract PlayerDecision choosePlay(
            Player player,
            PlayerHand hand,
            Card dealerUpcard);

    boolean canHandBeSplit(
            PlayerHand hand,
            CurrencyAmount bankrollAvailable) {
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
            if (!tableRules.getCanHitSplitAces()) {
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
            CurrencyAmount bankrollAvailable) {

        if (isAfterSplit && !tableRules.getCanDoubleDownAfterSplit()) {
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
