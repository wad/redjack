package org.wadhome.redjack.rules;

import org.wadhome.redjack.money.CurrencyAmount;

public class TableRulesEasy extends TableRules {
    public TableRulesEasy() {
        super();
        this.minBet = new CurrencyAmount(25L);
        this.maxBet = new CurrencyAmount(500L);
        this.hitSoftSeventeen = false;
        this.canSurrender = true;
    }
}
