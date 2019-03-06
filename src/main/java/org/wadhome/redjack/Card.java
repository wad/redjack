package org.wadhome.redjack;

public class Card {
    // todo: compress these three values to just in encoded in bits in a single int
    private int deckNumber;
    private Suite suite;
    private Value value;

    Card(
            int deckNumber,
            Suite suite,
            Value value) {
        this.deckNumber = deckNumber;
        this.suite = suite;
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString() + suite.toString();
    }

    public String print(boolean includeDeckNumber) {
        return (includeDeckNumber ? deckNumber + ":" : "") + toString();
    }

    @Override
    public int hashCode() {
        if (deckNumber > Deck.MAX_DECK_NUMBER) {
            throw new RuntimeException("Deck number too large.");
        }

        return (deckNumber * 1000) + (value.ordinal() * 10) + (suite.ordinal());
    }
}
