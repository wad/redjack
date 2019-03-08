package org.wadhome.redjack;

import java.util.ArrayList;
import java.util.List;

public class Seat {

    private final SeatNumber seatNumber;

    private Table table;
    private Player player = null;
    private List<PlayerHand> hands = new ArrayList<>();

    public Seat(
            Table table,
            SeatNumber seatNumber) {
        this.table = table;
        this.seatNumber = seatNumber;
    }

    public SeatNumber getSeatNumber() {
        return seatNumber;
    }

    public int getSeatNumeral() {
        return seatNumber.ordinal() + 1;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void removePlayer() {
        this.player = null;
    }

    public List<PlayerHand> getHands() {
        return hands;
    }

    public PlayerHand addNewHand(MoneyPile betAmount) {
        PlayerHand hand = new PlayerHand(this);
        hand.setBetAmount(betAmount);
        addHand(hand);
        return hand;
    }

    public void addHand(PlayerHand hand) {
        hands.add(hand);
    }

    public void destroyHands() {
        hands.clear();
    }

    public MoneyPile computeBetSum() {
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
