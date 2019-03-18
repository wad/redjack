package org.wadhome.redjack.rules;

import org.wadhome.redjack.money.CurrencyAmount;
import org.wadhome.redjack.money.CurrencyComputation;

public enum BlackjackPayOptions {
    TwoToOne("2:1"),
    ThreeToTwo("3:2"),
    SixToFive("6:5");

    String displayValue;

    BlackjackPayOptions(String displayValue) {
        this.displayValue = displayValue;
    }

    // this doesn't include their original bet, which is returned.
    public CurrencyAmount computePlayerBlackjackWinnings(CurrencyAmount betAmount) {
        switch (this) {
            case TwoToOne:
                return betAmount.compute(CurrencyComputation.doubleOf);
            case ThreeToTwo:
                return betAmount.compute(CurrencyComputation.oneAndHalfOf);
            case SixToFive:
                return betAmount.compute(CurrencyComputation.sixFifthsOf);
            default:
                throw new IllegalStateException("bug");
        }
    }
}
