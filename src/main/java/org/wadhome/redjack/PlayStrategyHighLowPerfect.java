package org.wadhome.redjack;

class PlayStrategyHighLowPerfect extends PlayStrategy {

    private PlayStrategyBasic basicStrategy;

    PlayStrategyHighLowPerfect(Table table) {
        super(table, new CardCountMethodHighLowPerfect(table.getTableRules()));
        this.basicStrategy = new PlayStrategyBasic(table);
    }

    int getTrueCount(Table table) {
        CardCountMethodHighLowPerfect cardCountMethod = (CardCountMethodHighLowPerfect) getCardCountMethod();
        return cardCountMethod.getTrueCount(table.getShoe().cards.size());
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
        if (getTrueCount(table) > 3) {
            return maximumInsuranceBet;
        }
        return MoneyPile.zero();
    }

    @Override
    MoneyPile getBet(
            MoneyPile favoriteBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet) {

        // todo: apply betting strategy here

        int trueCount = getTrueCount(table);
//        System.out.println("--------------- true count: " + trueCount);
        if (trueCount >= 3) {
            return maxPossibleBet;
        }

        return basicStrategy.getBet(
                favoriteBet,
                minPossibleBet,
                maxPossibleBet);
    }
}
