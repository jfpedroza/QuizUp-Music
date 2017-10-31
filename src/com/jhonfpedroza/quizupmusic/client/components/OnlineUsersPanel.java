package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class OnlineUsersPanel extends JPanel {

    private ArrayList<OnlineUserRow> rows;

    public OnlineUsersPanel(ArrayList<User> users) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        rows = new ArrayList<>();
        for (User user: users) {
            OnlineUserRow row = new OnlineUserRow(user);
            add(row);
            rows.add(row);
        }
    }

    public void addChallengeListener(Consumer<User> consumer) {
        rows.forEach(row -> row.addChallengeListener(consumer));
    }
}
