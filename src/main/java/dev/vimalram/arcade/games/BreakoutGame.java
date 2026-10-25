package dev.vimalram.arcade.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class BreakoutGame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final Color BACKGROUND = new Color(7, 16, 38);
    private static final Color HEADER_BACKGROUND = new Color(12, 27, 58);
    private static final Color NEON = new Color(91, 255, 155);
    private static final Color MUTED = new Color(143, 155, 180);
    private static final Color BALL_COLOR = new Color(255, 83, 112);

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
        frame.getContentPane().setBackground(BACKGROUND);

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        installControls();

        timer = new Timer(16, event -> {
            if (running && !gameOver && !won) {
                updateGame();
            }

            gamePanel.repaint();
        });

        frame.setContentPane(gamePanel);
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
                if (!running || gameOver || won) {
                    return;
                }

                paddleX += direction * 25;
                paddleX = Math.max(0, Math.min(WIDTH - paddleWidth, paddleX));
                gamePanel.repaint();
            }
        });
    }

    private void updateGame() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0) {
            ballX = 0;
            ballSpeedX = Math.abs(ballSpeedX);
        } else if (ballX + ballSize >= WIDTH) {
            ballX = WIDTH - ballSize;
            ballSpeedX = -Math.abs(ballSpeedX);
        }

        if (ballY <= 60) {
            ballY = 60;
            ballSpeedY = Math.abs(ballSpeedY);
        }

        if (ballY + ballSize >= paddleY
                && ballY + ballSize <= paddleY + paddleHeight + 8
                && ballX + ballSize >= paddleX
                && ballX <= paddleX + paddleWidth
                && ballSpeedY > 0) {

            ballY = paddleY - ballSize;
            ballSpeedY = -Math.abs(ballSpeedY);

            double hitPosition =
                    (ballX + ballSize / 2.0 - (paddleX + paddleWidth / 2.0))
                            / (paddleWidth / 2.0);

            ballSpeedX = (int) Math.round(hitPosition * 5);

            if (ballSpeedX == 0) {
                ballSpeedX = 1;
            }
        }

        if (ballY > HEIGHT) {
            lives--;

            if (lives <= 0) {
                gameOver = true;
                running = false;
                timer.stop();
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

        GamePanel() {
            setBackground(BACKGROUND);
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g.setColor(BACKGROUND);
            g.fillRect(0, 0, getWidth(), getHeight());

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
            g.setColor(HEADER_BACKGROUND);
            g.fillRect(0, 0, getWidth(), 60);

            g.setColor(NEON);
            g.setFont(new Font("SansSerif", Font.BOLD, 22));
            g.drawString("BREAKOUT", 24, 37);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 15));
            g.drawString("SCORE: " + score, 500, 35);
            g.drawString("LIVES: " + lives, 660, 35);
        }

        private void drawPaddle(Graphics2D g) {
            g.setColor(NEON);
            g.fillRoundRect(
                    paddleX,
                    paddleY,
                    paddleWidth,
                    paddleHeight,
                    12,
                    12
            );
        }

        private void drawBall(Graphics2D g) {
            g.setColor(new Color(255, 83, 112, 65));
            g.fillOval(
                    ballX - 5,
                    ballY - 5,
                    ballSize + 10,
                    ballSize + 10
            );

            g.setColor(BALL_COLOR);
            g.fillOval(ballX, ballY, ballSize, ballSize);
        }

        private void drawOverlay(
                Graphics2D g,
                String title,
                String message
        ) {
            g.setColor(new Color(5, 8, 20, 220));
            g.fillRect(0, 60, getWidth(), getHeight() - 60);

            g.setColor(NEON);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));

            FontMetrics titleMetrics = g.getFontMetrics();
            int titleX =
                    (getWidth() - titleMetrics.stringWidth(title)) / 2;

            g.drawString(title, titleX, getHeight() / 2);

            g.setColor(MUTED);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));

            FontMetrics messageMetrics = g.getFontMetrics();
            int messageX =
                    (getWidth() - messageMetrics.stringWidth(message)) / 2;

            g.drawString(message, messageX, getHeight() / 2 + 35);
        }
    }
}