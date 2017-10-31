package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.models.Game;

import javax.swing.*;
import java.awt.event.*;

public class ChallengeDialog extends JDialog {
    private Game game;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel textLabel;
    private MainWindow mainWindow;

    ChallengeDialog(MainWindow mainWindow, Game game) {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Nuevo reto");
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);

        this.mainWindow = mainWindow;
        this.game = game;
        textLabel.setText("El usuario " + game.getPlayer1().getName() + " te ha retado, ¿Aceptas el reto?");
        pack();

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        mainWindow.acceptOrRejectChallenge(this, Game.Status.ACCEPTED);
        dispose();
    }

    private void onCancel() {
        mainWindow.acceptOrRejectChallenge(this, Game.Status.REJECTED);
        dispose();
    }

    public Game getGame() {
        return game;
    }
}
