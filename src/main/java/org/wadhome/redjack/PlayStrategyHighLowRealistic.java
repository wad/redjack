package org.wadhome.redjack;

class PlayStrategyHighLowRealistic extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowRealistic(Table table) {
        super(table, new CardCountMethodHighLowRealistic(table.getTableRules()));
        this.basicStrategy = new PlayStrategyBasic(table);
    }

    int getTrueCount(Table table) {
        CardCountMethodHighLowRealistic cardCountMethod = (CardCountMethodHighLowRealistic) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getDiscardTray());
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

    @Override
    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable) {
        // todo - apply the strategy rules here
        return MoneyPile.zero();
    }

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet) {
        // todo: apply betting strategy here
        return basicStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet);
    }
}
