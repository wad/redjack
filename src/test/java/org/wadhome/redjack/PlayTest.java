package org.wadhome.redjack;

import org.junit.Assert;
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
        alex.setFavoriteBet(new MoneyPile(2500));

        Player becky = new Player(
                "Becky",
                PlayerGender.female,
                new MoneyPile(50000L),
                PlayerSmarts.BasicStrategy);
        table.assignPlayerToSeat(SeatNumber.three, becky);
        becky.setFavoriteBet(new MoneyPile(1000L));

        Player charles = new Player(
                "Charles",
                PlayerGender.male,
                new MoneyPile(10000L),
                PlayerSmarts.BasicStrategy);
        charles.setFavoriteBet(new MoneyPile(10000L));
        table.assignPlayerToSeat(SeatNumber.five, charles);

        MoneyPile initialCasinoBankroll = casino.getBankroll().copy();
        MoneyPile initialSumOfPlayerBankrolls = new MoneyPile(
                alex.getBankroll(),
                becky.getBankroll(),
                charles.getBankroll());
        MoneyPile initialMoney = new MoneyPile(initialCasinoBankroll, initialSumOfPlayerBankrolls);

        table.playRound();
        table.playRound();
        table.playRound();

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
    }
}
