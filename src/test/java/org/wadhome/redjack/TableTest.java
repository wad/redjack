package org.wadhome.redjack;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest extends TestHelper {

    private TableRules tableRules = TableRules.getDefaultRules();
    private Table table;
    private Shoe shoe;
    private Player player;

    @Before
    public void setup() {
        table = new Table(0, tableRules);
        shoe = table.getShoe();
        shoe.dumpAllCards();
        player = new Player(
                "Abe",
                PlayerGender.male,
                new MoneyPile(10000L),
                PlayerSmarts.BasicStrategy);
        int handNumber = 0;
        table.assignPlayerToHand(handNumber, player);
        table.placeBet(handNumber, new MoneyPile(1000L));
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
        shoe.addCardToBottom(cT(), cA(), cT(), cT());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
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
}
