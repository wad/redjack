package org.wadhome.redjack;

class CardCounterHighLowPerfect extends Strategy {

    private BasicStrategy basicStrategy = new BasicStrategy();

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
