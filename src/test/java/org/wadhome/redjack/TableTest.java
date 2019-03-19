package org.wadhome.redjack;

import org.junit.Before;
import org.junit.Test;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesDefault;
import org.wadhome.redjack.strategy.PlayStrategyBasic;

import static org.junit.Assert.assertEquals;

public class TableTest extends TestHelper {

    private TableRules tableRules;
    private Table table;
    private Shoe shoe;
    private Player player;

    @Before
    public void setup() {
        Casino casino = new Casino();
        tableRules = new TableRulesDefault();
        table = new Table(casino, 0, tableRules);
        shoe = table.getShoe();
        shoe.dumpAllCards();
        player = new Player(
                "Abe",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(10L));
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
        tableRules.setCanSurrender(true);
        shoe.addCardToBottom(cT(), cT(), c6(), cT());
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
    public void testDealerBlackjackAceFirstWithNoInsurance() {
        player.setTakesMaxInsurance(false);
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
        shoe.addCardToBottom(c8(), cT(), c8(), cT(), c8(), c9(), cT(), c9());
        table.playRound();
        assertEquals("$70.00", player.getBankroll().toString());
    }

    // todo: test split tens (not using pure basic strategy), and get a blackjack

    @Test
    public void testSplitAcesNoHittingAllowed() {
        tableRules.setCanHitSplitAces(false);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cA(), c8());
        table.playRound();
        assertEquals("$80.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesFirstLosesSecondIsBlackjack() {
        tableRules.setCanHitSplitAces(false);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), c8(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesFirstIsBlackjackSecondLoses() {
        tableRules.setCanHitSplitAces(false);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cT(), c8());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesNoHittingAllowedTwoBlackjacks() {
        tableRules.setCanHitSplitAces(false);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cT(), cT());
        table.playRound();
        assertEquals("$120.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesHittingAllowed() {
        tableRules.setCanHitSplitAces(true);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), c2(), c9(), c3(), c2(), cT());
        table.playRound();
        assertEquals("$90.00", player.getBankroll().toString());
    }

    @Test
    public void testSplitAcesHittingAllowedTwoSplits() {
        // todo: hands processed in the wrong order. A,9 hand is processed last, should be next-to-last.
        tableRules.setCanHitSplitAces(true);
        shoe.addCardToBottom(cA(), cT(), cA(), cT(), cA(), c6(), c2(), c9(), c8(), cT());
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }
}
