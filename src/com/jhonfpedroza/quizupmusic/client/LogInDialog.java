package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogInDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtHost;
    private JTextField txtName;

    LogInDialog(QuizUpInterface quizUp) {

        setContentPane(contentPane);
        setTitle("Iniciar sesión");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
        String host = txtHost.getText();
        String name = txtName.getText();

        if (host.equals("")) {
            JOptionPane.showMessageDialog(this, "Debe proveer un host", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Debe proveer un nombre de usuario", "Error", JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                QuizUpClient.quizUp = (QuizUpInterface) Naming.lookup("rmi://" + host + "/quizUp");
                User user = QuizUpClient.quizUp.logIn(name);
                JOptionPane.showMessageDialog(null, "Has iniciado sesión " + user.getName());
            } catch (RemoteException | NotBoundException | MalformedURLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(LogInDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
