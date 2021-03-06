package org.wadhome.redjack;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Randomness {
    private Random random;
    private Long seed;
    private Boolean overrideNextPercentCheckWithThisValue = null;

    public Randomness(Long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public boolean getRandomBoolean() {
        return (random.nextLong() & 1) == 1;
    }

    private int getRandomNumberFromZeroToValueExclusive(int maxValue) {
        return random.nextInt(maxValue);
    }

    public boolean checkRandomPercentChance(int percentChance) {
        if (overrideNextPercentCheckWithThisValue != null) {
            boolean result = overrideNextPercentCheckWithThisValue;
            overrideNextPercentCheckWithThisValue = null;
            return result;
        }

        if (percentChance <= 0) {
            return false;
        }
        if (percentChance >= 100) {
            return true;
        }
        return getRandomNumberFromZeroToValueExclusive(100) < percentChance;
    }

    public void shuffle(List list) {
        if (seed == null) {
            Collections.shuffle(list);
        } else {
            Collections.shuffle(list, random);
        }
    }

    public static long generateRandomSeed() {
        Random rand = new Random();
        return rand.nextLong();
    }

    public Long getSeed() {
        return seed;
    }

    public void overrideNextPercentCheckWithThisValue(Boolean overrideNextPercentCheckWithThisValue) {
        this.overrideNextPercentCheckWithThisValue = overrideNextPercentCheckWithThisValue;
    }
}
