package org.wadhome.redjack.bet;

import org.wadhome.redjack.MoneyPile;
import org.wadhome.redjack.Player;
import org.wadhome.redjack.Randomness;

public class BetRequest {
    private Player player;
    private Randomness randomness;
    private MoneyPile desiredBet;
    private MoneyPile minPossibleBet;
    private MoneyPile maxPossibleBet;
    private MoneyPile availableBankroll;
    private MoneyPile actualBetAmount;
    private Integer trueCount = null;
    private String betComment = null;
    private boolean canPlaceBet = true;

    public BetRequest(
            Player player,
            Randomness randomness,
            MoneyPile desiredBet,
            MoneyPile minPossibleBet,
            MoneyPile maxPossibleBet,
            MoneyPile availableBankroll) {

        this.player = player;
        this.randomness = randomness;
        this.desiredBet = desiredBet;
        this.minPossibleBet = minPossibleBet;
        this.maxPossibleBet = maxPossibleBet;
        this.availableBankroll = availableBankroll;

        validateAndAdjust();
    }

    private void validateAndAdjust() {
        if (minPossibleBet.isGreaterThan(maxPossibleBet)) {
            throw new IllegalStateException("Min bet is more than max bet!");
        }

        if (minPossibleBet.isGreaterThan(availableBankroll)) {
            betComment = "Oops, I can't afford to bet.";
            canPlaceBet = false;
            return;
        }

        if (maxPossibleBet.isGreaterThan(availableBankroll)) {
            maxPossibleBet = availableBankroll.copy();
        }

        if (desiredBet.isGreaterThan(maxPossibleBet)) {
            desiredBet = maxPossibleBet.copy();
        }

        if (desiredBet.isLessThan(minPossibleBet)) {
            betComment = "I'd like to bet " + desiredBet + " but I must bet at least " + minPossibleBet + ".";
            desiredBet = minPossibleBet.copy();
        }
    }

    public void setTrueCount(int trueCount) {
        this.trueCount = trueCount;
    }

    Player getPlayer() {
        return player;
    }

    Randomness getRandomness() {
        return randomness;
    }

    MoneyPile getDesiredBet() {
        return desiredBet;
    }

    MoneyPile getMinPossibleBet() {
        return minPossibleBet;
    }

    MoneyPile getMaxPossibleBet() {
        return maxPossibleBet;
    }

    int getTrueCount() {
        return trueCount;
    }

    MoneyPile setConstrainedActualBetAmount(final MoneyPile calculatedBet) {
        if (!canPlaceBet) {
            throw new IllegalStateException("bug! min=" + minPossibleBet
                    + " max=" + maxPossibleBet
                    + " calc=" + calculatedBet);
        }

        if (calculatedBet.isLessThan(minPossibleBet)) {
            betComment = "I'd like to bet " + calculatedBet + " but that's below the minimum.";
            actualBetAmount = minPossibleBet.copy();
            return actualBetAmount;
        }

        if (calculatedBet.isGreaterThan(maxPossibleBet)) {
            betComment = "I'd like to bet " + calculatedBet + " but that's too much.";
            actualBetAmount = maxPossibleBet.copy();
            return actualBetAmount;
        }

        actualBetAmount = calculatedBet.copy();
        return actualBetAmount;
    }

    public MoneyPile getActualBetAmount() {
        return actualBetAmount;
    }

    public boolean canPlaceBet() {
        return canPlaceBet;
    }

    void setBetComment(String betComment) {
        if (this.betComment == null) {
            this.betComment = betComment;
        } else {
            this.betComment += (" " + betComment);
        }
    }

    public String getBetComment() {
        return betComment;
    }
}
