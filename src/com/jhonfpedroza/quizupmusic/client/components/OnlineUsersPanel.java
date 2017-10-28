package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.util.ArrayList;

public class OnlineUsersPanel extends JPanel {

    public OnlineUsersPanel(ArrayList<User> users) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        for (User user: users) {
            add(new OnlineUserRow(user));
        }
    }
}
