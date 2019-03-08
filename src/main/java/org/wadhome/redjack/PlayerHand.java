package org.wadhome.redjack;

public class PlayerHand extends Hand {
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

    public Seat getSeat() {
        return seat;
    }

    public PlayerHand separateOutRightSplit(MoneyPile betAmount) {
        Card leftSplitCard = this.firstCard;
        Card rightSplitCard = this.secondCard;
        removeCards();
        addCard(leftSplitCard);

        PlayerHand rightSplitSide = new PlayerHand(seat);
        rightSplitSide.setBetAmount(betAmount);
        rightSplitSide.addCard(rightSplitCard);
        return rightSplitSide;
    }

    public boolean isSplitHandAndIsFinished() {
        return isSplitHandAndIsFinished;
    }

    public void setSplitHandAndIsFinished() {
        isSplitHandAndIsFinished = true;
    }
}
