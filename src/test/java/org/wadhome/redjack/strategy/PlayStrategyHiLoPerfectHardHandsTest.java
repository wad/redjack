package org.wadhome.redjack.strategy;

import org.junit.Test;
import org.wadhome.redjack.Value;
import org.wadhome.redjack.rules.BlackjackPlay;
import org.wadhome.redjack.rules.DoubleDownRuleOptions;
import org.wadhome.redjack.rules.TableRulesForTest;

import static org.junit.Assert.assertEquals;

public class PlayStrategyHiLoPerfectHardHandsTest extends HiLoPerfectStrategyTestHelper {

    @Test
    public void testSumsLessThanNine() {
        for (Value upcardValue : Value.values()) {

            // sum of 5
            assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c(upcardValue)));

            // sum of 6
            assertEquals(BlackjackPlay.Hit, compute(c2(), c4(), c(upcardValue)));

            // sum of 7
            assertEquals(BlackjackPlay.Hit, compute(c2(), c5(), c(upcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c2(), c(upcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(c3(), c4(), c(upcardValue)));

            // sum of 8
            assertEquals(BlackjackPlay.Hit, compute(c2(), c6(), c(upcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c3(), c(upcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(c3(), c5(), c(upcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(c3(), c2(), c3(), c(upcardValue)));
        }
    }

    @Test
    public void testSumsOfNine() {
        TableRulesForTest rulesDoubleDownLimited = new TableRulesForTest();
        rulesDoubleDownLimited.setDoubleDownOptions(DoubleDownRuleOptions.TenAndAceOnly);

        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c4(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c2(), c2(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c5(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(0), c4(), c5(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(1), c4(), c5(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(2), c4(), c5(), c(upcardValue))); // deviate
                    break;
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(c2(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(rulesDoubleDownLimited, c2(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c3(), c4(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(c3(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(c4(), c5(), c(upcardValue)));
                    break;
                case Seven:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c5(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(3), c4(), c5(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(4), c4(), c5(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(5), c4(), c5(), c(upcardValue))); // deviate
                    break;
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c5(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfTen() {
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
                    assertEquals(BlackjackPlay.DoubleDown, compute(c2(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(c3(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(c4(), c6(), c(upcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c6(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(3), c4(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(4), c4(), c6(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.DoubleDown, compute(tc(5), c4(), c6(), c(upcardValue))); // deviate
                    break;
            }
        }
    }

    @Test
    public void testSumsOfEleven() {
        // Note: book says to double-down vs ace if the true count >= 1, but basic strategy says to double-down anyway.

        for (Value upcardValue : Value.values()) {
            assertEquals(BlackjackPlay.DoubleDown, compute(c2(), c9(), c(upcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(c3(), c8(), c(upcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(c4(), c7(), c(upcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(c5(), c6(), c(upcardValue)));
        }
    }

    @Test
    public void testSumsOfTwelve() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(3), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tc(4), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tc(5), c5(), c7(), c(upcardValue))); // deviate
                    break;
                case Three:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(1), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tc(2), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tc(3), c5(), c7(), c(upcardValue))); // deviate
                    break;
                case Four:
                    assertEquals(BlackjackPlay.Stand, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(rc(-2), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(rc(-1), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(rc(0), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(rc(1), c5(), c7(), c(upcardValue)));
                    break;
                case Five:
                    assertEquals(BlackjackPlay.Stand, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c8(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(-3), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(tc(-2), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tc(-1), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tc(0), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tc(1), c5(), c7(), c(upcardValue)));
                    break;
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(rc(-2), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(rc(-1), c5(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(rc(0), c5(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(rc(1), c5(), c7(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c2(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c3(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c7(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfThirteen() {
        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Stand, compute(c3(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c6(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(rc(-2), c6(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(rc(-1), c6(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(rc(0), c6(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(rc(1), c6(), c7(), c(upcardValue)));
                    break;
                case Three:
                    assertEquals(BlackjackPlay.Stand, compute(c3(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c6(), c7(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tc(-3), c6(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(tc(-2), c6(), c7(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tc(-1), c6(), c7(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tc(0), c6(), c7(), c(upcardValue)));
                    break;
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(c3(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c4(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c6(), c7(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c3(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c4(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c7(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfFourteen() {
        TableRulesForTest tableRulesCanSurrender = new TableRulesForTest();
        tableRulesCanSurrender.setCanSurrender(true);

        for (Value upcardValue : Value.values()) {
            switch (upcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c5(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c6(), c8(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.Hit, compute(c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c8(), c(upcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                    assertEquals(BlackjackPlay.Hit, compute(c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c8(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, tc(2), c6(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(3), c6(), c8(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(4), c6(), c8(), c(upcardValue))); // deviate
                    break;
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c5(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c8(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfFifteen() {
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
                    assertEquals(BlackjackPlay.Stand, compute(c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c7(), c8(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                    assertEquals(BlackjackPlay.Hit, compute(c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c7(), c8(), c(upcardValue)));
                    break;
                case Nine:
                    assertEquals(BlackjackPlay.Hit, compute(c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c7(), c8(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, tc(1), c7(), c8(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(2), c7(), c8(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(3), c7(), c8(), c(upcardValue))); // deviate
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                    // Note: The book says to surrender if the running count >= 0, but Basic Strategy says to surrender anyway.

                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c7(), c8(), c(upcardValue)));

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, c5(), c4(), c6(), c(upcardValue)));

                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, tc(3), c5(), c4(), c6(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(4), c5(), c4(), c6(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(5), c5(), c4(), c6(), c(upcardValue))); // deviate

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c7(), c8(), c(upcardValue)));
                    break;
                case Ace:
                    // Note: The book says to surrender if the true count >= 1, but Basic Strategy says to surrender anyway.

                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c7(), c8(), c(upcardValue)));

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, c5(), c4(), c6(), c(upcardValue)));

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c5(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c6(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c7(), c8(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfSixteen() {
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
                    assertEquals(BlackjackPlay.Stand, compute(c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c7(), c9(), c(upcardValue)));
                    break;
                case Seven:
                case Eight:
                    assertEquals(BlackjackPlay.Hit, compute(c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(c7(), c9(), c(upcardValue)));
                    break;
                case Nine:
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(4), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(5), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(6), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, tc(4), c7(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(5), c7(), c9(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(6), c7(), c9(), c(upcardValue))); // deviate

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, tc(4), c3(), c4(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(5), c3(), c4(), c9(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, tc(6), c3(), c4(), c9(), c(upcardValue))); // deviate

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, tc(4), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(5), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(6), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, tc(4), c7(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(5), c7(), c9(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, tc(6), c7(), c9(), c(upcardValue))); // deviate
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, rc(-1), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(0), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(1), c6(), cT(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, rc(-1), c7(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(0), c7(), c9(), c(upcardValue))); // deviate
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(1), c7(), c9(), c(upcardValue))); // deviate

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(-1), c2(), c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(0), c2(), c4(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, rc(1), c2(), c4(), cT(), c(upcardValue)));

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(-1), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(0), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(1), c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(-1), c7(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(0), c7(), c9(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, rc(1), c7(), c9(), c(upcardValue)));
                    break;
                case Ace:
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c7(), c9(), c(upcardValue)));

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCanSurrender, c2(), c4(), cT(), c(upcardValue)));

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c6(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(tableRulesCannotSurrender, c7(), c9(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfSeventeen() {
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
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                    assertEquals(BlackjackPlay.Stand, compute(c7(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(c8(), c9(), c(upcardValue)));
                    break;
                case Ace:
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c7(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(tableRulesCanSurrender, c8(), c9(), c(upcardValue)));

                    // handle case where can't surrender due to more than 2 cards in hand
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCanSurrender, c2(), c5(), cT(), c(upcardValue)));

                    // handle conditions where can't surrender due to table rules
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, c7(), cT(), c(upcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(tableRulesCannotSurrender, c8(), c9(), c(upcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfEighteenAndNineteen() {
        for (Value upcardValue : Value.values()) {
            assertEquals(BlackjackPlay.Stand, compute(c8(), cT(), c(upcardValue)));
            assertEquals(BlackjackPlay.Stand, compute(c9(), cT(), c(upcardValue)));
        }
    }
}
