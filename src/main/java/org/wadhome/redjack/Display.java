package org.wadhome.redjack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.joining;

class Display {

    private static final String BANKROLL_LOG_FILENAME = "bankroll-log";
    private static final String PLAY_LOG_FILENAME = "play-log";
    private BufferedWriter writerBankroll;
    private BufferedWriter writerPlay;

    private static final int CARD_COUNT_REPORT_COLUMN_START = 170;

    private boolean isDisplaying;
    private boolean isLogging;

    Display(
            boolean isDisplaying,
            boolean isLogging) {
        this.isDisplaying = isDisplaying;
        this.isLogging = isLogging;
        if (isLogging) {
            openLogs();
        }
    }

    boolean isLogging() {
        return isLogging;
    }

    boolean isOutputting() {
        return isDisplaying || isLogging;
    }

    void showMessage(String message) {
        showMessage(message, null);
    }

    void showMessage(
            String message,
            CardCountStatus cardCountStatus) {
        if (isOutputting()) {
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

            if (isLogging) {
                writeLineToPlayLog(message);
            }
            if (isDisplaying) {
                System.out.println(message);
            }
        }
    }

    void openLogs() {
        Path pathBankroll = Paths.get(BANKROLL_LOG_FILENAME);
        Path pathPlay = Paths.get(PLAY_LOG_FILENAME);
        try {
            writerBankroll = Files.newBufferedWriter(pathBankroll);
            writerPlay = Files.newBufferedWriter(pathPlay);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void closeLogs() {
        if (isLogging) {
            try {
                writerBankroll.close();
                writerBankroll.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void logRound(
            int roundNumber,
            List<Player> players) {
        if (isLogging) {
            boolean isFirstRound = roundNumber == 1;
            if (isFirstRound) {
                String nameLine = players.stream()
                        .map(Player::getPlayerName)
                        .map(n -> n.replace(',', ' '))
                        .collect(joining(","));
                writeLineToBankrollLog("round," + nameLine);
            }
            String bankrollLine = players
                    .stream()
                    .map(n -> n.getBankroll().format(false))
                    .collect(joining(","));
            writeLineToBankrollLog(roundNumber + "," + bankrollLine);
        }
    }

    void writeLineToBankrollLog(String line) {
        writeLineToFile(writerBankroll, line);
    }

    void writeLineToPlayLog(String line) {
        writeLineToFile(writerPlay, line);
    }

    void writeLineToFile(
            BufferedWriter writer,
            String line) {
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
