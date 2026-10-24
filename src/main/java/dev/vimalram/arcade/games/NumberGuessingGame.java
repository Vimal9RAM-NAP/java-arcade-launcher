package dev.vimalram.arcade.games;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGame {

    private final Random random = new Random();

    private JFrame frame;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JTextField guessField;
    private JComboBox<String> difficultyBox;
    private JButton guessButton;

    private int secretNumber;
    private int attemptsLeft;
    private int maxAttempts;
    private int score;
    private boolean gameOver;

    public void open() {

        frame = new JFrame("Number Guessing");
        frame.setSize(520, 460);
        frame.setMinimumSize(new Dimension(440, 420));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(18, 20, 29));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("NUMBER GUESSING");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Find the secret number before you run out of attempts."
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(166, 174, 198));

        JLabel difficultyLabel = new JLabel("DIFFICULTY");
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyLabel.setForeground(new Color(180, 170, 255));
        difficultyLabel.setFont(new Font("Monospaced", Font.BOLD, 12));

        difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"}
        );
        difficultyBox.setMaximumSize(new Dimension(200, 35));
        difficultyBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        guessField = new JTextField();
        guessField.setMaximumSize(new Dimension(220, 42));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("SansSerif", Font.BOLD, 20));
        guessField.setToolTipText("Enter your guess");

        guessButton = new JButton("SUBMIT GUESS");
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessButton.setBackground(new Color(124, 92, 255));
        guessButton.setForeground(Color.WHITE);
        guessButton.setFocusPainted(false);
        guessButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        feedbackLabel = createLabel("Choose a difficulty and start guessing.");
        attemptsLabel = createLabel("");
        scoreLabel = createLabel("ROUND SCORE: 0");

        JButton newGameButton = new JButton("NEW GAME");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(event -> startNewGame());

        guessButton.addActionListener(event -> submitGuess());

        guessField.addActionListener(event -> submitGuess());

        difficultyBox.addActionListener(event -> startNewGame());

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(22));
        mainPanel.add(difficultyLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(difficultyBox);
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(guessField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(guessButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(feedbackLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(attemptsLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(scoreLabel);
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(newGameButton);

        frame.add(mainPanel);
        startNewGame();
        frame.setVisible(true);
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return label;
    }

    private void startNewGame() {

        String difficulty = (String) difficultyBox.getSelectedItem();

        int maxNumber;

        switch (difficulty) {

            case "Medium" -> {
                maxNumber = 100;
                maxAttempts = 7;
            }

            case "Hard" -> {
                maxNumber = 200;
                maxAttempts = 8;
            }

            default -> {
                maxNumber = 50;
                maxAttempts = 10;
            }
        }

        secretNumber = random.nextInt(maxNumber) + 1;
        attemptsLeft = maxAttempts;
        score = 0;
        gameOver = false;

        feedbackLabel.setText(
                "Guess a number between 1 and " + maxNumber + "."
        );

        attemptsLabel.setText(
                "ATTEMPTS LEFT: " + attemptsLeft
        );

        scoreLabel.setText("ROUND SCORE: 0");

        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);

        guessField.requestFocusInWindow();
    }

    private void submitGuess() {

        if (gameOver) {
            return;
        }

        int guess;

        try {
            guess = Integer.parseInt(guessField.getText().trim());
        } catch (NumberFormatException exception) {
            feedbackLabel.setText("Enter a valid whole number.");
            return;
        }

        int maxNumber = switch (difficultyBox.getSelectedIndex()) {
            case 1 -> 100;
            case 2 -> 200;
            default -> 50;
        };

        if (guess < 1 || guess > maxNumber) {
            feedbackLabel.setText(
                    "Enter a number between 1 and " + maxNumber + "."
            );
            return;
        }

        attemptsLeft--;

        if (guess == secretNumber) {

            score = attemptsLeft * 10 + 10;

            feedbackLabel.setText(
                    "Correct! The number was " + secretNumber + "."
            );

            scoreLabel.setText("ROUND SCORE: " + score);

            finishGame();

        } else if (attemptsLeft == 0) {

            feedbackLabel.setText(
                    "Game over! The number was " + secretNumber + "."
            );

            scoreLabel.setText("ROUND SCORE: 0");

            finishGame();

        } else if (guess < secretNumber) {

            feedbackLabel.setText("Too low! Try a higher number.");

        } else {

            feedbackLabel.setText("Too high! Try a lower number.");

        }

        attemptsLabel.setText(
                "ATTEMPTS LEFT: " + attemptsLeft
        );

        guessField.setText("");
        guessField.requestFocusInWindow();
    }

    private void finishGame() {

        gameOver = true;

        guessField.setEnabled(false);
        guessButton.setEnabled(false);
    }
}