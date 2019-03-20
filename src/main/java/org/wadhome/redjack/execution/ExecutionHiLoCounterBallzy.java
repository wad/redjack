package org.wadhome.redjack.execution;

import org.wadhome.redjack.Output;
import org.wadhome.redjack.bet.BettingStrategyMaxOnGoodCount;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionHiLoCounterBallzy extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(10L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(10000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(10, 300);

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Ballzy Anne");
            add("Ballzy Beth");
            add("Ballzy Callie");
            add("Ballzy Dora");
            add("Ballzy Edna");
            add("Ballzy Fran");
            add("Ballzy Grace");
        }}.stream().map(name -> new Player(
                name,
                Gender.female,
                casino,
                MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                new PlayStrategyHighLowPerfect(table, new BettingStrategyMaxOnGoodCount()),
                playerFavoriteBet)).
                collect(toList());

        CurrencyAmount initialPlayerFunds = getSumOfPlayerBankrolls(players);

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        CurrencyAmount finalPlayerFunds = getSumOfPlayerBankrolls(players);

        System.out.println();
        System.out.println("Initial player bankrolls: " + initialPlayerFunds);
        System.out.println("Final player bankrolls: " + finalPlayerFunds);
        System.out.println("Players " + initialPlayerFunds.describeDifference(initialPlayerFunds));

        return casino;
    }
}
