package dev.vimalram.arcade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import dev.vimalram.arcade.games.TicTacToeGame;
import dev.vimalram.arcade.games.RockPaperScissorsGame;
import dev.vimalram.arcade.games.NumberGuessingGame;
import dev.vimalram.arcade.games.SnakeGame;
import dev.vimalram.arcade.games.BreakoutGame;

public class LauncherFrame extends JFrame {

    private static final Color BACKGROUND = new Color(18, 20, 29);
    private static final Color PANEL = new Color(29, 33, 46);
    private static final Color ACCENT = new Color(124, 92, 255);
    private static final Color TEXT = new Color(239, 242, 255);
    private static final Color MUTED = new Color(166, 174, 198);
    private static final Color INPUT_BACKGROUND = new Color(35, 39, 54);
    private static final Color BUTTON_BACKGROUND = new Color(39, 43, 59);

    private final JPanel library = new JPanel();
    private final List<GameInfo> games = new ArrayList<>();
    private final List<JButton> categoryButtons = new ArrayList<>();

    private JTextField searchField;
    private JLabel resultLabel;
    private JLabel emptyLabel;

    private String activeCategory = "All Games";

    private final String[] categories = {
            "All Games",
            "Arcade",
            "Puzzle",
            "Strategy",
            "Quick Play"
    };

    public LauncherFrame() {
        setTitle("Java Arcade");
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        registerGames();
        createHeader();
        createGameLibrary();
        createFooter();
    }

    private void registerGames() {
        games.add(new GameInfo(
                "Tic-Tac-Toe",
                "A classic strategy game.",
                "2 PLAYERS",
                "Strategy"
        ));

        games.add(new GameInfo(
                "Snake",
                "Eat, grow, and avoid collisions.",
                "ARCADE",
                "Arcade"
        ));

        games.add(new GameInfo(
                "Rock Paper Scissors",
                "Challenge the computer.",
                "QUICK PLAY",
                "Quick Play"
        ));

        games.add(new GameInfo(
                "Number Guessing",
                "Can you find the secret number?",
                "PUZZLE",
                "Puzzle"
        ));

        games.add(new GameInfo(
                "Breakout",
                "Smash bricks, control the paddle, and beat the high score.",
                "ARCADE",
                "Arcade"
        ));
    }

    private void createHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(25, 35, 8, 35));

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

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        searchField.setForeground(TEXT);
        searchField.setBackground(INPUT_BACKGROUND);
        searchField.setCaretColor(TEXT);
        searchField.setBorder(new EmptyBorder(12, 15, 12, 15));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        searchField.setToolTipText(
                "Search games by name, description, or category"
        );

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(INPUT_BACKGROUND);
        searchPanel.setBorder(BorderFactory.createLineBorder(
                new Color(54, 59, 79), 1
        ));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel searchIcon = new JLabel("⌕");
        searchIcon.setFont(new Font("SansSerif", Font.BOLD, 25));
        searchIcon.setForeground(ACCENT);
        searchIcon.setBorder(new EmptyBorder(0, 12, 0, 8));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        JPanel categoryPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 9, 0)
        );
        categoryPanel.setOpaque(false);
        categoryPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String category : categories) {
            JButton button = createCategoryButton(category);
            categoryButtons.add(button);
            categoryPanel.add(button);
        }

        resultLabel = new JLabel();
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resultLabel.setForeground(MUTED);
        resultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(brand);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(17));
        header.add(searchPanel);
        header.add(Box.createVerticalStrut(15));
        header.add(categoryPanel);
        header.add(Box.createVerticalStrut(10));
        header.add(resultLabel);

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent event) {
                        filterGames();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent event) {
                        filterGames();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent event) {
                        filterGames();
                    }
                }
        );

        add(header, BorderLayout.NORTH);
    }

    private JButton createCategoryButton(String category) {
        JButton button = new JButton(category);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(TEXT);
        button.setBackground(BUTTON_BACKGROUND);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(9, 13, 9, 13));
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.addActionListener(event -> {
            activeCategory = category;
            updateCategoryButtons();
            filterGames();
        });

        return button;
    }

    private void updateCategoryButtons() {
        for (int i = 0; i < categories.length; i++) {
            JButton button = categoryButtons.get(i);
            boolean selected = categories[i].equals(activeCategory);

            button.setBackground(
                    selected ? ACCENT : BUTTON_BACKGROUND
            );

            button.setForeground(TEXT);
        }
    }

    private void createGameLibrary() {
        library.setLayout(new GridLayout(1, 2, 18, 18));
        library.setOpaque(false);
        library.setBorder(new EmptyBorder(12, 35, 20, 35));

        emptyLabel = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h2>No games found</h2>"
                        + "Try another search or select a different category."
                        + "</div></html>",
                SwingConstants.CENTER
        );

        emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        emptyLabel.setForeground(MUTED);
        emptyLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(library, BorderLayout.NORTH);
        content.add(emptyLabel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(BACKGROUND);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(10, 0));
        scrollBar.setBackground(BACKGROUND);
        scrollBar.setForeground(ACCENT);

        add(scrollPane, BorderLayout.CENTER);

        updateCategoryButtons();
        filterGames();
    }

    private void filterGames() {
        if (searchField == null || emptyLabel == null) {
            return;
        }

        String query = searchField.getText()
                .trim()
                .toLowerCase();

        library.removeAll();

        int matchCount = 0;

        for (GameInfo game : games) {
            boolean matchesCategory =
                    activeCategory.equals("All Games")
                            || game.category.equals(activeCategory);

            boolean matchesSearch =
                    game.title.toLowerCase().contains(query)
                            || game.description.toLowerCase().contains(query)
                            || game.category.toLowerCase().contains(query)
                            || game.badge.toLowerCase().contains(query);

            if (matchesCategory && matchesSearch) {
                library.add(createGameCard(
                        game.title,
                        game.description,
                        game.badge
                ));

                matchCount++;
            }
        }

        int rows = Math.max(1, (matchCount + 1) / 2);
        library.setLayout(new GridLayout(rows, 2, 18, 18));

        emptyLabel.setVisible(matchCount == 0);

        resultLabel.setText(
                matchCount
                        + (matchCount == 1 ? " game found" : " games found")
                        + "  |  "
                        + activeCategory
        );

        library.revalidate();
        library.repaint();

        emptyLabel.getParent().revalidate();
        emptyLabel.getParent().repaint();
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

        gameDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gameDescription.setForeground(MUTED);
        gameDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel gameCategory = new JLabel(category);
        gameCategory.setFont(new Font("Monospaced", Font.BOLD, 11));
        gameCategory.setForeground(ACCENT);
        gameCategory.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton playButton = new JButton("PLAY  →");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(ACCENT);
        playButton.setFocusPainted(false);
        playButton.setBorder(new EmptyBorder(9, 17, 9, 17));
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

        footer.setFont(new Font("Monospaced", Font.PLAIN, 10));
        footer.setForeground(MUTED);
        footer.setBorder(new EmptyBorder(0, 35, 16, 35));

        add(footer, BorderLayout.SOUTH);
    }

    private static class GameInfo {
        private final String title;
        private final String description;
        private final String badge;
        private final String category;

        private GameInfo(
                String title,
                String description,
                String badge,
                String category
        ) {
            this.title = title;
            this.description = description;
            this.badge = badge;
            this.category = category;
        }
    }
}
