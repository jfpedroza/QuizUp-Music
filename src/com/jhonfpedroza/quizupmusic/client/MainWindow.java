package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.GameDetailPanel;
import com.jhonfpedroza.quizupmusic.client.components.GameListPanel;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private JPanel contentPane;
    private JButton newGameButton;
    private JLabel nameLabel;
    private JPanel contentPanel;
    private GameListPanel gameListPanel;
    private GameDetailPanel gameDetailPanel;
    private QuizUpInterface quizUp;
    private User currentUser;
    private BackgroundTasks tasks;
    private NewGameDialog newGameDialog;
    private ArrayList<ChallengeDialog> challengeDialogs;

    MainWindow() {
        setContentPane(contentPane);
        setTitle("QuizUp Music");
        setMinimumSize(new Dimension(640, 480));
        setSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;
        nameLabel.setText("Usuario: " + currentUser.getName());
        challengeDialogs = new ArrayList<>();

        initMenus();

        contentPanel.setLayout(new GridLayout(0, 2));
        gameListPanel = new GameListPanel(currentUser);
        gameDetailPanel = new GameDetailPanel();
        contentPanel.add(gameListPanel);
        contentPanel.add(gameDetailPanel);

        setListeners();

        tasks = new BackgroundTasks();
        tasks.execute();
    }

    private void setListeners() {

        // call onExit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        newGameButton.addActionListener(actionEvent -> {
            newGameDialog = new NewGameDialog(this);
            newGameDialog.setChallengeListener(MainWindow.this::onChallenge);
            newGameDialog.setVisible(true);
        });

        gameListPanel.addSelectionListener(gameDetailPanel::setGame);
    }

    private void onExit() {
        try {
            quizUp.logOut(currentUser);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            tasks.cancel(true);
        }

        dispose();
    }

    private void onChallenge(User user) {
        try {
            Game challenge = quizUp.challenge(currentUser, user);
            tasks.watchGame(challenge, game -> {
                if (game.getStatus() == Game.Status.ACCEPTED) {
                    quizUp.setGameStatus(challenge, Game.Status.ONGOING);
                    newGameDialog.dispose();
                    game = quizUp.getGame(game.getId());
                    new GameWindow(this, game).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "El reto a " + game.getPlayer2().getName() + " fue rechazado", "Reto rechadado", JOptionPane.INFORMATION_MESSAGE);
                }
            }, Game.Status.ACCEPTED, Game.Status.REJECTED);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void acceptOrRejectChallenge(ChallengeDialog dialog, Game.Status status) {
        try {
            quizUp.setGameStatus(dialog.getGame(), status);
            challengeDialogs.remove(dialog);
            if (status == Game.Status.ACCEPTED) {
                tasks.watchGame(dialog.getGame(), game -> {
                    new GameWindow(this, game).setVisible(true);
                }, Game.Status.ONGOING);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initMenus() {

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        exitItem.addActionListener(actionEvent -> {
            onExit();
        });

        fileMenu.add(exitItem);
    }

    private class BackgroundTasks extends SwingWorker<Void, Object> {
        private Logger logger = Logger.getLogger(BackgroundTasks.class.getName());

        private List<WatchedGame> watchedGames;

        BackgroundTasks() {
            watchedGames = Collections.synchronizedList(new ArrayList<>());
        }

        void watchGame(Game game, WatchCallback callback, Game.Status... statuses) {
            watchedGames.add(new WatchedGame(game, statuses, callback));
            logger.log(Level.INFO, String.format("Watching game %s for statuses %s", game, Arrays.toString(statuses)));
        }

        @Override
        protected Void doInBackground() throws Exception {

            while (!isCancelled()) {
                Thread.sleep(1000);
                try {
                    processChallenges();
                    processWatchedGames();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.SEVERE, null, ex);
                }
            }

            return null;
        }

        @Override
        protected void process(List<Object> list) {
            for (Object object: list) {
                if (object instanceof ChallengeDialog) {
                    ((ChallengeDialog)object).setVisible(true);
                }

                if (object instanceof WatchedGame) {
                    try {
                        ((WatchedGame)object).run();
                    } catch (RemoteException ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        logger.log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        private void processChallenges() throws RemoteException {
            ArrayList<Game> challenges = quizUp.getChallenges(currentUser);
            for (Game challenge: challenges) {
                if (challengeDialogs.stream().anyMatch(challengeDialog -> challengeDialog.getGame().equals(challenge))) {
                    continue;
                }

                logger.log(Level.INFO, "Challenge received from " + challenge.getPlayer1());
                ChallengeDialog dialog = new ChallengeDialog(MainWindow.this, challenge);
                challengeDialogs.add(dialog);
                publish(dialog);
            }
        }

        private void processWatchedGames() throws RemoteException {
            ArrayList<WatchedGame> toRemove = new ArrayList<>();
            for (WatchedGame watchedGame: watchedGames) {
                Game game = quizUp.getGame(watchedGame.game.getId());
                if (Arrays.stream(watchedGame.statuses).anyMatch(status -> status == game.getStatus())) {
                    logger.log(Level.INFO, String.format("Status match for game %s: %s, running callback", game, game.getStatus()));
                    watchedGame.game = game;
                    publish(watchedGame);
                    toRemove.add(watchedGame);
                }
            }

            watchedGames.removeAll(toRemove);
            toRemove.clear();
        }
    }

    private class WatchedGame {
        Game game;
        Game.Status[] statuses;
        WatchCallback callback;

        WatchedGame(Game game, Game.Status[] statuses, WatchCallback callback) {
            this.game = game;
            this.statuses = statuses;
            this.callback = callback;
        }

        void run() throws RemoteException {
            callback.run(game);
        }
    }

    private interface WatchCallback {

        void run(Game game) throws RemoteException;
    }
}
