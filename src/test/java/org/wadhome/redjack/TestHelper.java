package org.wadhome.redjack;

import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrue;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Deck;
import org.wadhome.redjack.casino.Suite;
import org.wadhome.redjack.casino.Value;

public abstract class TestHelper {

    // The deck number cycles, so that the sets in hands don't collapse duplicate cards.
    private int deckNumber = 0;

    protected Card c(Value value) {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Clubs, value);
    }

    protected Card c2() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Two);
    }

    protected Card c3() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Three);
    }

    protected Card c4() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Four);
    }

    protected Card c5() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Five);
    }

    protected Card c6() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Six);
    }

    protected Card c7() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Seven);
    }

    protected Card c8() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Eight);
    }

    protected Card c9() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Nine);
    }

    protected Card cT() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Ten);
    }

    protected Card cA() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Ace);
    }

    protected CardCountStatus rc(int runningCount) {
        return new CardCountStatusRunningAndTrue(runningCount, 0);
    }

    protected CardCountStatus tc(int trueCount) {
        return new CardCountStatusRunningAndTrue(0, trueCount);
    }

    private void checkDeckNumber() {
        if (deckNumber > Deck.MAX_DECK_NUMBER) {
            deckNumber = 0;
        }
    }
}
