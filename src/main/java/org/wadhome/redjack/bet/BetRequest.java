package org.wadhome.redjack.bet;

import org.wadhome.redjack.Casino;
import org.wadhome.redjack.Player;
import org.wadhome.redjack.Randomness;
import org.wadhome.redjack.Table;
import org.wadhome.redjack.money.CurrencyAmount;

public class BetRequest {
    private Player player;
    private Randomness randomness;
    private CurrencyAmount desiredBet;
    private CurrencyAmount minPossibleBet;
    private CurrencyAmount maxPossibleBet;
    private CurrencyAmount availableBankroll;
    private CurrencyAmount actualBetAmount = null;
    private Integer trueCount = null;
    private String betComment = null;
    private boolean canPlaceBet = true;

    public BetRequest(
            Casino casino,
            Table table,
            Player player) {

        this.player = player;
        this.randomness = casino.getRandomness();
        this.desiredBet = player.getFavoriteBet(); // can be overridden by strategy
        this.minPossibleBet = table.getTableRules().getMinBet();
        this.maxPossibleBet = table.getTableRules().getMaxBet();
        this.availableBankroll = player.getBankroll().getCurrencyAmountCopy();

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

    CurrencyAmount getDesiredBet() {
        return desiredBet.copy();
    }

    CurrencyAmount getMinPossibleBet() {
        return minPossibleBet.copy();
    }

    CurrencyAmount getMaxPossibleBet() {
        return maxPossibleBet.copy();
    }

    Integer getTrueCount() {
        return trueCount;
    }

    CurrencyAmount setConstrainedActualBetAmount(CurrencyAmount calculatedBet) {
        if (actualBetAmount != null) {
            throw new IllegalStateException("Bug! Setting a bet more than once?");
        }

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

    public CurrencyAmount getActualBetAmount() {
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
