package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
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
        GameListModel model = new GameListModel(playedGames);

        gameList.setModel(model);
        gameList.setCellRenderer(new GameCellRenderer());
    }

    public void addSelectionListener(Consumer<Game> consumer) {
        gameList.addListSelectionListener(listSelectionEvent -> {
            consumer.accept((Game) gameList.getSelectedValue());
        });
    }

    public void updateList(ArrayList<Game> games) {

        playedGames = games;
        GameListModel model = new GameListModel(playedGames);
        gameList.setModel(model);
    }

    private class GameListModel extends AbstractListModel<Game> {

        ArrayList<Game> games;

        GameListModel(ArrayList<Game> games) {
            this.games = games;
        }

        @Override
        public int getSize() {
            return games.size();
        }

        @Override
        public Game getElementAt(int i) {
            return games.get(i);
        }
    };
}
