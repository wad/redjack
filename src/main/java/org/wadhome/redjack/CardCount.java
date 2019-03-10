package org.wadhome.redjack;

abstract class CardCount {
    protected int numDecks;

    CardCount(int numDecks) {
        this.numDecks = numDecks;
    }

    abstract void observeCard(Card newCardsSeen);

    abstract void newShoe();

    protected static int roundToInt(double value) {
        return (int) (value + 0.5D);
    }

    protected static double roundToHalf(double value) {
        double twiceValue = value * 2.0D;
        int rounded = roundToInt(twiceValue);
        return ((double)rounded) / 2.0D;
    }
}
