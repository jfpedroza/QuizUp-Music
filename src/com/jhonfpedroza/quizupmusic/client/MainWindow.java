package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private JPanel contentPane;
    private QuizUpInterface quizUp;
    private User currentUser;

    MainWindow() {
        setContentPane(contentPane);
        setTitle("QuizUp Music");
        setMinimumSize(new Dimension(800, 600));

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;

        // call onExit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        initMenus();
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

        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        exitItem.addActionListener(actionEvent -> {
            onExit();
        });

        JMenu fileMenu = new JMenu();
        fileMenu.add(exitItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
}
