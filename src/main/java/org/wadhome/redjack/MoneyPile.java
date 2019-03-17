package org.wadhome.redjack;

import java.util.Objects;

public class MoneyPile {

    public static final int NUM_CENTS_PER_DOLLAR = 100;

    private long numCents;

    public MoneyPile(long numCents) {
        this.numCents = numCents;
        validate();
    }

    MoneyPile(MoneyPile... moneyPiles) {
        this(0L);
        for (MoneyPile moneyPile : moneyPiles) {
            this.numCents += moneyPile.numCents;
        }
    }

    public void addToPile(MoneyPile moneyToAdd) {
        this.numCents += moneyToAdd.numCents;
        validate();
    }

    void subtractFromPile(MoneyPile moneyToSubtract) {
        this.numCents -= moneyToSubtract.numCents;
        validate();
    }

    public boolean isGreaterThan(MoneyPile target) {
        return this.numCents > target.numCents;
    }

    public boolean isLessThan(MoneyPile target) {
        return this.numCents < target.numCents;
    }

    public boolean isGreaterThanOrEqualTo(MoneyPile target) {
        return this.numCents >= target.numCents;
    }

    public boolean isGreaterThanOrEqualTo(long dollars) {
        return this.numCents >= (dollars * NUM_CENTS_PER_DOLLAR);
    }

    boolean hasMoney() {
        return numCents > 0L;
    }

    // todo: make this private, clean up usage
    public MoneyPile copy() {
        return new MoneyPile(numCents);
    }

    public MoneyPile computeHalf() {
        return new MoneyPile(this.numCents >> 1);
    }

    public MoneyPile computeOneAndHalf() {
        return new MoneyPile(this.numCents + (this.numCents >> 1));
    }

    public MoneyPile computeSixFifths() {
        return new MoneyPile((long) (((double) this.numCents) * (1.2D)));
    }

    public MoneyPile computeDouble() {
        return new MoneyPile(this.numCents << 1);
    }

    public static String computeDifference(
            MoneyPile original,
            MoneyPile amountToSubtract) {
        long delta = original.numCents - amountToSubtract.numCents;
        long absoluteValue = delta;
        boolean isNegative = delta < 0;
        if (isNegative) {
            absoluteValue = delta * -1;
        }

        MoneyPile moneyPile = new MoneyPile(absoluteValue);
        String winOrLoseWord = isNegative ? "lost " : "won ";
        return winOrLoseWord + moneyPile.toString();
    }

    private void validate() {
        if (numCents < 0L) {
            throw new RuntimeException("Cannot have a negative money pile.");
        }
    }

    public static MoneyPile zero() {
        return new MoneyPile(0L);
    }

    String format(boolean includeDollarSign) {
        long dollars = numCents / NUM_CENTS_PER_DOLLAR;
        long cents = numCents % NUM_CENTS_PER_DOLLAR;
        String dollarString = String.valueOf(dollars);
        String centsString = String.format("%02d", cents);
        return (includeDollarSign ? "$" : "") + dollarString + "." + centsString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyPile moneyPile = (MoneyPile) o;
        return numCents == moneyPile.numCents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCents);
    }

    @Override
    public String toString() {
        return format(true);
    }
}
