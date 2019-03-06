package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicStrategySplitHandsTest extends BasicStrategyTestHelper {

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
        TableRules tableRulesCannotSurrender = new TableRules();
        tableRulesCannotSurrender.canSurrender = false;

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
                    assertEquals(BlackjackPlay.Surrender, compute(c7(), c7(), c(upcardValue)));
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
            assertEquals(BlackjackPlay.Stand, compute(cT(), cT(), c(upcardValue)));
        }
    }

    @Test
    public void testPairOfAces() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                    assertEquals(BlackjackPlay.Split, compute(cA(), cA(), c(upcardValue)));
                    // todo: hit if can't split (splits all used up)
                    break;
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Split, compute(cA(), cA(), c(upcardValue)));
                    // todo: hit if can't split (splits all used up)
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Split, compute(cA(), cA(), c(upcardValue)));
                    // todo: hit if can't split (splits all used up)
                    break;
            }
        }
    }
}
