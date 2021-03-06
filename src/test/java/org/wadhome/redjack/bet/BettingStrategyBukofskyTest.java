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

public class BettingStrategyBukofskyTest {
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
                new CurrencyAmount(20L));
    }

    @Test
    public void testGetBet() {
        BettingStrategyBukofsky bettingStrategyBukofsky = new BettingStrategyBukofsky(true);
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setTrueCount(2);
        bettingStrategyBukofsky.getBet(betRequest);
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$10.00", betRequest.getActualBetAmount().toString());
    }

    @Test
    public void testGetBet_level2kPerfect() {
        player.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(BukofskyBankrollLevel.Level2k);
        BettingStrategyBukofsky bettingStrategyBukofsky = new BettingStrategyBukofsky(true);
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setTrueCount(6);
        bettingStrategyBukofsky.getBet(betRequest);
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$26.00", betRequest.getActualBetAmount().toString());
    }

    @Test
    public void testGetBet_level2kNotPerfect() {
        player.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(BukofskyBankrollLevel.Level2k);
        BettingStrategyBukofsky bettingStrategyBukofsky = new BettingStrategyBukofsky(false);
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setTrueCount(6);
        bettingStrategyBukofsky.getBet(betRequest);
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$25.00", betRequest.getActualBetAmount().toString());
    }
}
