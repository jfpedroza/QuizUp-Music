package com.jhonfpedroza.quizupmusic.client;

import javax.swing.*;
import java.awt.event.*;

public class NewGameDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;

    NewGameDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Nueva partida");
        setLocationRelativeTo(null);

        buttonCancel.addActionListener(e -> onCancel());

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

    private void onCancel() {
        dispose();
    }
}
