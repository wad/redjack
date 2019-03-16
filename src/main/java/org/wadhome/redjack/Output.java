package org.wadhome.redjack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.joining;

class Output {

    private static final String BANKROLL_LOG_FILENAME = "bankroll.csv";
    private static final String BANKROLL_SAMPLE_LOG_FILENAME = "bankroll_sample.csv";
    private static final String PLAY_LOG_FILENAME = "play.log";
    private BufferedWriter writerBankroll;
    private BufferedWriter writerBankrollSample;
    private BufferedWriter writerPlay;

    private static final int CARD_COUNT_REPORT_COLUMN_START = 170;
    private static final int SAMPLE_FACTOR = 100;

    private boolean isDisplaying;
    private boolean isLogging;

    Output(
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

    boolean isDisplaying() {
        return isDisplaying;
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

    private void openLogs() {
        Path pathBankroll = Paths.get(BANKROLL_LOG_FILENAME);
        Path pathBankrollSample = Paths.get(BANKROLL_SAMPLE_LOG_FILENAME);
        Path pathPlay = Paths.get(PLAY_LOG_FILENAME);
        try {
            writerBankroll = Files.newBufferedWriter(pathBankroll);
            writerBankrollSample = Files.newBufferedWriter(pathBankrollSample);
            writerPlay = Files.newBufferedWriter(pathPlay);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void closeLogs() {
        if (isLogging) {
            try {
                writerBankroll.close();
                writerBankrollSample.close();
                writerPlay.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void logRound(
            int roundNumber,
            List<Player> players) {
        if (isLogging) {
            boolean isFirstRound = roundNumber == 0;
            if (isFirstRound) {
                String nameLine = players.stream()
                        .map(Player::getPlayerName)
                        .map(n -> n.replace(',', ' '))
                        .collect(joining(","));
                writeLineToBankrollLog(roundNumber, "round," + nameLine);
            }
            String bankrollLine = players
                    .stream()
                    .map(n -> n.getBankroll().format(false))
                    .collect(joining(","));
            writeLineToBankrollLog(roundNumber, roundNumber + "," + bankrollLine);
        }
    }

    private void writeLineToBankrollLog(int roundNumber, String line) {
        writeLineToFile(writerBankroll, line);
        if (roundNumber % SAMPLE_FACTOR == 0) {
            writeLineToFile(writerBankrollSample, line);
        }
    }

    private void writeLineToPlayLog(String line) {
        writeLineToFile(writerPlay, line);
    }

    private void writeLineToFile(
            BufferedWriter writer,
            String line) {
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
