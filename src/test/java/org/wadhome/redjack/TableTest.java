package org.wadhome.redjack;

import org.junit.Test;

public class TableTest extends TestHelper {
    @Test
    public void testSimple() {
        int tableNumber = 1;
        Casino casino = new Casino("TestTable");
        casino.createTable(tableNumber, TableRules.getHomeCasinoRules());
        Table table = casino.getTable(tableNumber);
        table.prepareForPlay();

        Player alex = new Player(
                "Alex",
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToHand(1, alex);

        table.placeBet(1, new MoneyPile(1000L));

        table.playRound();
    }
}
