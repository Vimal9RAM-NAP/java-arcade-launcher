package dev.vimalram.arcade.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame {

    private static final int CELL_SIZE = 24;
    private static final int COLUMNS = 25;
    private static final int ROWS = 20;
    private static final int BOARD_WIDTH = CELL_SIZE * COLUMNS;
    private static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    private static final int HEADER_HEIGHT = 88;
    private static final int INITIAL_LENGTH = 4;
    private static final int TIMER_DELAY = 115;

    private final Random random = new Random();

    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;

    private final int[] snakeX = new int[COLUMNS * ROWS];
    private final int[] snakeY = new int[COLUMNS * ROWS];

    private int snakeLength;
    private int foodX;
    private int foodY;
    private int score;
    private int bestScore;

    private char direction = 'R';
    private char nextDirection = 'R';

    private boolean running;
    private boolean paused;
    private boolean gameOver;

    public void open() {
        frame = new JFrame("Java Arcade | Neon Snake");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(
                new Dimension(BOARD_WIDTH, HEADER_HEIGHT + BOARD_HEIGHT)
        );

        installKeyBindings();

        timer = new Timer(TIMER_DELAY, event -> {
            if (running && !paused && !gameOver) {
                moveSnake();
                checkCollisions();

                if (running && !gameOver) {
                    checkFood();
                }
            }

            gamePanel.repaint();
        });

        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startNewGame();
    }

    private void installKeyBindings() {
        bindDirection("UP", KeyEvent.VK_UP, 'U');
        bindDirection("DOWN", KeyEvent.VK_DOWN, 'D');
        bindDirection("LEFT", KeyEvent.VK_LEFT, 'L');
        bindDirection("RIGHT", KeyEvent.VK_RIGHT, 'R');

        bindDirection("W", KeyEvent.VK_W, 'U');
        bindDirection("S", KeyEvent.VK_S, 'D');
        bindDirection("A", KeyEvent.VK_A, 'L');
        bindDirection("D", KeyEvent.VK_D, 'R');

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pause");

        gamePanel.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (running && !gameOver) {
                    paused = !paused;
                    gamePanel.repaint();
                }
            }
        });

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "restart");

        gamePanel.getActionMap().put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (gameOver) {
                    startNewGame();
                }
            }
        });
    }

    private void bindDirection(
            String name,
            int keyCode,
            char requestedDirection
    ) {
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyCode, 0), name);

        gamePanel.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                changeDirection(requestedDirection);
            }
        });
    }

    private void changeDirection(char requested) {
        if (requested == 'U' && direction != 'D') {
            nextDirection = 'U';
        } else if (requested == 'D' && direction != 'U') {
            nextDirection = 'D';
        } else if (requested == 'L' && direction != 'R') {
            nextDirection = 'L';
        } else if (requested == 'R' && direction != 'L') {
            nextDirection = 'R';
        }
    }

    private void startNewGame() {
        snakeLength = INITIAL_LENGTH;
        score = 0;

        direction = 'R';
        nextDirection = 'R';

        paused = false;
        gameOver = false;
        running = true;

        int startX = COLUMNS / 2;
        int startY = ROWS / 2;

        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = startX - i;
            snakeY[i] = startY;
        }

        spawnFood();

        timer.start();
        gamePanel.repaint();
    }

    private void moveSnake() {
        direction = nextDirection;

        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakeY[0]--;
            case 'D' -> snakeY[0]++;
            case 'L' -> snakeX[0]--;
            case 'R' -> snakeX[0]++;
        }
    }

    private void checkCollisions() {
        if (snakeX[0] < 0 || snakeX[0] >= COLUMNS
                || snakeY[0] < 0 || snakeY[0] >= ROWS) {
            endGame();
            return;
        }

        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i]
                    && snakeY[0] == snakeY[i]) {
                endGame();
                return;
            }
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            score += 10;
            bestScore = Math.max(bestScore, score);

            if (snakeLength < snakeX.length - 1) {
                snakeLength++;
            }

            if (snakeLength >= COLUMNS * ROWS - 1) {
                endGame();
                return;
            }

            spawnFood();
        }
    }

    private void spawnFood() {
        boolean occupied;

        do {
            foodX = random.nextInt(COLUMNS);
            foodY = random.nextInt(ROWS);
            occupied = false;

            for (int i = 0; i < snakeLength; i++) {
                if (snakeX[i] == foodX && snakeY[i] == foodY) {
                    occupied = true;
                    break;
                }
            }
        } while (occupied);
    }

    private void endGame() {
        running = false;
        gameOver = true;
        timer.stop();
    }

    private class GamePanel extends JPanel {

        private final Color background = new Color(12, 15, 23);
        private final Color gridColor = new Color(27, 33, 46);
        private final Color neonGreen = new Color(91, 255, 155);
        private final Color headGreen = new Color(170, 255, 190);
        private final Color foodColor = new Color(255, 83, 112);
        private final Color mutedText = new Color(143, 155, 180);

        GamePanel() {
            setBackground(background);
            setFocusable(true);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            drawHeader(g);
            drawBoard(g);
            drawFood(g);
            drawSnake(g);

            if (paused) {
                drawOverlay("PAUSED", "Press SPACE to resume", false);
            } else if (gameOver) {
                drawOverlay("GAME OVER", "Press ENTER to play again", true);
            }

            g.dispose();
        }

        private void drawHeader(Graphics2D g) {
            g.setColor(new Color(18, 23, 34));
            g.fillRect(0, 0, BOARD_WIDTH, HEADER_HEIGHT);

            g.setColor(neonGreen);
            g.setFont(new Font("SansSerif", Font.BOLD, 21));
            g.drawString("NEON SNAKE", 22, 32);

            g.setColor(mutedText);
            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.drawString("ARCADE MODE / JAVA EDITION", 23, 53);

            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 17));
            g.drawString(String.format("%03d", score), BOARD_WIDTH - 100, 31);

            g.setColor(mutedText);
            g.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g.drawString("SCORE", BOARD_WIDTH - 99, 51);

            g.setColor(new Color(60, 71, 91));
            g.drawLine(0, HEADER_HEIGHT - 1,
                    BOARD_WIDTH, HEADER_HEIGHT - 1);
        }

        private void drawBoard(Graphics2D g) {
            g.setColor(background);
            g.fillRect(0, HEADER_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

            g.setColor(gridColor);

            for (int x = 0; x <= BOARD_WIDTH; x += CELL_SIZE) {
                g.drawLine(x, HEADER_HEIGHT,
                        x, HEADER_HEIGHT + BOARD_HEIGHT);
            }

            for (int y = 0; y <= BOARD_HEIGHT; y += CELL_SIZE) {
                g.drawLine(0, HEADER_HEIGHT + y,
                        BOARD_WIDTH, HEADER_HEIGHT + y);
            }
        }

        private void drawFood(Graphics2D g) {
            int x = foodX * CELL_SIZE;
            int y = HEADER_HEIGHT + foodY * CELL_SIZE;

            g.setColor(new Color(255, 83, 112, 45));
            g.fillOval(x - 4, y - 4, CELL_SIZE + 8, CELL_SIZE + 8);

            g.setColor(foodColor);
            g.fillOval(x + 4, y + 4,
                    CELL_SIZE - 8, CELL_SIZE - 8);
        }

        private void drawSnake(Graphics2D g) {
            for (int i = snakeLength - 1; i >= 0; i--) {
                int x = snakeX[i] * CELL_SIZE;
                int y = HEADER_HEIGHT + snakeY[i] * CELL_SIZE;

                g.setColor(i == 0 ? headGreen : neonGreen);

                g.fillRoundRect(
                        x + 2, y + 2,
                        CELL_SIZE - 4, CELL_SIZE - 4,
                        8, 8
                );

                if (i == 0) {
                    g.setColor(new Color(12, 35, 25));
                    drawEyes(g, x, y);
                }
            }
        }

        private void drawEyes(Graphics2D g, int x, int y) {
            int eyeSize = 3;

            switch (direction) {
                case 'U' -> {
                    g.fillOval(x + 6, y + 5, eyeSize, eyeSize);
                    g.fillOval(x + 15, y + 5, eyeSize, eyeSize);
                }
                case 'D' -> {
                    g.fillOval(x + 6, y + 15, eyeSize, eyeSize);
                    g.fillOval(x + 15, y + 15, eyeSize, eyeSize);
                }
                case 'L' -> {
                    g.fillOval(x + 5, y + 6, eyeSize, eyeSize);
                    g.fillOval(x + 5, y + 15, eyeSize, eyeSize);
                }
                default -> {
                    g.fillOval(x + 15, y + 6, eyeSize, eyeSize);
                    g.fillOval(x + 15, y + 15, eyeSize, eyeSize);
                }
            }
        }

        private void drawOverlay(
                String title,
                String message,
                boolean showScore
        ) {
            Graphics2D g = (Graphics2D) getGraphics();

            if (g == null) {
                return;
            }

            try {
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g.setColor(new Color(5, 8, 14, 205));
                g.fillRect(0, HEADER_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

                g.setColor(showScore ? foodColor : neonGreen);
                g.setFont(new Font("SansSerif", Font.BOLD, 32));

                FontMetrics titleMetrics = g.getFontMetrics();
                int titleX =
                        (BOARD_WIDTH - titleMetrics.stringWidth(title)) / 2;
                int centerY = HEADER_HEIGHT + BOARD_HEIGHT / 2;

                g.drawString(title, titleX, centerY - 8);

                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.PLAIN, 14));

                FontMetrics messageMetrics = g.getFontMetrics();
                int messageX =
                        (BOARD_WIDTH - messageMetrics.stringWidth(message)) / 2;

                g.drawString(message, messageX, centerY + 23);

                if (showScore) {
                    g.setColor(mutedText);
                    g.setFont(new Font("Monospaced", Font.BOLD, 12));

                    String finalScore = "FINAL SCORE: " + score
                            + "    BEST THIS SESSION: " + bestScore;

                    FontMetrics metrics = g.getFontMetrics();

                    g.drawString(
                            finalScore,
                            (BOARD_WIDTH - metrics.stringWidth(finalScore)) / 2,
                            centerY + 52
                    );
                }
            } finally {
                g.dispose();
            }
        }
    }
}