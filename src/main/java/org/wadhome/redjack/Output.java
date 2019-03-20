package org.wadhome.redjack;

import org.wadhome.redjack.cardcount.CardCountStatus;
import org.wadhome.redjack.casino.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Output {
    // The spreadsheet with the chart is set to display 1000 rows of data (rounds).
    public static final int SPREADHSEET_ROUNDS = 1000;

    private static final String BANKROLL_LOG_FILENAME = "bankroll.csv";
    private static final String BANKROLL_SAMPLE_LOG_FILENAME = "bankroll_sample.csv";
    private static final String PLAY_LOG_FILENAME = "play.log";
    private BufferedWriter writerBankroll;
    private BufferedWriter writerBankrollSample;
    private BufferedWriter writerPlay;

    private static final int NUM_ROUNDS_TO_CUT_OFF_PLAY_LOGGING = 100100;
    private boolean isPlayLoggingCutOff = false;

    private static final int CARD_COUNT_REPORT_COLUMN_START = 170;

    private boolean isDisplaying;
    private boolean isLogging;
    private int sampleFactor = 100; // this works for runs of 100000 rounds.

    public Output(
            boolean isDisplaying,
            boolean isLogging) {
        this.isDisplaying = isDisplaying;
        this.isLogging = isLogging;
        if (isLogging) {
            openLogs();
        }
    }

    public void setSampleFactor(
            int desiredTotalRoundsInBankrollSample,
            int expectedNumRounds) {
        sampleFactor = expectedNumRounds / desiredTotalRoundsInBankrollSample;
    }

    public boolean isLogging() {
        return isLogging;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDisplaying() {
        return isDisplaying;
    }

    boolean isOutputting() {
        return isDisplaying || isLogging;
    }

    public void showMessage(String message) {
        showMessage(message, null);
    }

    public void showMessage(
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

    public void closeLogs() {
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

    public void logRound(
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
            if (roundNumber > NUM_ROUNDS_TO_CUT_OFF_PLAY_LOGGING) {
                isPlayLoggingCutOff = true;
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
        if (roundNumber % sampleFactor == 0 || sampleFactor <= 1) {
            writeLineToFile(writerBankrollSample, line);
        }
    }

    private void writeLineToPlayLog(String line) {
        if (!isPlayLoggingCutOff) {
            writeLineToFile(writerPlay, line);
        }
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
