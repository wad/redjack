package org.wadhome.redjack;

import org.wadhome.redjack.money.CurrencyAmount;

public class PlayerHand extends Hand {
    private Seat seat;
    private CurrencyAmount betAmount = CurrencyAmount.zero();
    private boolean isSplitHandAndIsFinished;

    public PlayerHand(Seat seat) {
        this.seat = seat;
    }

    public CurrencyAmount getBetAmount() {
        return betAmount.copy();
    }

    void setBetAmount(CurrencyAmount betAmount) {
        this.betAmount = betAmount.copy();
    }

    CurrencyAmount removeBet() {
        CurrencyAmount betToRemove = this.betAmount.copy();
        this.betAmount = CurrencyAmount.zero();
        return betToRemove;
    }

    public Seat getSeat() {
        return seat;
    }

    PlayerHand separateOutRightSplit(CurrencyAmount betAmount) {
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
