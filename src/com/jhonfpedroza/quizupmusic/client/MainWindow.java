package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private JPanel contentPane;
    private JButton newGameButton;
    private JLabel nameLabel;
    private QuizUpInterface quizUp;
    private User currentUser;

    MainWindow() {
        setContentPane(contentPane);
        setTitle("QuizUp Music");
        setMinimumSize(new Dimension(640, 480));
        setSize(new Dimension(800, 600));

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;
        nameLabel.setText("Usuario: " + currentUser.getName());

        initMenus();

        // call onExit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        newGameButton.addActionListener(actionEvent -> {

        });
    }

    private void onExit() {
        try {
            quizUp.logOut(currentUser);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
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
}
