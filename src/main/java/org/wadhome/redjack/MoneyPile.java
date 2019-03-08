package org.wadhome.redjack;

public class MoneyPile {
    private long numCents;

    public MoneyPile(long numCents) {
        this.numCents = numCents;
        validate();
    }

    public MoneyPile(MoneyPile... moneyPiles) {
        this(0L);
        for (MoneyPile moneyPile : moneyPiles) {
            this.numCents += moneyPile.numCents;
        }
    }

    void addToPile(MoneyPile moneyToAdd) {
        this.numCents += moneyToAdd.numCents;
        validate();
    }

    void subtractFromPile(MoneyPile moneyToSubtract) {
        this.numCents -= moneyToSubtract.numCents;
        validate();
    }

    boolean isLessThan(MoneyPile target) {
        return this.numCents < target.numCents;
    }

    boolean isGreaterThan(MoneyPile target) {
        return this.numCents > target.numCents;
    }

    boolean isGreaterThanOrEqualTo(MoneyPile target) {
        return this.numCents >= target.numCents;
    }

    boolean hasMoney() {
        return numCents > 0L;
    }

    MoneyPile copy() {
        return new MoneyPile(numCents);
    }

    MoneyPile computeHalf() {
        return new MoneyPile(this.numCents >> 1);
    }

    MoneyPile computeOneAndHalf() {
        return new MoneyPile(this.numCents + (this.numCents >> 1));
    }

    MoneyPile computeDouble() {
        return new MoneyPile(this.numCents + this.numCents);
    }

    private void validate() {
        if (numCents < 0L) {
            throw new RuntimeException("Cannot have a negative money pile.");
        }
    }

    static MoneyPile zero() {
        return new MoneyPile(0L);
    }

    @Override
    public String toString() {
        long dollars = numCents / 100;
        long cents = numCents % 100;
        String dollarString = String.valueOf(dollars);
        String centsString = String.format("%02d", cents);
        return "$" + dollarString + "." + centsString;
    }
}
