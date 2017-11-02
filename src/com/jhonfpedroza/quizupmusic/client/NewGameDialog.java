package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.OnlineUsersPanel;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
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

    NewGameDialog(MainWindow window) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Nueva partida");
        setLocationRelativeTo(window);
        setResizable(false);

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;

        refreshButton.setIcon(ImageUtil.createImageIcon("img/refresh.png", 16, 16));
        randomButton.setIcon(ImageUtil.createImageIcon("img/random.png", 16, 16));

        setListeners();

        getOnlineUsers();

        pack();
    }

    private void onCancel() {
        dispose();
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
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setListeners() {
        buttonCancel.addActionListener(e -> onCancel());
        refreshButton.addActionListener(e -> getOnlineUsers());
        randomButton.addActionListener(e -> {
        });

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
}
