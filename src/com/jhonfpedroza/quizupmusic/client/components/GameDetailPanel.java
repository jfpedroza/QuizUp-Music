package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.Game;

import javax.swing.*;
import java.awt.*;

import static com.sun.javafx.fxml.expression.Expression.add;

public class GameDetailPanel extends JPanel {
    private JPanel content;
    private JLabel p1Label;
    private JLabel p2Label;
    private JLabel dateLabel;
    private Game game;

    public GameDetailPanel() {
        setLayout(new BorderLayout());
        game = null;
    }

    public void setGame(Game game) {
        if (this.game == null) {
            add(content, BorderLayout.CENTER);
        }

        this.game = game;
        p1Label.setText(game.getPlayer1().getName());
        p2Label.setText(game.getPlayer2().getName());
        dateLabel.setText(game.getDate().toString());
        repaint();
    }
}
