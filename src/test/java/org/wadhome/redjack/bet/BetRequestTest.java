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

import static org.junit.Assert.*;

public class BetRequestTest {

    Casino casino;
    Table table;
    Player player;

    @Before
    public void setup() {
        casino = new Casino("test", 1L, false, false);
        table = casino.createTable(0, TableRules.getDefaultRules());
        player = new Player(
                "test",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(100L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(20L));
    }

    @Test
    public void testConstruction() {
        BetRequest betRequest = new BetRequest(casino, table, player);
        assertEquals("$20.00", betRequest.getDesiredBet().toString());
        assertEquals("$10.00", betRequest.getMinPossibleBet().toString());
        assertEquals("$100.00", betRequest.getMaxPossibleBet().toString());
        assertTrue(betRequest.canPlaceBet());
        try {
            assertNull(betRequest.getTrueCount());
            fail("Should have thrown.");
        } catch (IllegalStateException ignored) {
            // yay
        }
    }

    @Test
    public void testConstructionCannotAfford() {
        // Remove $99 from the player's bankroll, so she only have $1
        player.getBankroll().moveMoneyToTargetPile(
                MoneyPile.extractMoneyFromFederalReserve(CurrencyAmount.zero()),
                new CurrencyAmount(99L));
        BetRequest betRequest = new BetRequest(casino, table, player);
        assertFalse(betRequest.canPlaceBet());
    }

    @Test
    public void testConstructionAdjusted() {
        player.setFavoriteBet(new CurrencyAmount(2000L));
        BetRequest betRequest = new BetRequest(casino, table, player);
        assertEquals("$100.00", betRequest.getDesiredBet().toString());
        assertEquals("$10.00", betRequest.getMinPossibleBet().toString());
        assertEquals("$100.00", betRequest.getMaxPossibleBet().toString());
        assertTrue(betRequest.canPlaceBet());
    }

    @Test
    public void testSetConstrainedActualBetAmount_tooLow() {
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setConstrainedActualBetAmount(new CurrencyAmount(1L));
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$10.00", betRequest.getActualBetAmount().toString());
    }

    @Test
    public void testSetConstrainedActualBetAmount_tooHigh() {
        BetRequest betRequest = new BetRequest(casino, table, player);
        betRequest.setConstrainedActualBetAmount(new CurrencyAmount(2000L));
        assertTrue(betRequest.canPlaceBet());
        assertEquals("$100.00", betRequest.getActualBetAmount().toString());
    }
}
