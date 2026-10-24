package dev.vimalram.arcade;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Game {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.err.println(
                    "Could not load system theme."
                );
            }

            new LauncherFrame().setVisible(true);
        });
    }
}