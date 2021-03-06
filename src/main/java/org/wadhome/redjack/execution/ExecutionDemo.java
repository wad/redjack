package org.wadhome.redjack.execution;

import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.bet.BettingStrategyMaxOnGoodCount;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyBasic;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

class ExecutionDemo extends Execution {

    public static void main(String... args) {
        new ExecutionDemo().execute();
    }

    @Override
    Command getCommand() {
        return Command.demo;
    }

    @Override
    Casino execute() {
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(10L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(2000L);
        System.out.println("Three players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing one shoe.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(5, 100);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                true,
                true);
        Table table = casino.createTable(1, tableRules);

        List<Player> players = new ArrayList<>();
        Player alice = new Player(
                "Alice Adventurer",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyMaxOnGoodCount()),
                playerFavoriteBet);
        alice.setNotes("Alice blatantly uses the hi-low count method."
                + " She bets minimums, except when the count goes to 3, when she bets maximum."
                + " She's going to get caught.");
        players.add(alice);

        Player brandon = new Player(
                "Brandon Basic",
                Gender.male,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite()),
                playerFavoriteBet);
        brandon.setNotes("Brandon plays perfect Basic Strategy, doesn't count cards,"
                + " and he always bets his favorite bet.");
        players.add(brandon);

        Player callie = new Player(
                "Callie Careful",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                playerFavoriteBet);
        callie.setNotes("Callie read Bukofsky's book, counts cards using the hi-low method,"
                + " and is playing with 5% risk of ruin, at the $2000 bankroll level, betting accordingly."
                + "\nShe is careful to not be too obvious, avoids certain plays,"
                + " and won't flatline the table minimum bet.");
        players.add(callie);

        assignPlayersToTable(players, table);

        table.playRoundsUntilEndOfShoe();

        return casino;
    }
}
