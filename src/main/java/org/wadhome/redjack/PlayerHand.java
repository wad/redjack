package org.wadhome.redjack;

public class PlayerHand extends Hand {
    private Seat seat;
    private MoneyPile betAmount = MoneyPile.zero();

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
}
