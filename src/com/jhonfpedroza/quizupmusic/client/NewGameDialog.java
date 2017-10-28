package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.GameCellRenderer;
import com.jhonfpedroza.quizupmusic.client.components.OnlineUserCellRenderer;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton refreshButton;
    private JButton randomButton;
    private JList onlineList;
    private QuizUpInterface quizUp;
    private User currentUser;

    NewGameDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Nueva partida");
        setLocationRelativeTo(null);
        pack();
        setResizable(false);

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;

        refreshButton.setIcon(ImageUtil.createImageIcon("img/refresh.png", 16, 16));
        randomButton.setIcon(ImageUtil.createImageIcon("img/random.png", 16, 16));

        onlineList.setCellRenderer(new OnlineUserCellRenderer());

        setListeners();
        getOnlineUsers();
    }

    private void onCancel() {
        dispose();
    }

    private void getOnlineUsers() {
        try {
            ArrayList<User> onlineUsers = quizUp.getOnlineUsers();
            onlineUsers.remove(currentUser);
            onlineList.setModel(new OnlineUsersModel(onlineUsers));
            onlineList.repaint();
        } catch (RemoteException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
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

    private class OnlineUsersModel implements ListModel<User> {

        ArrayList<User> users;

        OnlineUsersModel(ArrayList<User> users) {
            this.users = users;
        }

        @Override
        public int getSize() {
            return users.size();
        }

        @Override
        public User getElementAt(int i) {
            return users.get(i);
        }

        @Override
        public void addListDataListener(ListDataListener listDataListener) {

        }

        @Override
        public void removeListDataListener(ListDataListener listDataListener) {

        }
    };
}
