package org.wadhome.redjack;

enum BlackjackPayOptions {
    TwoToOne("2:1"),
    ThreeToTwo("3:2"),
    SixToFive("6:5");

    String displayValue;

    BlackjackPayOptions(String displayValue) {
        this.displayValue = displayValue;
    }

    // this doesn't include their original bet, which is returned.
    MoneyPile computePlayerBlackjackWinnings(MoneyPile betAmount) {
        switch(this) {
            case TwoToOne:
                return betAmount.computeDouble();
            case ThreeToTwo:
                return betAmount.computeOneAndHalf();
            case SixToFive:
                return betAmount.computeSixFifths();
            default:
                throw new IllegalStateException("bug");
        }
    }
}
