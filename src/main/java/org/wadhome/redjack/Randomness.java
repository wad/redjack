package org.wadhome.redjack;

import java.util.Collections;
import java.util.List;
import java.util.Random;

class Randomness {
    private Random random;
    private Long seed;

    Randomness(Long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    boolean getRandomBoolean() {
        return (random.nextLong() & 1) == 1;
    }

    void shuffle(List list) {
        if (seed == null) {
            Collections.shuffle(list);
        } else {
            Collections.shuffle(list, random);
        }
    }

    static long generateRandomSeed() {
        Random rand = new Random();
        return rand.nextLong();
    }

    Long getSeed() {
        return seed;
    }
}
