package dev.vimalram.arcade.games;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;

public class RockPaperScissorsGame {

    private final Random random = new Random();

    private int playerScore = 0;
    private int computerScore = 0;
    private int draws = 0;

    private JLabel resultLabel;
    private JLabel playerChoiceLabel;
    private JLabel computerChoiceLabel;
    private JLabel scoreLabel;

    public void open() {

        JFrame frame = new JFrame("Rock Paper Scissors");

        frame.setSize(650, 440);
        frame.setMinimumSize(new Dimension(550, 400));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(15, 15));

        JPanel content = new JPanel();
        content.setBackground(new Color(18, 20, 29));
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("ROCK PAPER SCISSORS");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Choose your move.");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(166, 174, 198));

        playerChoiceLabel = createLabel("YOU: —");
        computerChoiceLabel = createLabel("COMPUTER: —");

        resultLabel = createLabel("Ready to play?");
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        scoreLabel = createLabel("YOU 0  |  DRAWS 0  |  COMPUTER 0");

        JPanel choices = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        choices.setOpaque(false);

        addChoiceButton(choices, "✊", "Rock", new Color(124, 92, 255));
        addChoiceButton(choices, "✋", "Paper", new Color(0, 160, 150));
        addChoiceButton(choices, "✌", "Scissors", new Color(225, 100, 120));

        JButton resetButton = new JButton("Reset Scores");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(event -> resetScores());

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(25));
        content.add(playerChoiceLabel);
        content.add(Box.createVerticalStrut(6));
        content.add(computerChoiceLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(resultLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(choices);
        content.add(Box.createVerticalStrut(12));
        content.add(scoreLabel);
        content.add(Box.createVerticalStrut(18));
        content.add(resetButton);

        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));

        return label;
    }

    private void addChoiceButton(
            JPanel panel,
            String icon,
            String choice,
            Color color
    ) {

        JButton button = new JButton(
                "<html><center><font size='+3'>" + icon
                        + "</font><br>" + choice + "</center></html>"
        );

        button.setPreferredSize(new Dimension(125, 90));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));

        button.addActionListener(event -> playRound(choice));

        panel.add(button);
    }

    private void playRound(String playerChoice) {

        String[] choices = {"Rock", "Paper", "Scissors"};

        String computerChoice = choices[
                random.nextInt(choices.length)
        ];

        playerChoiceLabel.setText("YOU: " + playerChoice);
        computerChoiceLabel.setText("COMPUTER: " + computerChoice);

        if (playerChoice.equals(computerChoice)) {

            draws++;
            resultLabel.setText("It's a draw!");

        } else if (
                (playerChoice.equals("Rock")
                        && computerChoice.equals("Scissors"))
                || (playerChoice.equals("Paper")
                        && computerChoice.equals("Rock"))
                || (playerChoice.equals("Scissors")
                        && computerChoice.equals("Paper"))
        ) {

            playerScore++;
            resultLabel.setText("You win this round!");

        } else {

            computerScore++;
            resultLabel.setText("Computer wins this round!");

        }

        updateScore();
    }

    private void updateScore() {

        scoreLabel.setText(
                "YOU " + playerScore
                        + "  |  DRAWS " + draws
                        + "  |  COMPUTER " + computerScore
        );
    }

    private void resetScores() {

        playerScore = 0;
        computerScore = 0;
        draws = 0;

        playerChoiceLabel.setText("YOU: —");
        computerChoiceLabel.setText("COMPUTER: —");
        resultLabel.setText("Ready to play?");

        updateScore();
    }
}