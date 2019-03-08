package org.wadhome.redjack;

enum Gender {
    male,
    female;

    String getHisHer(boolean shouldCapitalize) {
        switch (this) {
            case male:
                return shouldCapitalize ? "His" : "his";
            case female:
                return shouldCapitalize ? "Her" : "her";
            default:
                throw new RuntimeException("Bug");
        }
    }

    String getHeShe(
            @SuppressWarnings("SameParameterValue") boolean shouldCapitalize) {
        switch (this) {
            case male:
                return shouldCapitalize ? "He" : "he";
            case female:
                return shouldCapitalize ? "She" : "she";
            default:
                throw new RuntimeException("Bug");
        }
    }

    static Gender getRandomGender(Randomness randomness) {
        return randomness.getRandomBoolean() ? male : female;
    }
}
