package dev.vimalram.arcade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
        setLayout(new BorderLayout(0, 20));

        createHeader();
        createGameLibrary();
        createFooter();
    }

    private void createHeader() {

        JPanel header = new JPanel();
        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBorder(
                new EmptyBorder(35, 35, 10, 35)
        );

        JLabel brand = new JLabel("JAVA ARCADE");

        brand.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        brand.setForeground(ACCENT);

        JLabel title = new JLabel(
                "Your next game starts here."
        );

        title.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );

        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "A collection of classic games, built with Java."
        );

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        subtitle.setForeground(MUTED);

        header.add(brand);
        header.add(Box.createVerticalStrut(12));
        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);
    }

    private void createGameLibrary() {

        JPanel library = new JPanel(
                new GridLayout(0, 2, 20, 20)
        );

        library.setOpaque(false);

        library.setBorder(
                new EmptyBorder(10, 35, 20, 35)
        );

        library.add(
                createGameCard(
                        "Tic-Tac-Toe",
                        "A classic strategy game.",
                        "2 PLAYERS"
                )
        );

        library.add(
                createGameCard(
                        "Snake",
                        "Eat, grow, and avoid collisions.",
                        "ARCADE"
                )
        );

        library.add(
                createGameCard(
                        "Rock Paper Scissors",
                        "Challenge the computer.",
                        "QUICK PLAY"
                )
        );

        library.add(
                createGameCard(
                        "Number Guessing",
                        "Can you find the secret number?",
                        "PUZZLE"
                )
        );

        add(library, BorderLayout.CENTER);
    }

    private JPanel createGameCard(
            String title,
            String description,
            String category
    ) {

        JPanel card = new JPanel();

        card.setBackground(PANEL);

        card.setBorder(
                new EmptyBorder(24, 24, 24, 24)
        );

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        JLabel gameTitle = new JLabel(title);

        gameTitle.setFont(
                new Font("SansSerif", Font.BOLD, 21)
        );

        gameTitle.setForeground(TEXT);

        JLabel gameDescription = new JLabel(
                "<html><div style='width:300px'>"
                        + description
                        + "</div></html>"
        );

        gameDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        gameDescription.setForeground(MUTED);

        JLabel gameCategory = new JLabel(category);

        gameCategory.setFont(
                new Font("Monospaced", Font.BOLD, 11)
        );

        gameCategory.setForeground(ACCENT);

        JButton playButton = new JButton("PLAY  →");

        playButton.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        playButton.setForeground(Color.WHITE);
        playButton.setBackground(ACCENT);
        playButton.setFocusPainted(false);
        playButton.setBorder(
                new EmptyBorder(10, 18, 10, 18)
        );

        playButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        playButton.addActionListener(event ->
                JOptionPane.showMessageDialog(
                        this,
                        title + " will be available soon!",
                        title,
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        card.add(gameTitle);
        card.add(Box.createVerticalStrut(12));
        card.add(gameDescription);
        card.add(Box.createVerticalGlue());
        card.add(gameCategory);
        card.add(Box.createVerticalStrut(16));
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

        footer.setBorder(
                new EmptyBorder(0, 35, 18, 35)
        );

        add(footer, BorderLayout.SOUTH);
    }
}