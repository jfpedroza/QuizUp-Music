package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {
    private JLabel player1Label;
    private JPanel content;
    private JLabel player2Label;
    private JLabel points1Label;
    private JLabel points2Label;

    public EndGamePanel() {
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
    }

    public void setGame(Game game) {
        int points1 = game.getPoints(game.getPlayer1());
        int points2 = game.getPoints(game.getPlayer2());

        player1Label.setText(game.getPlayer1().toString());
        points1Label.setText("" + points1);

        player2Label.setText(game.getPlayer2().toString());
        points2Label.setText("" + points2);

        if (points1 > points2) {
            player1Label.setForeground(Color.GREEN);
            points1Label.setForeground(Color.GREEN);
            player2Label.setForeground(Color.RED);
            points2Label.setForeground(Color.RED);
        } else if (points2 > points1){
            player2Label.setForeground(Color.GREEN);
            points2Label.setForeground(Color.GREEN);
            player1Label.setForeground(Color.RED);
            points1Label.setForeground(Color.RED);
        }
    }
}
