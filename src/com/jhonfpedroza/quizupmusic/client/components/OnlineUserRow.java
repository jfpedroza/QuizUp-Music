package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;

public class OnlineUserRow extends JPanel {
    private JPanel content;
    private JButton challengeButton;
    private JLabel userNameLabel;

    OnlineUserRow(User user) {

        userNameLabel.setText(user.getName());

        challengeButton.addActionListener(actionEvent -> {
            System.out.println("Challenge");
        });

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        setMaximumSize(new Dimension(300, 40));
    }
}
