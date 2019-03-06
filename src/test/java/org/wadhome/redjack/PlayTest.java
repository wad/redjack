package org.wadhome.redjack;

import org.junit.Test;

public class PlayTest {
    @Test
    public void testRunningIt() {
        int tableNumber = 1;
        Casino casino = new Casino("TestMe");
        casino.createTable(tableNumber, TableRules.getHomeCasinoRules());
        Table table = casino.getTable(tableNumber);
        table.prepareForPlay();

        Player alex = new Player(
                "Alex",
                PlayerGender.male,
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToHand(1, alex);
        table.assignPlayerToHand(2, alex);

        Player becky = new Player(
                "Becky",
                PlayerGender.female,
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToHand(3, becky);

        Player charles = new Player(
                "Charles",
                PlayerGender.male,
                new MoneyPile(10000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToHand(5, charles);

        table.placeBet(1, new MoneyPile(1000L));
        table.placeBet(2, new MoneyPile(2500L));
        table.placeBet(3, new MoneyPile(1000L));
        table.placeBet(5, new MoneyPile(10000L));

        table.playRound();
    }
}
