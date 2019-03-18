package org.wadhome.redjack.money;

public class MoneyPile {

    // This represents all money. New MoneyPile objects can only come from this pile.
    private static final MoneyPile federalReserve = new MoneyPile(new CurrencyAmount(1000000000000000L));

    private CurrencyAmount currencyAmount;

    private MoneyPile(CurrencyAmount currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public void moveMoneyToTargetPile(MoneyPile target, CurrencyAmount amount) {
        if (!canExtract(amount)) {
            throw new IllegalStateException("Bug! Check first.");
        }

        target.currencyAmount.increaseBy(amount);
        currencyAmount.reduceBy(amount);
    }

    public static MoneyPile extractMoneyFromPileToMakeNewPile(
            CurrencyAmount amountToExtract,
            MoneyPile fromPile) {
        if (!fromPile.canExtract(amountToExtract)) {
            throw new RuntimeException("Bug! Check first.");
        }

        fromPile.currencyAmount.reduceBy(amountToExtract);
        return new MoneyPile(amountToExtract);
    }

    public static MoneyPile extractMoneyFromFederalReserve(CurrencyAmount amountToExtract) {
        return extractMoneyFromPileToMakeNewPile(amountToExtract, federalReserve);
    }

    public CurrencyAmount getCurrencyAmountCopy() {
        return currencyAmount.copy();
    }

    public boolean canExtract(CurrencyAmount amountToCheck) {
        return amountToCheck.isLessThanOrEqualTo(currencyAmount);
    }

    public boolean isGreaterThanOrEqualTo(CurrencyAmount target) {
        return this.currencyAmount.isGreaterThanOrEqualTo(target);
    }

    public CurrencyAmount compute(CurrencyComputation currencyComputation) {
        return currencyAmount.compute(currencyComputation);
    }

    public String format(boolean includeDollarSign) {
        return currencyAmount.format(includeDollarSign);
    }

    @Override
    public String toString() {
        return format(true);
    }
}
