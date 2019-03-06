package org.wadhome.redjack;

import org.junit.Assert;
import org.junit.Test;

public class TestMoneyPile {
    @Test
    public void testIt() {
        Assert.assertEquals("$123.45", new MoneyPile(12345L).toString());
        Assert.assertEquals("$120.00", new MoneyPile(12000L).toString());
    }
}
