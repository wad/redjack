package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.Card;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;

public class CardCountMethodNone extends CardCountMethod {

    public CardCountMethodNone(
            Table table,
            BettingStrategy bettingStrategy) {
        super(table, bettingStrategy);
    }

    @Override
    public CardCountStatus getCardCountStatus() {
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
