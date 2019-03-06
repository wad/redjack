package org.wadhome.redjack;

public class MoneyPile
{
    long numCents;

    public MoneyPile(long numCents)
    {
        this.numCents = numCents;
        validate();
    }

    public void add(MoneyPile moneyToAdd)
    {
        this.numCents += moneyToAdd.numCents;
        validate();
    }

    public void subtract(MoneyPile moneyToSubtract)
    {
        this.numCents -= moneyToSubtract.numCents;
        validate();
    }

    public boolean isLessThan(MoneyPile target)
    {
        return this.numCents < target.numCents;
    }

    public boolean isGreaterThan(MoneyPile target)
    {
        return this.numCents > target.numCents;
    }

    public boolean isGreaterThanOrEqualTo(MoneyPile target)
    {
        return this.numCents >= target.numCents;
    }

    public boolean isZero()
    {
        return numCents == 0L;
    }

    public MoneyPile copy()
    {
        return new MoneyPile(numCents);
    }

    public MoneyPile computeHalf()
    {
        return new MoneyPile(this.numCents >> 1);
    }

    public MoneyPile computeOneAndHalf()
    {
        return new MoneyPile(this.numCents + (this.numCents >> 1));
    }

    public MoneyPile computeDouble()
    {
        return new MoneyPile(this.numCents + this.numCents);
    }

    private void validate()
    {
        if (numCents < 0L)
        {
            throw new RuntimeException("Cannot have a negative money pile.");
        }
    }

    public static MoneyPile zero()
    {
        return new MoneyPile(0L);
    }

    @Override
    public String toString()
    {
        long dollars = numCents / 100;
        long cents = numCents % 100;
        String dollarString = String.valueOf(dollars);
        String centsString = String.format("%02d", cents);
        return "$" + dollarString + "." + centsString;
    }
}
