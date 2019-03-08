package org.wadhome.redjack;

class Display {

    private boolean isMute = false;

    void showMessage(String message) {
        if (!isMute) {
            System.out.println(message);
        }
    }

    void setMute(boolean mute) {
        isMute = mute;
    }
}
