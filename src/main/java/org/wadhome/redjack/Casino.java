package org.wadhome.redjack;

import java.util.HashMap;
import java.util.Map;

public class Casino {
    private Map<Integer, Table> tables = new HashMap<>();
    private String casinoName;
    private MoneyPile houseBankroll = new MoneyPile(100000000);

    Casino(String casinoName) {
        this.casinoName = casinoName;
        Display.showMessage("Welcome to the " + casinoName + " casino.");
    }

    public MoneyPile getHouseBankroll() {
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

    public String getCasinoName() {
        return casinoName;
    }
}
