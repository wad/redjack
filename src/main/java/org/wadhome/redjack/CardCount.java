package org.wadhome.redjack;

abstract class CardCount {
    protected int numDecks;

    CardCount(int numDecks) {
        this.numDecks = numDecks;
    }

    abstract void observeCard(Card newCardsSeen);

    abstract void newShoe();

    protected int roundToInt(double value) {
        return (int) (value + 0.5D);
    }
}
