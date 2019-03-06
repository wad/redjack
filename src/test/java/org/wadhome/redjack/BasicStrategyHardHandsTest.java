package org.wadhome.redjack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicStrategyHardHandsTest extends BasicStrategyTestHelper {

    @Test
    public void testSumsLessThanNine() {
        for (Value dealerUpcardValue : Value.values()) {

            // sum of 5
            assertEquals(BlackjackPlay.Hit, compute(
                    c2(),
                    c3(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));

            // sum of 6
            assertEquals(BlackjackPlay.Hit, compute(
                    c2(),
                    c4(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));

            // sum of 7
            assertEquals(BlackjackPlay.Hit, compute(
                    c2(),
                    c5(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(
                    c3(),
                    c4(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));

            // sum of 8
            assertEquals(BlackjackPlay.Hit, compute(
                    c2(),
                    c6(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.Hit, compute(
                    c3(),
                    c5(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
        }
    }

    @Test
    public void testSumsOfNine() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c2(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            c6(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c5(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c2(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c3(),
                            c6(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c4(),
                            c5(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c2(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            c6(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c5(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfTen() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                case Seven:
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c2(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c3(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.DoubleDown, compute(
                            c4(),
                            c6(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c2(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c6(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfEleven() {
        for (Value dealerUpcardValue : Value.values()) {
            assertEquals(BlackjackPlay.DoubleDown, compute(
                    c2(),
                    c9(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(
                    c3(),
                    c8(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(
                    c4(),
                    c7(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.DoubleDown, compute(
                    c5(),
                    c6(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
        }
    }

    @Test
    public void testSumsOfTwelve() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                case Three:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c2(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c5(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(
                            c2(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c3(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c4(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c5(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c2(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c5(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfThirteenAndFourteen() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    // sum of 13
                    assertEquals(BlackjackPlay.Stand, compute(
                            c3(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c4(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c5(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c6(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));

                    // sum of 14
                    assertEquals(BlackjackPlay.Stand, compute(
                            c4(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c5(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c6(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    // sum of 13
                    assertEquals(BlackjackPlay.Hit, compute(
                            c3(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c5(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c6(),
                            c7(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));

                    // sum of 14
                    assertEquals(BlackjackPlay.Hit, compute(
                            c4(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c5(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c6(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
            }
        }
    }

    @Test
    public void testSumsOfFifteen() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(
                            c5(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c6(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c7(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Seven:
                case Eight:
                case Nine:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c5(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c6(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c7(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c5(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c6(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c7(),
                            c8(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    // todo: handle conditions where can't surrender
                    break;
            }
        }
    }

    @Test
    public void testSumsOfSixteen() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
                case Two:
                case Three:
                case Four:
                case Five:
                case Six:
                    assertEquals(BlackjackPlay.Stand, compute(
                            c6(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c7(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Seven:
                case Eight:
                    assertEquals(BlackjackPlay.Hit, compute(
                            c6(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Hit, compute(
                            c7(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Nine:
                case Ten:
                case Jack:
                case Queen:
                case King:
                case Ace:
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c6(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c7(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    // todo: handle conditions where can't surrender
                    break;
            }
        }
    }

    @Test
    public void testSumsOfSeventeen() {
        for (Value dealerUpcardValue : Value.values()) {
            switch (dealerUpcardValue) {
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
                    assertEquals(BlackjackPlay.Stand, compute(
                            c7(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Stand, compute(
                            c8(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    break;
                case Ace:
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c7(),
                            cT(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    assertEquals(BlackjackPlay.Surrender, compute(
                            c8(),
                            c9(),
                            new Card(0, Suite.Clubs, dealerUpcardValue)));
                    // todo: handle conditions where can't surrender
                    break;
            }
        }
    }

    @Test
    public void testSumsOfEighteenAndNineteen() {
        for (Value dealerUpcardValue : Value.values()) {
            assertEquals(BlackjackPlay.Stand, compute(
                    c8(),
                    cT(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
            assertEquals(BlackjackPlay.Stand, compute(
                    c9(),
                    cT(),
                    new Card(0, Suite.Clubs, dealerUpcardValue)));
        }
    }

    @Test
    public void testSingle() {
        assertEquals(BlackjackPlay.Hit, compute(
                c2(),
                cT(),
                new Card(0, Suite.Clubs, Value.Seven)));
    }
}
