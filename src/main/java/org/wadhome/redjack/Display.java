package org.wadhome.redjack;

class Display {

    private boolean isMute;

    public Display(boolean isMute) {
        this.isMute = isMute;
    }

    void showMessage(String message) {
        showMessage(message, false);
    }

    void showMessage(String message, boolean overrideMute) {
        if (overrideMute || !isMute) {
            System.out.println(message);
        }
    }
}
