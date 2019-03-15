package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;
    private MoneyPile houseBankroll = new MoneyPile(100000000000000L);
    private Display display;

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
        this.casinoName = casinoName;
        this.display = new Display(isDisplaying, isLogging);
        display.showMessage("Running " + casinoName + " with seed " + randomness.getSeed() + ".");
    }

    MoneyPile getBankroll() {
        return houseBankroll;
    }

    Display getDisplay() {
        return display;
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
        if (display != null) {
            display.closeLogs();
        }
    }
}
