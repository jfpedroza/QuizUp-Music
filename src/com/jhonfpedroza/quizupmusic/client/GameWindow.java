package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.GameCountPanel;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JDialog {
    private JPanel contentPane;
    private JLabel p1Label;
    private JLabel p2Label;
    private JPanel contentPanel;
    private Game game;
    private QuizUpInterface quizUp;
    private User currentUser;

    GameWindow(MainWindow window, Game game) {
        super(window);
        this.game = game;
        setContentPane(contentPane);
        setTitle("Juego " + game.getPlayer1() + " vs " + game.getPlayer2());
        setMinimumSize(new Dimension(300, 450));
        setModal(true);
        setLocationRelativeTo(window);

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;

        p1Label.setText(game.getPlayer1().toString());
        p2Label.setText(game.getPlayer2().toString());

        contentPanel.add(new GameCountPanel(() -> {
            contentPanel.removeAll();
            contentPanel.revalidate();
            contentPanel.repaint();
        }), BorderLayout.CENTER);
    }
}
