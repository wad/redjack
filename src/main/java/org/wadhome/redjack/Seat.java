package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

class Seat {
    private final SeatNumber seatNumber;
    private Player player = null;
    private List<PlayerHand> hands = new ArrayList<>();

    Seat(SeatNumber seatNumber) {
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

    PlayerHand addNewHand(MoneyPile betAmount) {
        PlayerHand hand = new PlayerHand(this);
        hand.setBetAmount(betAmount);
        addHand(hand);
        return hand;
    }

    void addHand(PlayerHand hand) {
        hands.add(hand);
    }

    void destroyHands() {
        hands.clear();
    }

    MoneyPile computeBetSum() {
        MoneyPile sum = MoneyPile.zero();
        for (PlayerHand hand : hands) {
            sum.addToPile(hand.getBetAmount());
        }
        return sum;
    }

    int getNumSplitsSoFar() {
        if (hands.isEmpty()) {
            return 0;
        }
        return hands.size() - 1;
    }
}
