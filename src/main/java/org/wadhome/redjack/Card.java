package org.wadhome.redjack;

public class Card
{
    // todo: compress these three values to just in encoded in bits in a single int
    private int deckNumber;
    private Suite suite;
    private Value value;

    // this is used in test code
    Card(Value value)
    {
        this(0, Suite.Clubs, value);
    }

    Card(
            int deckNumber,
            Suite suite,
            Value value)
    {
        this.deckNumber = deckNumber;
        this.suite = suite;
        this.value = value;
    }

    public Value getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value.toString() + suite.toString();
    }

    public String print(boolean includeDeckNumber)
    {
        return (includeDeckNumber ? deckNumber + ":" : "") + toString();
    }

    @Override
    public int hashCode()
    {
        if (deckNumber > Deck.MAX_DECK_NUMBER)
        {
            throw new RuntimeException("Deck number too large.");
        }

        return (deckNumber * 1000) + (value.ordinal() * 10) + (suite.ordinal());
    }

    // These are used in tests
    public static Card two()
    {
        return new Card(0, Suite.Spades, Value.Two);
    }

    public static Card three()
    {
        return new Card(0, Suite.Spades, Value.Three);
    }

    public static Card four()
    {
        return new Card(0, Suite.Spades, Value.Four);
    }

    public static Card five()
    {
        return new Card(0, Suite.Spades, Value.Five);
    }

    public static Card six()
    {
        return new Card(0, Suite.Spades, Value.Six);
    }

    public static Card seven()
    {
        return new Card(0, Suite.Spades, Value.Seven);
    }

    public static Card eight()
    {
        return new Card(0, Suite.Spades, Value.Eight);
    }

    public static Card nine()
    {
        return new Card(0, Suite.Spades, Value.Nine);
    }

    public static Card ten()
    {
        return new Card(0, Suite.Spades, Value.Ten);
    }

    public static Card ace()
    {
        return new Card(0, Suite.Spades, Value.Ace);
    }
}
