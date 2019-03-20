package org.wadhome.redjack.bet;

import org.wadhome.redjack.money.CurrencyAmount;

public enum BukofskyBankrollLevel {
    Level100k(100000, 500,
            new int[]{417, 667, 909, 1333, 1538, 1818, 2222, 2632, 2857, 3125},
            new int[]{500, 500, 1000, 1500, 1500, 2000, 2000, 2500, 3000, 3500}),
    Level40k(40000, 200,
            new int[]{166, 267, 364, 533, 615, 727, 889, 1053, 1143, 1250},
            new int[]{200, 300, 400, 500, 600, 700, 900, 1000, 1200, 1300}),
    Level20k(20000, 25,
            new int[]{83, 133, 182, 267, 338, 364, 444, 526, 571, 625},
            new int[]{75, 100, 200, 300, 300, 400, 500, 500, 600, 600}),
    Level10k(10000, 25,
            new int[]{40, 40, 90, 125, 160, 190, 210, 260, 300, 325},
            new int[]{50, 50, 75, 125, 150, 175, 200, 250, 300, 325}),
    Level5k(5000, 10,
            new int[]{20, 33, 45, 67, 77, 91, 111, 132, 143, 156},
            new int[]{30, 40, 50, 60, 70, 100, 100, 125, 125, 150}),
    Level2k(2000, 5,
            new int[]{8, 13, 18, 26, 31, 36, 44, 53, 57, 63},
            new int[]{10, 15, 20, 25, 30, 35, 45, 50, 55, 60}),
    LevelZero(0, 0,
            new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    private int minimumBankrollInDollars;
    private int minimumBetInDollars;
    private int[] perfectBetMatrix;
    private int[] realisticBetMatrix;

    BukofskyBankrollLevel(
            int minimumBankrollInDollars,
            int minimumBetInDollars,
            int[] perfectBetMatrix,
            int[] realisticBetMatrix) {
        this.minimumBankrollInDollars = minimumBankrollInDollars;
        this.minimumBetInDollars = minimumBetInDollars;
        this.perfectBetMatrix = perfectBetMatrix;
        this.realisticBetMatrix = realisticBetMatrix;
    }

    CurrencyAmount getBet(
            int trueCount,
            boolean beSuspiciouslyPerfect) {
        if (trueCount < 3) {
            throw new IllegalStateException("Bug! TrueCount is " + trueCount + ", should be at least 3.");
        }

        int[] betMatrix = beSuspiciouslyPerfect ? perfectBetMatrix : realisticBetMatrix;
        int indexIntoMatrix = trueCount - 3; // only have extra bets when the true count is >= 3
        if (indexIntoMatrix >= betMatrix.length) {
            indexIntoMatrix = betMatrix.length - 1;
        }
        int betAmountInDollars = betMatrix[indexIntoMatrix];
        return new CurrencyAmount(betAmountInDollars);
    }

    static BukofskyBankrollLevel determine(CurrencyAmount initialBankroll) {
        for (BukofskyBankrollLevel level : values()) {
            if (initialBankroll.isGreaterThanOrEqualToDollars(level.minimumBankrollInDollars)) {
                return level;
            }
        }
        throw new RuntimeException("Bug! initialBankroll=" + initialBankroll);
    }
}
