package org.wadhome.redjack.rules;

import org.wadhome.redjack.money.CurrencyAmount;

public class TableRulesCustomMinMaxBets extends TableRules {
    public TableRulesCustomMinMaxBets(
            int minBetInDollars,
            int maxBetInDollars) {
        super();
        this.minBet = new CurrencyAmount(minBetInDollars);
        this.maxBet = new CurrencyAmount(maxBetInDollars);
    }
}
