package org.wadhome.redjack;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Randomness {
    private Random random;
    private Long seed;

    public Randomness(Long seed) {
        this.seed = seed;
        if (seed == null) {
            random = new Random();
        } else {
            random = new Random(seed);
        }
    }

    public boolean getRandomBoolean() {
        return (random.nextLong() & 1) == 1;
    }

    void shuffle(List list) {
        if (seed == null) {
            Collections.shuffle(list);
        } else {
            Collections.shuffle(list, random);
        }
    }

    public Long getSeed() {
        return seed;
    }
}
