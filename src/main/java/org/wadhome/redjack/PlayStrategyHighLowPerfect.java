package org.wadhome.redjack;

class PlayStrategyHighLowPerfect extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowPerfect(TableRules tableRules) {
        super(tableRules, new CardCountMethodHighLowPerfect(tableRules));
        this.basicStrategy = new PlayStrategyBasic(tableRules);
    }

    @Override
    BlackjackPlay choosePlay(
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable,
            Table table) {

        // todo: use the card count

        return this.basicStrategy.choosePlay(
                hand,
                dealerUpcard,
                bankrollAvailable,
                table);
    }

    @Override
    MoneyPile getInsuranceBet(
            MoneyPile maximumInsuranceBet,
            PlayerHand hand,
            Card dealerUpcard,
            MoneyPile bankrollAvailable,
            Table table) {
        // todo - apply the strategy rules here
        return MoneyPile.zero();
    }

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            Table table) {
        // todo: apply betting strategy here
        return basicStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet,
                table);
    }
}
