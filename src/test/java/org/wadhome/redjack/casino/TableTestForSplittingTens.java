package org.wadhome.redjack.casino;

import org.junit.Before;
import org.junit.Test;
import org.wadhome.redjack.TestHelper;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.cardcount.CardCountStatusRunningAndTrue;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRulesForTest;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import static org.junit.Assert.assertEquals;

public class TableTestForSplittingTens extends TestHelper {

    private Table table;
    private Shoe shoe;
    private Player player;

    @Before
    public void setup() {
        Casino casino = new Casino();
        TableRulesForTest tableRules = new TableRulesForTest();
        table = casino.createTable(0, tableRules);
        shoe = table.getShoe();
        shoe.dumpAllCards();
        player = new Player(
                "Abe",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L)),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(30L));
        SeatNumber seatNumber = SeatNumber.one;
        table.assignPlayerToSeat(seatNumber, player);
    }

    // todo: blackjack on split ace shouldn't result in 1.5x winnings
    @Test
    public void testSplitTensOneBlackjackOneBust() {
        table.getCasino().getOutput().enableDisplayForTest();
        shoe.addCardToBottom(cT(), c6(), cT(), cT(), c2(), cA(), c2(), c2());
        CardCountStatusRunningAndTrue count = new CardCountStatusRunningAndTrue(0, 10);
        player.getPlayStrategy().getCardCountMethod().temporarilyOverrideCardCountStatus(count);
        table.playRound();
        assertEquals("$100.00", player.getBankroll().toString());
    }
}
