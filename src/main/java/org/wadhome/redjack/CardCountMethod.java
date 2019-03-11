package org.wadhome.redjack;

abstract class CardCountMethod {

    Table table;
    TableRules tableRules;
    BettingStrategy bettingStrategy;

    CardCountMethod(
            Table table,
            BettingStrategy bettingStrategy) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.bettingStrategy = bettingStrategy;
    }

    abstract void observeCard(Card card);

    abstract void observeShuffle();

    abstract MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Player player);

    static int roundToInt(double value) {
        return (int) (value + 0.5D);
    }

    static double roundToHalf(double value) {
        double twiceValue = value * 2.0D;
        int rounded = roundToInt(twiceValue);
        return ((double)rounded) / 2.0D;
    }
}
