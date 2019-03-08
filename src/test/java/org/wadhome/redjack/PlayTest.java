package org.wadhome.redjack;

import org.junit.Test;

public class PlayTest {
    @Test
    public void testRunningIt() {
        int tableNumber = 1;
        Casino casino = new Casino("TestMe");
        casino.createTable(tableNumber, TableRules.getDefaultRules());
        Table table = casino.getTable(tableNumber);
        table.prepareForPlay();

        Player alex = new Player(
                "Alex",
                PlayerGender.male,
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToSeat(SeatNumber.one, alex);
        table.assignPlayerToSeat(SeatNumber.two, alex);

        Player becky = new Player(
                "Becky",
                PlayerGender.female,
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToSeat(SeatNumber.three, becky);

        Player charles = new Player(
                "Charles",
                PlayerGender.male,
                new MoneyPile(10000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToSeat(SeatNumber.five, charles);

        table.createHandAndPlaceBet(SeatNumber.one, new MoneyPile(1000L));
        table.createHandAndPlaceBet(SeatNumber.two, new MoneyPile(2500L));
        table.createHandAndPlaceBet(SeatNumber.three, new MoneyPile(1000L));
        table.createHandAndPlaceBet(SeatNumber.five, new MoneyPile(10000L));

        table.playRound();
    }
}
