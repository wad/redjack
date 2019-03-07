package org.wadhome.redjack;

public class Card {
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

    @Override
    public int hashCode() {
        if (deckNumber > Deck.MAX_DECK_NUMBER) {
            throw new RuntimeException("Deck number too large.");
        }

        return (deckNumber * 1000) + (value.ordinal() * 10) + (suite.ordinal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return deckNumber == card.deckNumber &&
                suite == card.suite &&
                value == card.value;
    }
}
