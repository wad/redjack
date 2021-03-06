package org.wadhome.redjack.cardcount;

import org.wadhome.redjack.bet.BetRequest;
import org.wadhome.redjack.bet.BettingStrategy;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.casino.Card;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.rules.TableRules;

public abstract class CardCountMethod {

    Table table;
    TableRules tableRules;
    BettingStrategy bettingStrategy;
    private BukofskyBankrollLevel bukofskyBankrollLevelDesired = null;

    // used for testing
    private CardCountStatus overriddenCardCountStatus = null;

    CardCountMethod(
            Table table,
            BettingStrategy bettingStrategy) {
        this.table = table;
        this.tableRules = table.getTableRules();
        this.bettingStrategy = bettingStrategy;
        resetCounts();
    }

    public CardCountStatus getCardCountStatus() {
        if (overriddenCardCountStatus != null) {
            CardCountStatus toReturn = overriddenCardCountStatus;
            overriddenCardCountStatus = null;
            return toReturn;
        }

        return getCardCountStatusHelper();
    }

    protected abstract void resetCounts();

    protected abstract CardCountStatus getCardCountStatusHelper();

    public void temporarilyOverrideCardCountStatus(CardCountStatus cardCountStatus) {
        overriddenCardCountStatus = cardCountStatus;
    }

    public abstract void observeCard(Card card);

    public void observeShuffle() {
        resetCounts();
    }

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
