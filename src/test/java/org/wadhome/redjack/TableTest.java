package org.wadhome.redjack;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest extends TestHelper {

    private TableRules tableRules;
    private Table table;
    private Shoe shoe;
    private Player player;

    @Before
    public void setup() {
        Casino casino = new Casino("Test");
        tableRules = TableRules.getDefaultRules();
        table = new Table(casino, 0, tableRules);
        shoe = table.getShoe();
        shoe.dumpAllCards();
        player = new Player(
                "Abe",
                Gender.male,
                new MoneyPile(10000L),
                PlayerStrategy.BasicStrategy);
        player.setFavoriteBet(new MoneyPile(1000L));
        SeatNumber seatNumber = SeatNumber.one;
        table.assignPlayerToSeat(seatNumber, player);
    }

    @Test
    public void testPush() {
        shoe.addCardToBottom(cT(), cT(), cT(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testDoubleDownWin() {
        shoe.addCardToBottom(c5(), cT(), c6(), cT(), cT());
        table.playRound();
        assertEquals("$120.00", player.getBankroll().toString());
    }

    @Test
    public void testDoubleDownLoss() {
        shoe.addCardToBottom(c5(), cT(), c6(), cT(), c2());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    @Test
    public void testPlayerBust() {
        shoe.addCardToBottom(cT(), cT(), c3(), cT(), cT());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testSurrender() {
        tableRules.canSurrender = true;
        shoe.addCardToBottom(cT(), cT(), c6(), cT(), cT());
        table.playRound();
        assertEquals("$95.00", player.getBankroll().toString());
    }

    @Test
    public void testPlayerBlackjack() {
        shoe.addCardToBottom(cA(), cT(), cT(), cT());
        table.playRound();
        assertEquals("$115.00", player.getBankroll().toString());
    }

    @Test
    public void testDealerBlackjackAceFirst() {
        player.setTakesMaxInsurance(false);
        shoe.addCardToBottom(cT(), cA(), cT(), cT());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testDealerBlackjackAceFirstWithInsurance() {
        player.setTakesMaxInsurance(true);
        shoe.addCardToBottom(cT(), cA(), cT(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testDealerBlackjackTenFirst() {
        shoe.addCardToBottom(cT(), cT(), cT(), cA());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testBlackjackPush() {
        shoe.addCardToBottom(cA(), cT(), cT(), cA());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testDealerBust() {
        shoe.addCardToBottom(cT(), c3(), cT(), cT(), cT());
        table.playRound();
        assertEquals("$110.00", player.getBankroll().toString());
    }

    @Test
    public void testWinByPoints() {
        shoe.addCardToBottom(cT(), c7(), cT(), cT());
        table.playRound();
        assertEquals("$110.00", player.getBankroll().toString());
    }

    @Test
    public void testLoseToDealer() {
        shoe.addCardToBottom(cT(), cT(), c9(), cT());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testLoseToDealerBlackjack() {
        shoe.addCardToBottom(cT(), cT(), c9(), cA());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testCharlie() {
        shoe.addCardToBottom(c2(), cT(), c2(), cT(), c2(), c2(), c2(), c2(), c2());
        table.playRound();
        assertEquals("$120.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitLoseBoth() {
        shoe.addCardToBottom(c8(), cT(), c8(), cT(), c9(), c9());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitWinBoth() {
        shoe.addCardToBottom(c8(), cT(), c8(), c7(), cT(), cT());
        table.playRound();
        assertEquals("$120.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitWinLose() {
        shoe.addCardToBottom(c8(), cT(), c8(), c7(), cT(), c4(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitWinPush() {
        shoe.addCardToBottom(c8(), cT(), c8(), c7(), cT(), c9());
        table.playRound();
        assertEquals("$110.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitLoseWin() {
        shoe.addCardToBottom(c8(), cT(), c8(), c7(), c4(), cT(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitWithTwoSplits() {
        // dealer hand: T,T (20)
        // split hand 0: 8,5 -> hit and get 8 (21 stand) -> win
        // split hand 1: 8,T -> stand at 18 -> lose
        // split hand 2: 8,9 -> stand at 17 -> lose
        shoe.addCardToBottom(c8(), cT(), c8(), cT(), c8(), c9(), c7(), cT(), c8());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    // todo: test split tens (not using pure basic strategy), and get a blackjack

    @Test
    public void testSplitAcesNoHittingAllowed() {
        tableRules.canHitSplitAces = false;
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cA(), c9());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesFirstLosesSecondIsBlackjack() {
        tableRules.canHitSplitAces = false;
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), c8(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesFirstIsBlackjackSecondLoses() {
        tableRules.canHitSplitAces = false;
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cT(), c8());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesNoHittingAllowedTwoBlackjacks() {
        tableRules.canHitSplitAces = false;
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cT(), cT());
        table.playRound();
        assertEquals("$120.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesHittingAllowed() {
        tableRules.canHitSplitAces = true;
        //
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cA(), c9(), c5(), cT());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesHittingAllowedTwoSplits() {
        tableRules.canHitSplitAces = true;
        //
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cA(), c9(), c5(), cT());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }
}
