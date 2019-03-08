package org.wadhome.redjack;

abstract class TestHelper {

    // The deck number cycles, so that the sets in hands don't collapse duplicate cards.
    private int deckNumber = 0;

    Card c(Value value) {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Clubs, value);
    }

    Card c2() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Two);
    }

    Card c3() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Three);
    }

    Card c4() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Four);
    }

    Card c5() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Five);
    }

    Card c6() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Six);
    }

    Card c7() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Seven);
    }

    Card c8() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Eight);
    }

    Card c9() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Nine);
    }

    Card cT() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Ten);
    }

    Card cA() {
        checkDeckNumber();
        return new Card(deckNumber++, Suite.Spades, Value.Ace);
    }

    private void checkDeckNumber() {
        if (deckNumber > Deck.MAX_DECK_NUMBER) {
            deckNumber = 0;
        }
    }
}
