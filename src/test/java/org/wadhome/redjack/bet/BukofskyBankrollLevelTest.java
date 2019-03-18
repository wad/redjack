package org.wadhome.redjack.bet;

import org.junit.Test;
import org.wadhome.redjack.money.CurrencyAmount;

import static org.junit.Assert.assertEquals;
import static org.wadhome.redjack.bet.BukofskyBankrollLevel.*;

public class BukofskyBankrollLevelTest {
    @Test
    public void testDetermine() {
        assertEquals(LevelZero, BukofskyBankrollLevel.determine(new CurrencyAmount(0L)));

        assertEquals(LevelZero, BukofskyBankrollLevel.determine(new CurrencyAmount(1999L)));
        assertEquals(Level2k, BukofskyBankrollLevel.determine(new CurrencyAmount(2000L)));
        assertEquals(Level2k, BukofskyBankrollLevel.determine(new CurrencyAmount(2001L)));

        assertEquals(Level2k, BukofskyBankrollLevel.determine(new CurrencyAmount(4999L)));
        assertEquals(Level5k, BukofskyBankrollLevel.determine(new CurrencyAmount(5000L)));
        assertEquals(Level5k, BukofskyBankrollLevel.determine(new CurrencyAmount(5001L)));

        assertEquals(Level5k, BukofskyBankrollLevel.determine(new CurrencyAmount(9999L)));
        assertEquals(Level10k, BukofskyBankrollLevel.determine(new CurrencyAmount(10000L)));
        assertEquals(Level10k, BukofskyBankrollLevel.determine(new CurrencyAmount(10001L)));

        assertEquals(Level10k, BukofskyBankrollLevel.determine(new CurrencyAmount(19999L)));
        assertEquals(Level20k, BukofskyBankrollLevel.determine(new CurrencyAmount(20000L)));
        assertEquals(Level20k, BukofskyBankrollLevel.determine(new CurrencyAmount(20001L)));

        assertEquals(Level20k, BukofskyBankrollLevel.determine(new CurrencyAmount(39999L)));
        assertEquals(Level40k, BukofskyBankrollLevel.determine(new CurrencyAmount(40000L)));
        assertEquals(Level40k, BukofskyBankrollLevel.determine(new CurrencyAmount(40001L)));

        assertEquals(Level40k, BukofskyBankrollLevel.determine(new CurrencyAmount(99999L)));
        assertEquals(Level100k, BukofskyBankrollLevel.determine(new CurrencyAmount(100000L)));
        assertEquals(Level100k, BukofskyBankrollLevel.determine(new CurrencyAmount(100001L)));
    }

    @Test
    public void testGetBet_spotChecks() {
        assertEquals(CurrencyAmount.zero().toString(), LevelZero.getBet(3, true).toString());
        assertEquals(CurrencyAmount.zero().toString(), LevelZero.getBet(3, false).toString());
        assertEquals(CurrencyAmount.zero().toString(), LevelZero.getBet(100, false).toString());

        assertEquals("$8.00", Level2k.getBet(3, true).toString());
        assertEquals("$10.00", Level2k.getBet(3, false).toString());

        assertEquals("$57.00", Level2k.getBet(11, true).toString());
        assertEquals("$55.00", Level2k.getBet(11, false).toString());

        assertEquals("$63.00", Level2k.getBet(12, true).toString());
        assertEquals("$60.00", Level2k.getBet(12, false).toString());

        assertEquals("$63.00", Level2k.getBet(13, true).toString());
        assertEquals("$60.00", Level2k.getBet(13, false).toString());

        assertEquals("$909.00", Level100k.getBet(5, true).toString());
        assertEquals("$1000.00", Level100k.getBet(5, false).toString());
    }
}
