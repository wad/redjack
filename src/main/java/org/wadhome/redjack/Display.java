package org.wadhome.redjack;

class Display {

    private static final int CARD_COUNT_REPORT_COLUMN_START = 170;

    private boolean isMute;

    Display(boolean isMute) {
        this.isMute = isMute;
    }

    void showMessage(String message) {
        showMessage(message, false, null);
    }

    void showMessage(String message, boolean overrideMute) {
        showMessage(message, overrideMute, null);
    }

    void showMessage(String message, CardCountStatus cardCountStatus) {
        showMessage(message, false, cardCountStatus);
    }

    void showMessage(
            String message,
            boolean overrideMute,
            CardCountStatus cardCountStatus) {
        if (isMute && !overrideMute) {
            return;
        }

        if (cardCountStatus != null) {
            int numCharsInMessage = message.length();
            StringBuilder buffer = new StringBuilder(" ");
            if (numCharsInMessage < CARD_COUNT_REPORT_COLUMN_START) {
                for (int i = numCharsInMessage; i < CARD_COUNT_REPORT_COLUMN_START; i++) {
                    buffer.append(" ");
                }
            }
            message = message + buffer + cardCountStatus.getReport();
        }

        System.out.println(message);
    }
}
