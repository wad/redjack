package org.wadhome.redjack.execution;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionHiLoCounterPerfect extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(10L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(10000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = TableRules.getDefaultRules();
        tableRules.setMinBet(new CurrencyAmount(10L));
        tableRules.setMaxBet(new CurrencyAmount(300L));

        Casino casino = new Casino(
                "Redjack (" + command + ")",
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
                new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                playerFavoriteBet)).
                collect(toList());

        CurrencyAmount initialPlayerFunds = getSumOfPlayerBankrolls(players);

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        CurrencyAmount finalPlayerFunds = getSumOfPlayerBankrolls(players);

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerFunds);
        System.out.println("Final player bankrolls: " + finalPlayerFunds);
        System.out.println("Players " + finalPlayerFunds.computeDifference(initialPlayerFunds));

        return casino;
    }
}
