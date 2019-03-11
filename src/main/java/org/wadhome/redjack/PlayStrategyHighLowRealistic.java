package org.wadhome.redjack;

class PlayStrategyHighLowRealistic extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowRealistic(
            Table table,
            BettingStrategy bettingStrategy) {
        super(
                table,
                new CardCountMethodHighLowRealistic(
                        table,
                        bettingStrategy));
        this.basicStrategy = new PlayStrategyBasic(table, bettingStrategy);
    }

    @Override
    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {
        if (getTrueCount(table) > 3) {
            return maximumInsuranceBet;
        }
        return MoneyPile.zero();
    }

    @Override
    BlackjackPlay choosePlay(
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {

        // todo: use the card count

        return this.basicStrategy.choosePlay(
                hand,
                dealerUpcard,
                bankrollAvailable);
    }

    private int getTrueCount(Table table) {
        CardCountMethodHighLowRealistic cardCountMethod = (CardCountMethodHighLowRealistic) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getDiscardTray());
    }

    private int getRunningCount() {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getRunningCount();
    }
}
