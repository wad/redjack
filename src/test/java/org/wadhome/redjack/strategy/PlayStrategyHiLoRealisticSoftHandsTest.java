package org.wadhome.redjack.strategy;

import org.junit.Test;
import org.wadhome.redjack.Value;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.DoubleDownRuleOptions;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class PlayStrategyHiLoRealisticSoftHandsTest extends HiLoRealisticStrategyTestHelper {

    @Test
    public void testAceWithTwoOrThree() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), c(upcardValue)));
                    break;
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c3(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testAceWithFourOrFive() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c4(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), cA(), c(upcardValue)));
                    break;
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c4(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c5(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), cA(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c4(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), cA(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testAceWithSix() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c4(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c4(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c4(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testAceWithSeven() {
        TableRulesForTest tableRulesDoubleDownLimited = new TableRulesForTest();
        tableRulesDoubleDownLimited.setDoubleDownOptions(DoubleDownRuleOptions.TenAndAceOnly);

        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, cA(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c5(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c5(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c3(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c6(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c5(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), c2(), cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(cA(), c3(), cA(), cA(), cA(), cA(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testAceWithEight() {
        TableRulesForTest tableRulesDoubleDownLimited = new TableRulesForTest();
        tableRulesDoubleDownLimited.setDoubleDownOptions(DoubleDownRuleOptions.TenAndAceOnly);

        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), cA(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Stand, compute(tc(0), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(1), cA(), c8(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(2), cA(), c8(), c(upcardValue))); // deviate

                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(0), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(1), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(2), cA(), c8(), c(upcardValue)));
                    break;
                case Three:
                case Four:
                case Five:
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), cA(), c(upcardValue)));
                    break;
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), c2(), c(upcardValue)));
                    break;
                case Seven:
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), cA(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Stand, compute(tc(3), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(4), cA(), c8(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(5), cA(), c8(), c(upcardValue))); // deviate

                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(3), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(4), cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesDoubleDownLimited, tc(5), cA(), c8(), c(upcardValue)));
                    break;
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(cA(), c6(), cA(), cA(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testAceWithNine() {
        TableRulesForTest tableRulesDoubleDownLimited = new TableRulesForTest();
        tableRulesDoubleDownLimited.setDoubleDownOptions(DoubleDownRuleOptions.TenAndAceOnly);

        for (Value upcardValue : Value.values()) {
            assertEquals(BlackjackPlay.Stand, compute(cA(), c9(), c(upcardValue)));
            assertEquals(BlackjackPlay.Stand, compute(cA(), c8(), cA(), c(upcardValue)));
            assertEquals(BlackjackPlay.Stand, compute(cA(), c7(), cA(), cA(), c(upcardValue)));
        }
    }

    @Test
    public void testSingle() {
        assertEquals(BlackjackPlay.Hit, compute(cA(), c2(), c(Value.Four)));
    }
}
