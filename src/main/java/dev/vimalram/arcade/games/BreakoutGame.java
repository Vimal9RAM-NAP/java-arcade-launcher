package dev.vimalram.arcade.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class BreakoutGame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;

    private int paddleX = 340;
    private final int paddleY = 520;
    private final int paddleWidth = 120;
    private final int paddleHeight = 14;

    private int ballX = 390;
    private int ballY = 400;
    private final int ballSize = 16;

    private int ballSpeedX = 4;
    private int ballSpeedY = -4;

    private int score;
    private int lives = 3;

    private boolean running;
    private boolean gameOver;
    private boolean won;

    public void open() {
        frame = new JFrame("Java Arcade | Breakout");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        installControls();

        timer = new Timer(16, event -> {
            if (running && !gameOver && !won) {
                updateGame();
            }

            gamePanel.repaint();
        });

        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        running = true;
        timer.start();
    }

    private void installControls() {
        bindKey("LEFT", KeyEvent.VK_LEFT, -1);
        bindKey("RIGHT", KeyEvent.VK_RIGHT, 1);
        bindKey("A", KeyEvent.VK_A, -1);
        bindKey("D", KeyEvent.VK_D, 1);

        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "restart");

        gamePanel.getActionMap().put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (gameOver || won) {
                    restartGame();
                }
            }
        });
    }

    private void bindKey(String name, int keyCode, int direction) {
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyCode, 0), name);

        gamePanel.getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                paddleX += direction * 25;
                paddleX = Math.max(0, Math.min(WIDTH - paddleWidth, paddleX));
                gamePanel.repaint();
            }
        });
    }

    private void updateGame() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0 || ballX + ballSize >= WIDTH) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballY <= 70) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballY + ballSize >= paddleY
                && ballY + ballSize <= paddleY + paddleHeight + 8
                && ballX + ballSize >= paddleX
                && ballX <= paddleX + paddleWidth
                && ballSpeedY > 0) {
            ballSpeedY = -Math.abs(ballSpeedY);
        }

        if (ballY > HEIGHT) {
            lives--;

            if (lives <= 0) {
                gameOver = true;
                running = false;
            } else {
                resetBall();
            }
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2;
        ballY = HEIGHT - 150;
        ballSpeedX = 4;
        ballSpeedY = -4;
    }

    private void restartGame() {
        paddleX = (WIDTH - paddleWidth) / 2;
        score = 0;
        lives = 3;
        gameOver = false;
        won = false;
        running = true;

        resetBall();
        timer.start();
        gamePanel.repaint();
    }

    private class GamePanel extends JPanel {

        private final Color background = new Color(7, 16, 38);
        private final Color neon = new Color(91, 255, 155);
        private final Color muted = new Color(143, 155, 180);

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g.setColor(background);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            drawHeader(g);
            drawPaddle(g);
            drawBall(g);

            if (gameOver) {
                drawOverlay(g, "GAME OVER", "Press ENTER to restart");
            } else if (won) {
                drawOverlay(g, "YOU WIN!", "Press ENTER to play again");
            }

            g.dispose();
        }

        private void drawHeader(Graphics2D g) {
            g.setColor(new Color(18, 23, 34));
            g.fillRect(0, 0, WIDTH, 60);

            g.setColor(neon);
            g.setFont(new Font("SansSerif", Font.BOLD, 22));
            g.drawString("BREAKOUT", 24, 37);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 15));
            g.drawString("SCORE: " + score, 500, 35);
            g.drawString("LIVES: " + lives, 660, 35);
        }

        private void drawPaddle(Graphics2D g) {
            g.setColor(neon);
            g.fillRoundRect(
                    paddleX, paddleY,
                    paddleWidth, paddleHeight,
                    12, 12
            );
        }

        private void drawBall(Graphics2D g) {
            g.setColor(new Color(255, 83, 112, 65));
            g.fillOval(ballX - 5, ballY - 5,
                    ballSize + 10, ballSize + 10);

            g.setColor(new Color(255, 83, 112));
            g.fillOval(ballX, ballY, ballSize, ballSize);
        }

        private void drawOverlay(
                Graphics2D g,
                String title,
                String message
        ) {
            g.setColor(new Color(5, 8, 14, 210));
            g.fillRect(0, 60, WIDTH, HEIGHT - 60);

            g.setColor(neon);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));

            FontMetrics titleMetrics = g.getFontMetrics();
            int titleX =
                    (WIDTH - titleMetrics.stringWidth(title)) / 2;

            g.drawString(title, titleX, HEIGHT / 2);

            g.setColor(muted);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));

            FontMetrics messageMetrics = g.getFontMetrics();
            int messageX =
                    (WIDTH - messageMetrics.stringWidth(message)) / 2;

            g.drawString(message, messageX, HEIGHT / 2 + 35);
        }
    }
}