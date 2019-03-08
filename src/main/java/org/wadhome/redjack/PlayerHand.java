package org.wadhome.redjack;

class PlayerHand extends Hand {
    private Seat seat;
    private MoneyPile betAmount = MoneyPile.zero();
    private boolean isSplitHandAndIsFinished;

    PlayerHand(Seat seat) {
        this.seat = seat;
    }

    MoneyPile getBetAmount() {
        return betAmount;
    }

    MoneyPile removeBet() {
        MoneyPile toReturn = betAmount.copy();
        this.betAmount = MoneyPile.zero();
        return toReturn;
    }

    void setBetAmount(MoneyPile betAmount) {
        this.betAmount = betAmount;
    }

    Seat getSeat() {
        return seat;
    }

    PlayerHand separateOutRightSplit(MoneyPile betAmount) {
        Card leftSplitCard = this.getFirstCard();
        Card rightSplitCard = this.getSecondCard();
        removeCards();
        addCard(leftSplitCard);

        PlayerHand rightSplitSide = new PlayerHand(seat);
        rightSplitSide.setBetAmount(betAmount);
        rightSplitSide.addCard(rightSplitCard);
        return rightSplitSide;
    }

    boolean isSplitHandAndIsFinished() {
        return isSplitHandAndIsFinished;
    }

    void setSplitHandAndIsFinished() {
        isSplitHandAndIsFinished = true;
    }
}
