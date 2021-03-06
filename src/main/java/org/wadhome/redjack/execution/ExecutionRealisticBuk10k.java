package org.wadhome.redjack.execution;

import org.wadhome.redjack.Output;
import org.wadhome.redjack.bet.BettingStrategyBukofsky;
import org.wadhome.redjack.casino.Casino;
import org.wadhome.redjack.casino.Gender;
import org.wadhome.redjack.casino.Player;
import org.wadhome.redjack.casino.Table;
import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.CurrencyComputation;
import org.wadhome.redjack.money.MoneyPile;
import org.wadhome.redjack.rules.TableRules;
import org.wadhome.redjack.rules.TableRulesCustomMinMaxBets;
import org.wadhome.redjack.strategy.PlayStrategyHighLowRealistic;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

class ExecutionRealisticBuk10k extends Execution {

    public static void main(String... args) {
        new ExecutionRealisticBuk10k().execute();
    }

    @Override
    Command getCommand() {
        return Command.playHiLoRealisticBuk10k;
    }

    @Override
    Casino execute() {
        int numRoundsToPlay = 100000;
        CurrencyAmount playerFavoriteBet = new CurrencyAmount(25L);
        CurrencyAmount initialPlayerBankrolls = new CurrencyAmount(10000L);
        System.out.println("Seven players, each with "
                + initialPlayerBankrolls
                + ", betting " + playerFavoriteBet
                + ", playing until double, bankrupt, or " + numRoundsToPlay + " rounds.");

        TableRules tableRules = new TableRulesCustomMinMaxBets(25, 500);

        Casino casino = new Casino(
                "Redjack (" + getCommand() + ")",
                getSeed(),
                false,
                true);
        casino.getOutput().setSampleFactor(Output.SPREADSHEET_ROUNDS, numRoundsToPlay);
        Table table = casino.createTable(0, tableRules);

        List<Player> players = new ArrayList<String>() {{
            add("Buky Anne");
            add("Buky Beth");
            add("Buky Callie");
            add("Buky Dora");
            add("Buky Edna");
            add("Buky Fran");
            add("Buky Grace");
        }}.stream().map(name -> {
            MoneyPile initialPlayerBankroll = MoneyPile.extractMoneyFromFederalReserve(initialPlayerBankrolls);
            Player player = new Player(
                    name,
                    Gender.female,
                    casino,
                    initialPlayerBankroll,
                    new PlayStrategyHighLowRealistic(table, new BettingStrategyBukofsky(false)),
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
