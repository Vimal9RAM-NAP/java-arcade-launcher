package dev.vimalram.arcade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import dev.vimalram.arcade.games.TicTacToeGame;
import dev.vimalram.arcade.games.RockPaperScissorsGame;
import dev.vimalram.arcade.games.NumberGuessingGame;
import dev.vimalram.arcade.games.SnakeGame;
import dev.vimalram.arcade.games.BreakoutGame;

public class LauncherFrame extends JFrame {

    private static final Color BACKGROUND =
            new Color(18, 20, 29);

    private static final Color PANEL =
            new Color(29, 33, 46);

    private static final Color ACCENT =
            new Color(124, 92, 255);

    private static final Color TEXT =
            new Color(239, 242, 255);

    private static final Color MUTED =
            new Color(166, 174, 198);

    public LauncherFrame() {
        setTitle("Java Arcade");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        createHeader();
        createGameLibrary();
        createFooter();
    }

    private void createHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(28, 35, 12, 35));

        JLabel brand = new JLabel("JAVA ARCADE");
        brand.setFont(new Font("SansSerif", Font.BOLD, 13));
        brand.setForeground(ACCENT);

        JLabel title = new JLabel("Your next game starts here.");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "A collection of classic games, built with Java."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(MUTED);

        header.add(brand);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);
    }

    private void createGameLibrary() {
        JPanel library = new JPanel(
                new GridLayout(0, 2, 18, 18)
        );

        library.setOpaque(false);
        library.setBorder(new EmptyBorder(12, 35, 20, 35));

        library.add(createGameCard(
                "Tic-Tac-Toe",
                "A classic strategy game.",
                "2 PLAYERS"
        ));

        library.add(createGameCard(
                "Snake",
                "Eat, grow, and avoid collisions.",
                "ARCADE"
        ));

        library.add(createGameCard(
                "Rock Paper Scissors",
                "Challenge the computer.",
                "QUICK PLAY"
        ));

        library.add(createGameCard(
                "Number Guessing",
                "Can you find the secret number?",
                "PUZZLE"
        ));

        library.add(createGameCard(
                "Breakout",
                "Smash bricks, control the paddle, and beat the high score.",
                "ARCADE"
        ));

        add(library, BorderLayout.CENTER);
    }

    private JPanel createGameCard(
            String title,
            String description,
            String category
    ) {
        JPanel card = new JPanel();
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(20, 22, 20, 22));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel gameTitle = new JLabel(title);
        gameTitle.setFont(new Font("SansSerif", Font.BOLD, 21));
        gameTitle.setForeground(TEXT);
        gameTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel gameDescription = new JLabel(
                "<html><div style='width:260px'>"
                        + description
                        + "</div></html>"
        );

        gameDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        gameDescription.setForeground(MUTED);
        gameDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel gameCategory = new JLabel(category);
        gameCategory.setFont(
                new Font("Monospaced", Font.BOLD, 11)
        );
        gameCategory.setForeground(ACCENT);
        gameCategory.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton playButton = new JButton("PLAY  →");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(ACCENT);
        playButton.setFocusPainted(false);
        playButton.setBorder(
                new EmptyBorder(9, 17, 9, 17)
        );
        playButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        playButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        playButton.addActionListener(event -> {
            switch (title) {
                case "Tic-Tac-Toe" ->
                        new TicTacToeGame().open();

                case "Rock Paper Scissors" ->
                        new RockPaperScissorsGame().open();

                case "Number Guessing" ->
                        new NumberGuessingGame().open();

                case "Snake" ->
                        new SnakeGame().open();

                case "Breakout" ->
                        new BreakoutGame().open();

                default ->
                        JOptionPane.showMessageDialog(
                                this,
                                title + " will be available soon!",
                                title,
                                JOptionPane.INFORMATION_MESSAGE
                        );
            }
        });

        card.add(gameTitle);
        card.add(Box.createVerticalStrut(9));
        card.add(gameDescription);
        card.add(Box.createVerticalStrut(7));
        card.add(gameCategory);
        card.add(Box.createVerticalStrut(12));
        card.add(playButton);

        return card;
    }

    private void createFooter() {
        JLabel footer = new JLabel(
                "JAVA ARCADE  |  BUILT WITH JAVA SWING"
        );

        footer.setFont(
                new Font("Monospaced", Font.PLAIN, 10)
        );
        footer.setForeground(MUTED);
        footer.setBorder(new EmptyBorder(0, 35, 16, 35));

        add(footer, BorderLayout.SOUTH);
    }
}