package dev.vimalram.arcade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class HighScoreManager {

    private static final Path SAVE_DIRECTORY =
            Paths.get(System.getProperty("user.home"), ".java-arcade");

    private static final Path SAVE_FILE =
            SAVE_DIRECTORY.resolve("highscores.properties");

    private HighScoreManager() {
    }

    public static int getHighScore(String gameName) {
        Properties scores = loadScores();

        try {
            return Integer.parseInt(
                    scores.getProperty(gameName, "0")
            );
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static void saveScore(String gameName, int score) {
        Properties scores = loadScores();
        int currentHighScore = getHighScore(gameName);

        if (score <= currentHighScore) {
            return;
        }

        scores.setProperty(gameName, String.valueOf(score));

        try {
            Files.createDirectories(SAVE_DIRECTORY);

            try (var output = Files.newOutputStream(SAVE_FILE)) {
                scores.store(output, "Java Arcade High Scores");
            }
        } catch (IOException exception) {
            System.err.println(
                    "Could not save high score: " + exception.getMessage()
            );
        }
    }

    private static Properties loadScores() {
        Properties scores = new Properties();

        if (!Files.exists(SAVE_FILE)) {
            return scores;
        }

        try (var input = Files.newInputStream(SAVE_FILE)) {
            scores.load(input);
        } catch (IOException exception) {
            System.err.println(
                    "Could not load high scores: " + exception.getMessage()
            );
        }

        return scores;
    }
}
