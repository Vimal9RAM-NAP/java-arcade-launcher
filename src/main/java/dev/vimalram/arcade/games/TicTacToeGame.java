package dev.vimalram.arcade.games;

import javax.swing.*;
import java.awt.*;

public class TicTacToeGame {

    private final JButton[] cells = new JButton[9];

    private boolean xTurn = true;
    private int moves = 0;
    private boolean gameOver = false;

    private JLabel status;
    private JFrame frame;

    public void open() {

        frame = new JFrame("Tic-Tac-Toe");
        frame.setSize(420, 500);
        frame.setMinimumSize(new Dimension(350, 420));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        status = new JLabel(
                "Player X's turn",
                SwingConstants.CENTER
        );

        status.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );

        status.setBorder(
                BorderFactory.createEmptyBorder(15, 5, 5, 5)
        );

        JPanel board = new JPanel(
                new GridLayout(3, 3, 6, 6)
        );

        board.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        for (int i = 0; i < cells.length; i++) {

            JButton cell = new JButton("");

            cell.setFont(
                    new Font("SansSerif", Font.BOLD, 48)
            );

            cell.setFocusPainted(false);

            final int index = i;

            cell.addActionListener(event -> makeMove(index));

            cells[i] = cell;
            board.add(cell);
        }

        JButton restart = new JButton("New Game");

        restart.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        restart.addActionListener(event -> resetGame());

        JPanel bottom = new JPanel();
        bottom.add(restart);

        frame.add(status, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void makeMove(int index) {

        if (gameOver || !cells[index].getText().isEmpty()) {
            return;
        }

        String symbol = xTurn ? "X" : "O";

        cells[index].setText(symbol);
        cells[index].setForeground(
                xTurn
                        ? new Color(124, 92, 255)
                        : new Color(0, 190, 170)
        );

        moves++;

        if (hasWon()) {

            status.setText("Player " + symbol + " wins!");
            gameOver = true;
            disableBoard();

        } else if (moves == 9) {

            status.setText("It's a draw!");
            gameOver = true;

        } else {

            xTurn = !xTurn;

            status.setText(
                    "Player " + (xTurn ? "X" : "O") + "'s turn"
            );
        }
    }

    private boolean hasWon() {

        int[][] combinations = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };

        for (int[] combination : combinations) {

            String a = cells[combination[0]].getText();
            String b = cells[combination[1]].getText();
            String c = cells[combination[2]].getText();

            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                return true;
            }
        }

        return false;
    }

    private void disableBoard() {

        for (JButton cell : cells) {
            cell.setEnabled(false);
        }
    }

    private void resetGame() {

        xTurn = true;
        moves = 0;
        gameOver = false;

        status.setText("Player X's turn");

        for (JButton cell : cells) {

            cell.setText("");
            cell.setEnabled(true);
            cell.setForeground(Color.BLACK);
        }
    }
}