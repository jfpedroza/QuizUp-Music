package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;

public class OnlineUserCellRenderer implements ListCellRenderer {
    private JPanel content;
    private JButton challengeButton;
    private JLabel userNameLabel;

    public OnlineUserCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object obj, int index, boolean isSelected, boolean cellHasFocus) {
        User user = (User)obj;

        userNameLabel.setText(user.getName());

        challengeButton.addActionListener(actionEvent -> {

        });

        return content;
    }
}
