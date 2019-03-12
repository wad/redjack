package org.wadhome.redjack;

import static org.wadhome.redjack.Value.Ace;

abstract class PlayStrategy {

    protected Table table;
    protected TableRules tableRules;
    protected CardCountMethod cardCountMethod;

    PlayStrategy(
            Table table,
            CardCountMethod cardCountMethod) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.cardCountMethod = cardCountMethod;
    }

    abstract MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable);

    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Player player) { // some betting strategies want to know the initial bankroll amount
        return getCardCountMethod().getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet,
                player,
                table.getCasino().getRandomness());
    }

    abstract BlackjackPlay choosePlay(
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable);

    protected boolean canHandBeSplit(
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

    protected boolean canDoubleDown(
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

    protected void validateHand(PlayerHand hand) {
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
