package org.wadhome.redjack.money;

public class CurrencyAmount {

    private static final int NUM_CENTS_PER_DOLLAR = 100;

    private long numCents;

    public CurrencyAmount(long numDollars) {
        this(numDollars, 0);
    }

    public CurrencyAmount(
            long numDollars,
            long numCents) {
        if (numDollars < 0 || numCents < 0) {
            throw new IllegalStateException("Cannot have negative dollar amount");
        }

        this.numCents = (numDollars * NUM_CENTS_PER_DOLLAR) + numCents;
    }

    public static CurrencyAmount zero() {
        return new CurrencyAmount(0L);
    }

    public CurrencyAmount copy() {
        return new CurrencyAmount(0L, numCents);
    }

    public String describeDifference(CurrencyAmount target) {
        long delta = numCents - target.numCents;
        long absoluteValue = delta;
        boolean isNegative = delta < 0;
        if (isNegative) {
            absoluteValue = delta * -1;
        }

        CurrencyAmount currencyAmount = new CurrencyAmount(0L, absoluteValue);
        String winOrLoseWord = isNegative ? "lost " : "won ";
        return winOrLoseWord + currencyAmount.format(true);
    }

    public boolean isZero() {
        return numCents == 0L;
    }

    public boolean isLessThanOrEqualTo(CurrencyAmount currencyAmount) {
        return numCents <= currencyAmount.numCents;
    }

    public boolean isLessThan(CurrencyAmount currencyAmount) {
        return numCents < currencyAmount.numCents;
    }

    public boolean isGreaterThanOrEqualTo(CurrencyAmount currencyAmount) {
        return numCents >= currencyAmount.numCents;
    }

    public boolean isGreaterThanOrEqualToDollars(long numDollars) {
        return numCents >= numDollars * NUM_CENTS_PER_DOLLAR;
    }

    public boolean isGreaterThan(CurrencyAmount currencyAmount) {
        return numCents > currencyAmount.numCents;
    }

    public CurrencyAmount compute(CurrencyComputation currencyComputation) {
        switch (currencyComputation) {
            case halfOf:
                return new CurrencyAmount(0L, numCents >> 1);
            case doubleOf:
                return new CurrencyAmount(0L, numCents << 1);
            case oneAndHalfOf:
                return new CurrencyAmount(0L, numCents + (numCents >> 1));
            case sixFifthsOf:
                return new CurrencyAmount(0L, (long) (((double) this.numCents) * (1.2D)));
            default:
                throw new IllegalStateException("bug");
        }
    }

    @Override
    public String toString() {
        return format(true);
    }

    String format(boolean includeDollarSign) {
        long dollars = numCents / NUM_CENTS_PER_DOLLAR;
        long cents = numCents % NUM_CENTS_PER_DOLLAR;
        String dollarString = String.valueOf(dollars);
        String centsString = String.format("%02d", cents);
        return (includeDollarSign ? "$" : "") + dollarString + "." + centsString;
    }

    public void reduceBy(CurrencyAmount currencyAmount) {
        this.numCents -= currencyAmount.numCents;
    }

    public void increaseBy(CurrencyAmount currencyAmount) {
        this.numCents += currencyAmount.numCents;
    }
}
