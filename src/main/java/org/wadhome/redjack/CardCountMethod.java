package org.wadhome.redjack;

abstract class CardCountMethod {

    protected TableRules tableRules;
    protected BettingStrategy bettingStrategy;

    CardCountMethod(
            TableRules tableRules,
            BettingStrategy bettingStrategy) {
        this.tableRules = tableRules;
        this.bettingStrategy = bettingStrategy;
    }

    abstract void observeCard(Card card);

    abstract void observeShuffle();

    protected static int roundToInt(double value) {
        return (int) (value + 0.5D);
    }

    protected static double roundToHalf(double value) {
        double twiceValue = value * 2.0D;
        int rounded = roundToInt(twiceValue);
        return ((double)rounded) / 2.0D;
    }
}
