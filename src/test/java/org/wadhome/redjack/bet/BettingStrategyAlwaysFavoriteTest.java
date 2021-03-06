package org.wadhome.redjack.bet;

import org.junit.Before;
import org.junit.Test;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRulesForTest;
import org.wadhome.redjack.strategy.PlayStrategyBasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BettingStrategyAlwaysFavoriteTest {
    private Casino casino;
    private Table table;
    private Player player;

    @Before
    public void setup() {
        casino = new Casino();
        table = casino.createTable(0, new TableRulesForTest());
        player = new Player(
                "test",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(5000L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()), // unused
                new CurrencyAmount(66L));
    }

    @Test
    public void testGetBet() {
        BettingStrategyAlwaysFavorite bettingStrategyAlwaysFavorite = new BettingStrategyAlwaysFavorite();
        BetRequest betRequest = new BetRequest(casino, table, player);
        bettingStrategyAlwaysFavorite.getBet(betRequest);
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$66.00", betRequest.getActualBetAmount().toString());
    }
}
