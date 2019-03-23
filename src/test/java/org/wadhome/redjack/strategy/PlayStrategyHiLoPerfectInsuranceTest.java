package org.wadhome.redjack.strategy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayStrategyHiLoPerfectInsuranceTest extends HiLoPerfectStrategyTestHelper {

    @Test
    public void testShouldGetInsurance() {
        assertEquals("$0.00", computeInsuranceBet(-1).toString());
        assertEquals("$0.00", computeInsuranceBet(0).toString());
        assertEquals("$0.00", computeInsuranceBet(1).toString());
        assertEquals("$0.00", computeInsuranceBet(2).toString());
        assertEquals("$15.00", computeInsuranceBet(3).toString());
        assertEquals("$15.00", computeInsuranceBet(4).toString());
    }
}
