package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private MoneyPile houseBankroll = new MoneyPile(100000000000000L);
    private Output output;

    // used for testing
    Casino() {
        this(
                "test",
                Randomness.generateRandomSeed(),
                false,
                false);
    }

    Casino(
            String casinoName,
            Long seed,
            boolean isDisplaying,
            boolean isLogging) {
        randomness = new Randomness(seed);
        this.output = new Output(isDisplaying, isLogging);
        output.showMessage("Running " + casinoName + " with seed " + randomness.getSeed() + ".");
    }

    MoneyPile getBankroll() {
        return houseBankroll;
    }

    Output getOutput() {
        return output;
    }

    Table createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                this,
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
        table.shuffleAndStuff();
        return table;
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    Randomness getRandomness() {
        return randomness;
    }

    void closeCasino() {
        if (output != null) {
            output.closeLogs();
        }
    }
}
