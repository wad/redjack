package org.wadhome.redjack;

import org.junit.Assert;
import org.junit.Test;

public class PlayTest {

    @Test
    public void testRunningIt() {
        int tableNumber = 1;
        Casino casino = new Casino("TestMe", null, new Display(false));
        TableRules tableRules = TableRules.getDefaultRules();
        casino.createTable(tableNumber, tableRules);
        Table table = casino.getTable(tableNumber);
        table.shuffleAndStuff();

        Player alex = new Player(
                "Alex",
                Gender.male,
                new MoneyPile(50000L),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(2500L));
        table.assignPlayerToSeat(SeatNumber.one, alex);
        table.assignPlayerToSeat(SeatNumber.two, alex);

        Player becky = new Player(
                "Becky",
                Gender.female,
                new MoneyPile(50000L),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(1000L));
        table.assignPlayerToSeat(SeatNumber.three, becky);

        Player charles = new Player(
                "Charles",
                Gender.male,
                new MoneyPile(10000L),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new MoneyPile(10000L));
        table.assignPlayerToSeat(SeatNumber.five, charles);

        MoneyPile initialCasinoBankroll = casino.getBankroll().copy();
        MoneyPile initialSumOfPlayerBankrolls = new MoneyPile(
                alex.getBankroll(),
                becky.getBankroll(),
                charles.getBankroll());
        MoneyPile initialMoney = new MoneyPile(initialCasinoBankroll, initialSumOfPlayerBankrolls);

        table.playRounds(3);

        MoneyPile finalCasinoBankroll = casino.getBankroll().copy();
        MoneyPile finalSumOfPlayerBankrolls = new MoneyPile(
                alex.getBankroll(),
                becky.getBankroll(),
                charles.getBankroll());
        MoneyPile finalMoney = new MoneyPile(finalCasinoBankroll, finalSumOfPlayerBankrolls);

        System.out.println("Initial casino bankroll: " + initialCasinoBankroll);
        System.out.println("Final casino bankroll: " + finalCasinoBankroll);

        System.out.println("Initial player bankrolls: " + initialSumOfPlayerBankrolls);
        System.out.println("Final player bankrolls: " + finalSumOfPlayerBankrolls);

        Assert.assertEquals(initialMoney, finalMoney);

        table.removePlayerFromSeat(SeatNumber.one);
        table.removePlayerFromSeat(SeatNumber.two);
        table.removePlayerFromSeat(SeatNumber.three);
        table.removePlayerFromSeat(SeatNumber.five);
    }
}
