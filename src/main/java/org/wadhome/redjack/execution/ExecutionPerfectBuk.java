package org.wadhome.redjack.execution;

import org.wadhome.redjack.*;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.CurrencyComputation;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyHighLowPerfect;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionPerfectBuk extends Execution {
    @Override
    Casino execute(Command command) {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(5L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(2000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing until double, bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(5, 100);

        Casino casino = new Casino(
                "Redjack (" + command + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADHSEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Perfect Buky Anne");
            add("Perfect Buky Beth");
            add("Perfect Buky Callie");
            add("Perfect Buky Dora");
            add("Perfect Buky Edna");
            add("Perfect Buky Fran");
            add("Perfect Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowPerfect(table, new BettingStrategyBukofsky(true)),
                    playerFavoriteBet);
            CurrencyAmount doubled = initialPlayerBankroll.getCurrencyAmountCopy().compute(CurrencyComputation.doubleOf);
            player.setRetirementTriggerBankroll(doubled);
            return player;
        }).collect(toList());

        assignPlayersToTable(players, table);
        table.playRounds(numRoundsToPlay);

        System.out.println();
        System.out.println("Retired players: " + countRetiredPlayers(players)
                + ". Bankrupt players: " + countBankruptPlayers(players) + ".");

        return casino;
    }
}
