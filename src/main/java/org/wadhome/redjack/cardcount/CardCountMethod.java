package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.Card;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.rules.TableRules;

public abstract class CardCountMethod {

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

    public abstract CardCountStatus getCardCountStatus();

    public abstract void observeCard(Card card);

    public abstract void observeShuffle();

    public abstract void getBet(BetRequest betRequest);

    static int roundToInt(double value) {
        return (int) (value + 0.5D);
    }

    static double roundToHalf(double value) {
        double twiceValue = value * 2.0D;
        int rounded = roundToInt(twiceValue);
        return ((double) rounded) / 2.0D;
    }

    public BukofskyBankrollLevel getBukofskyBankrollLevelDesired() {
        return bukofskyBankrollLevelDesired;
    }

    public void setBukofskyBankrollLevelDesired(BukofskyBankrollLevel bukofskyBankrollLevelDesired) {
        this.bukofskyBankrollLevelDesired = bukofskyBankrollLevelDesired;
    }
}
