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
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionRealisticBukBut4k extends Execution {

    public static void main(String... args) {
        new ExecutionRealisticBukBut4k().execute();
    }

    @Override
    Command getCommand() {
        return Command.playHiLoRealisticBukBut4k;
    }

    @Override
    Casino execute() {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(5L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(4000L);
        CurrencyAmount retirementBankroll = new CurrencyAmount(5000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing until " + retirementBankroll + ", bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(50, 100);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADSHEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Real Buky Anne");
            add("Real Buky Beth");
            add("Real Buky Callie");
            add("Real Buky Dora");
            add("Real Buky Edna");
            add("Real Buky Fran");
            add("Real Buky Grace");
        }}.stream().map(name -> {
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls),
                    new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
                    playerFavoriteBet);
            player.setRetirementTriggerBankroll(retirementBankroll);
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
