package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class OnlineUserRow extends JPanel {
    private JPanel content;
    private JButton challengeButton;
    private JLabel userNameLabel;
    private User user;

    OnlineUserRow(User user) {
        this.user = user;

        userNameLabel.setText(user.getName());

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        setMaximumSize(new Dimension(300, 40));
    }

    void addChallengeListener(Consumer<User> consumer) {
        challengeButton.addActionListener(actionEvent -> {
            consumer.accept(user);
        });
    }
}
