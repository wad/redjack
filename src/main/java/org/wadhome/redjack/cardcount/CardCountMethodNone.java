package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Table;

public class CardCountMethodNone extends CardCountMethod {

    public CardCountMethodNone(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
    }

    @Override
    protected void resetCounts() {
    }

    @Override
    protected CardCountStatus getCardCountStatusHelper() {
        return new CardCountStatusNone();
    }

    @Override
    public void observeCard(Card card) {
        // do nothing
    }

    @Override
    public void observeShuffle() {
        // do nothing
    }

    @Override
    public void getBet(BetRequest betRequest) {
        betRequest.setTrueCount(0); // there really is none, but whatever.
        bettingStrategy.getBet(betRequest);
    }
}
