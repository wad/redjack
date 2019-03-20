package org.wadhome.redjack.casino;

import org.junit.Test;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesForTest;
import org.wadhome.redjack.strategy.PlayStrategyBasic;

import static org.junit.Assert.assertEquals;

public class TablePlayTest {

    @Test
    public void testRunningBankrollBalance() {
        int tableNumber = 1;
        Casino casino = new Casino();
        TableRules tableRules = new TableRulesForTest();
        casino.createTable(tableNumber, tableRules);
        Table table = casino.getTable(tableNumber);
        table.shuffleAndStuff();

        Player alex = new Player(
                "Alex",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(500L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(25L));
        table.assignPlayerToSeat(SeatNumber.one, alex);
        table.assignPlayerToSeat(SeatNumber.two, alex);

        Player becky = new Player(
                "Becky",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(500L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(10L));
        table.assignPlayerToSeat(SeatNumber.three, becky);

        Player charles = new Player(
                "Charles",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(new CurrencyAmount(500L)),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                new CurrencyAmount(100L));
        table.assignPlayerToSeat(SeatNumber.five, charles);
        table.setMuteDisplay();

        CurrencyAmount initialCasinoBankroll = casino.getBankroll().getCurrencyAmountCopy();

        CurrencyAmount initialSumOfPlayerBankrolls = CurrencyAmount.zero();
        initialCasinoBankroll.increaseBy(alex.getBankroll().getCurrencyAmountCopy());
        initialCasinoBankroll.increaseBy(becky.getBankroll().getCurrencyAmountCopy());
        initialCasinoBankroll.increaseBy(charles.getBankroll().getCurrencyAmountCopy());

        CurrencyAmount initialMoney = CurrencyAmount.zero();
        initialMoney.increaseBy(initialCasinoBankroll);
        initialMoney.increaseBy(initialSumOfPlayerBankrolls);

        table.playRounds(3);

        CurrencyAmount finalCasinoBankroll = casino.getBankroll().getCurrencyAmountCopy();
        CurrencyAmount finalSumOfPlayerBankrolls = CurrencyAmount.zero();
        finalSumOfPlayerBankrolls.increaseBy(alex.getBankroll().getCurrencyAmountCopy());
        finalSumOfPlayerBankrolls.increaseBy(becky.getBankroll().getCurrencyAmountCopy());
        finalSumOfPlayerBankrolls.increaseBy(charles.getBankroll().getCurrencyAmountCopy());

        CurrencyAmount finalMoney = CurrencyAmount.zero();
        finalMoney.increaseBy(finalCasinoBankroll);
        finalMoney.increaseBy(finalSumOfPlayerBankrolls);

        assertEquals(initialMoney.toString(), finalMoney.toString());

        table.removePlayerFromSeat(SeatNumber.one);
        table.removePlayerFromSeat(SeatNumber.two);
        table.removePlayerFromSeat(SeatNumber.three);
        table.removePlayerFromSeat(SeatNumber.five);
    }
}
