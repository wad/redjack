package org.wadhome.redjack.execution;

import org.wadhome.redjack.Output;
import org.wadhome.redjack.bet.BettingStrategyAlwaysFavorite;
import org.wadhome.redjack.bet.BukofskyBankrollLevel;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategy;
import org.wadhome.redjack.strategy.PlayStrategyBasic;
import org.wadhome.redjack.strategy.PlayStrategyUstonApsPerfect;

import java.util.ArrayList;
import java.util.List;

class ExecutionUstonApsCounterPerfect extends Execution {

    public static void main(String... args) {
        new ExecutionUstonApsCounterPerfect().execute();
    }

    @Override
    Command getCommand() {
        return Command.playHiLoPerfect;
    }

    @Override
    Casino execute() {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(25L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(100000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(25, 500);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADSHEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        Player player1 = new Player(
                "Anne",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy1(table),
                playerFavoriteBet);
        Player player2 = new Player(
                "Beth",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy1(table),
                playerFavoriteBet);
        Player player3 = new Player(
                "Cassie",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy1(table),
                playerFavoriteBet);
        Player player4 = new Player(
                "Dot",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy2(table),
                playerFavoriteBet);
        Player player5 = new Player(
                "Edna",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy2(table),
                playerFavoriteBet);
        Player player6 = new Player(
                "Fanny",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy2(table),
                playerFavoriteBet);
        Player player7 = new Player(
                "Gail",
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                getPlayStrategy2(table),
                playerFavoriteBet);

        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        players.add(player7);

        for (Player player : players) {
            player.getPlayStrategy().getCardCountMethod().setBukofskyBankrollLevelDesired(BukofskyBankrollLevel.Level20k);
        }

        CurrencyAmount initialPlayerFunds = getSumOfPlayerBankrolls(players);

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        CurrencyAmount finalPlayerFunds = getSumOfPlayerBankrolls(players);

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerFunds);
        System.out.println("Final player bankrolls: " + finalPlayerFunds);
        System.out.println("Players " + finalPlayerFunds.describeDifference(initialPlayerFunds));

        return casino;
    }

    private PlayStrategy getPlayStrategy1(Table table) {
        return new PlayStrategyUstonApsPerfect(table, new BettingStrategyAlwaysFavorite());
    }

    private PlayStrategy getPlayStrategy2(Table table) {
        return new PlayStrategyBasic(table, new BettingStrategyAlwaysFavorite());
    }
}
