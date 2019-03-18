package org.wadhome.redjack.bet;

import org.junit.Before;
import org.junit.Test;
import org.wadhome.redjack.Casino;
import org.wadhome.redjack.Gender;
import org.wadhome.redjack.Player;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyBasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BettingStrategyBukofskyTest {
    private Casino casino;
    private Table table;
    private Player player;

    @Before
    public void setup() {
        casino = new Casino();
        table = casino.createTable(0, TableRules.getDefaultRules());
        player = new Player(
                "test",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(5000L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()), // unused
                new CurrencyAmount(20L));
    }

    @Test
    public void testGetBet_perfect() {
        BettingStrategyBukofsky bettingStrategyBukofsky = new BettingStrategyBukofsky(true);
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setTrueCount(2);
        bettingStrategyBukofsky.getBet(betRequest);
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$10.00", betRequest.getActualBetAmount().toString());
    }

    // todo: lots more tests here
}
