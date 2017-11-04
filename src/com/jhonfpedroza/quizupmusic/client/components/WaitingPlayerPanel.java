package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;

public class WaitingPlayerPanel extends JPanel {
    private JLabel playerLabel;
    private JPanel content;

    public WaitingPlayerPanel(User player) {
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        playerLabel.setText(player.toString());
    }
}
