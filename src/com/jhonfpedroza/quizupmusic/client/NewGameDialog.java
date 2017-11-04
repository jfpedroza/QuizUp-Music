package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.OnlineUsersPanel;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton refreshButton;
    private JButton randomButton;
    private JPanel usersPanel;
    private JScrollPane usersScrollPane;
    private QuizUpInterface quizUp;
    private User currentUser;
    private OnlineUsersPanel oup;
    private Consumer<User> challengeListener;
    private ImageIcon randomIcon;
    private ImageIcon cancelIcon;
    private boolean isSearchingGame;
    private Game game;
    private Consumer<Game> cancelRandomListener;

    NewGameDialog(MainWindow window) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Nueva partida");
        setLocationRelativeTo(window);
        setResizable(false);

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;

        randomIcon = ImageUtil.createImageIcon("img/random.png", 16, 16);
        cancelIcon = ImageUtil.createImageIcon("img/cancel.png", 16, 16);

        refreshButton.setIcon(ImageUtil.createImageIcon("img/refresh.png", 16, 16));
        randomButton.setIcon(randomIcon);
        isSearchingGame = false;

        setListeners();

        getOnlineUsers();

        pack();
    }

    private void onCancel() {
        if (game != null) {
            cancelRandomListener.accept(game);
        }
        dispose();
    }

    void setGame(Game game) {
        this.game = game;
    }

    private void getOnlineUsers() {
        try {
            ArrayList<User> onlineUsers = quizUp.getOnlineUsers();
            onlineUsers.remove(currentUser);
            EventQueue.invokeLater(() -> {
                usersPanel.removeAll();
                oup = new OnlineUsersPanel(onlineUsers);
                usersScrollPane = new JScrollPane(oup);
                usersPanel.add(usersScrollPane, BorderLayout.CENTER);
                usersPanel.revalidate();
                usersPanel.repaint();
                setOupListeners();
            });
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(NewGameDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setListeners() {
        buttonCancel.addActionListener(e -> onCancel());
        refreshButton.addActionListener(e -> getOnlineUsers());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setOupListeners() {
        oup.addChallengeListener(user -> {
            if (challengeListener != null) {
                challengeListener.accept(user);
            }
        });
    }

    void setChallengeListener(Consumer<User> challengeListener) {
        this.challengeListener = challengeListener;
    }

    void setRandomListener(Runnable randomListener, Consumer<Game> cancelRandomListener) {
        this.cancelRandomListener = cancelRandomListener;
        randomButton.addActionListener(e -> {
            if (!isSearchingGame) {
                isSearchingGame = true;
                randomListener.run();
                randomButton.setIcon(cancelIcon);
                randomButton.setText("Cancelar");
            } else {
                isSearchingGame = false;
                cancelRandomListener.accept(game);
                randomButton.setIcon(randomIcon);
                randomButton.setText("Aleatorio");
            }
        });
    }
}
