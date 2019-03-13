package org.wadhome.redjack;

abstract class CardCountMethod {

    Table table;
    TableRules tableRules;
    BettingStrategy bettingStrategy;
    private BukofskyBankrollLevel bukofskyBankrollLevelDesired = null;

    CardCountMethod(
            Table table,
            BettingStrategy bettingStrategy) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.bettingStrategy = bettingStrategy;
    }

    abstract String reportOnCurrentCardCount();

    abstract void observeCard(Card card);

    abstract void observeShuffle();

    abstract MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Player player,
            Randomness randomness);

    Randomness getRandomness() {
        return table.getCasino().getRandomness();
    }

    static int roundToInt(double value) {
        return (int) (value + 0.5D);
    }

    static double roundToHalf(double value) {
        double twiceValue = value * 2.0D;
        int rounded = roundToInt(twiceValue);
        return ((double)rounded) / 2.0D;
    }

    BukofskyBankrollLevel getBukofskyBankrollLevelDesired() {
        return bukofskyBankrollLevelDesired;
    }

    void setBukofskyBankrollLevelDesired(BukofskyBankrollLevel bukofskyBankrollLevelDesired) {
        this.bukofskyBankrollLevelDesired = bukofskyBankrollLevelDesired;
    }
}
