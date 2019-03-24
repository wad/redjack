package org.wadhome.redjack.execution;

import org.wadhome.redjack.Output;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyUstonApsPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(10000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(25, 10000);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Perfect Anne");
            add("Perfect Beth");
            add("Perfect Callie");
            add("Perfect Dora");
            add("Perfect Edna");
            add("Perfect Fran");
            add("Perfect Grace");
        }}.stream().map(name -> new Player(
                name,
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyUstonApsPerfect(table, new BettingStrategyBukofsky(true)),
                playerFavoriteBet)).
                collect(toList());

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
}
