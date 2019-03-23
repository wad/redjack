package org.wadhome.redjack.strategy;

import org.junit.Test;
import org.wadhome.redjack.casino.Value;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class PlayStrategyHiLoPerfectSplitHandsTest extends HiLoPerfectStrategyTestHelper {

    @Test
    public void testPairOfTwosOrThrees() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                case Seven:
                    assertEquals(BlackjackPlay.Split, compute(c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Split, compute(c3(), c3(), c(upcardValue)));
                    break;
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c3(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfFours() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c4(), c(upcardValue)));
                    break;
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Split, compute(c4(), c4(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c4(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfFives() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                case Seven:
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.DoubleDown, compute(c5(), c5(), c(upcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c5(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfSixes() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Split, compute(c6(), c6(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c6(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfSevens() {
        TableRulesForTest tableRulesCannotSurrender = new TableRulesForTest();
        tableRulesCannotSurrender.setCanSurrender(false);

        TableRulesForTest tableRulesCanSurrender = new TableRulesForTest();
        tableRulesCanSurrender.setCanSurrender(true);

        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                case Seven:
                    assertEquals(BlackjackPlay.Split, compute(c7(), c7(), c(upcardValue)));
                    break;
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.Hit, compute(c7(), c7(), c(upcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c7(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c7(), c7(), c(upcardValue)));
                    break;
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c7(), c7(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfEights() {
        for (Value upcardValue : Value.values()) {
            assertEquals(BlackjackPlay.Split, compute(c8(), c8(), c(upcardValue)));
        }
    }

    @Test
    public void testPairOfNines() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Split, compute(c9(), c9(), c(upcardValue)));
                    break;
                case Seven:
                    assertEquals(BlackjackPlay.Stand, compute(c9(), c9(), c(upcardValue)));
                    break;
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.Split, compute(c9(), c9(), c(upcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Stand, compute(c9(), c9(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfTens() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                    assertEquals(BlackjackPlay.Stand, compute(cT(), cT(), c(upcardValue)));
                    break;
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(cT(), cT(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Stand, compute(tc(4), cT(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Split, compute(tc(5), cT(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Split, compute(tc(6), cT(), cT(), c(upcardValue))); // deviate
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Stand, compute(cT(), cT(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testPairOfAces() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Split, compute(cA(), cA(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Split, computeCannotSplit(cA(), cA(), c(upcardValue)));
                    break;
            }
        }
    }
}
