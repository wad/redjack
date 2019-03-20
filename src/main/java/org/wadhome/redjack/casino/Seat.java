package org.wadhome.redjack.casino;

import org.wadhome.redjack.money.CurrencyAmount;

import java.util.ArrayList;
import java.util.List;

public class Seat {
    private final SeatNumber seatNumber;
    private Player player = null;
    private List<PlayerHand> hands = new ArrayList<>();

    public Seat(SeatNumber seatNumber) {
        this.seatNumber = seatNumber;
    }

    SeatNumber getSeatNumber() {
        return seatNumber;
    }

    int getSeatNumeral() {
        return seatNumber.ordinal() + 1;
    }

    Player getPlayer() {
        return player;
    }

    boolean hasPlayer() {
        return player != null;
    }

    void setPlayer(Player player) {
        this.player = player;
    }

    void removePlayer() {
        this.player = null;
    }

    List<PlayerHand> getHands() {
        return hands;
    }

    PlayerHand addNewHand(CurrencyAmount betAmount) {
        PlayerHand hand = new PlayerHand(this);
        hand.setBetAmount(betAmount.copy());
        addHand(hand);
        return hand;
    }

    void addHand(PlayerHand hand) {
        hands.add(hand);
    }

    void destroyHands() {
        hands.clear();
    }

    CurrencyAmount computeBetSum() {
        CurrencyAmount sum = CurrencyAmount.zero();
        for (PlayerHand hand : hands) {
            sum.increaseBy(hand.getBetAmount());
        }
        return sum;
    }

    public int getNumSplitsSoFar() {
        if (hands.isEmpty()) {
            return 0;
        }
        return hands.size() - 1;
    }
}
