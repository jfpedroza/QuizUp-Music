package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;


public class GameListPanel extends JPanel {
    private JList gameList;
    private JPanel content;
    private ArrayList<Game> playedGames;

    public GameListPanel(User user) {
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        playedGames = user.getPlayedGames();
        gameList.setModel(model);
        gameList.setCellRenderer(new GameCellRenderer());
    }

    public void addSelectionListener(Consumer<Game> consumer) {
        gameList.addListSelectionListener(listSelectionEvent -> {
            consumer.accept((Game) gameList.getSelectedValue());
        });
    }

    private ListModel<Game> model = new ListModel<Game>() {

        @Override
        public int getSize() {
            return playedGames.size();
        }

        @Override
        public Game getElementAt(int i) {
            return playedGames.get(i);
        }

        @Override
        public void addListDataListener(ListDataListener listDataListener) {

        }

        @Override
        public void removeListDataListener(ListDataListener listDataListener) {

        }
    };
}
