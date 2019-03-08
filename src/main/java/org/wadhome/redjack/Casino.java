package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

class Casino {
    private Randomness randomness;
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;
    private MoneyPile houseBankroll = new MoneyPile(100000000);

    Casino(String casinoName) {
        this(casinoName, null);
    }

    Casino(
            String casinoName,
            Long seed) {
        this.casinoName = casinoName;

        if (seed == null) {
            seed = Randomness.generateRandomSeed();
        }
        randomness = new Randomness(seed);

        showWelcomeInfo();
    }

    private void showWelcomeInfo() {
        Display.showMessage("Welcome to the " + casinoName + " casino.");
        Long seed = randomness.getSeed();
        Display.showMessage("Randomness seed: " + seed);
    }

    MoneyPile getBankroll() {
        return houseBankroll;
    }

    void createTable(
            int tableNumber,
            TableRules tableRules) {
        Table table = new Table(
                this,
                tableNumber,
                tableRules);
        tables.put(tableNumber, table);
        table.showPreparationMessages();
    }

    Table getTable(int tableNumber) {
        return tables.get(tableNumber);
    }

    Randomness getRandomness() {
        return randomness;
    }
}
