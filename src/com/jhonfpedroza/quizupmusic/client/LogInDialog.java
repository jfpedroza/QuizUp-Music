package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogInDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtHost;
    private JTextField txtName;

    LogInDialog() {

        setContentPane(contentPane);
        setTitle("Iniciar sesión");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

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

        loadConfig();
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
                QuizUpClient.currentUser = QuizUpClient.quizUp.logIn(name);
                if (QuizUpClient.currentUser != null) {
                    saveConfig();

                    EventQueue.invokeLater(() -> {
                        MainWindow window = new MainWindow();
                        window.setVisible(true);
                    });

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "El usuario ya ha iniciado sesión", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException | NotBoundException | MalformedURLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(LogInDialog.class.getName()).log(Level.SEVERE, null, ex);
                dispose();
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void loadConfig() {
        File file = new File("config.properties");
        if (file.exists()) {
            InputStream in = null;

            try {
                in = new FileInputStream(file);

                Properties prop = new Properties();
                prop.load(in);
                txtHost.setText(prop.getProperty("host"));
                txtName.setText(prop.getProperty("name"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void saveConfig() {
        OutputStream out = null;
        try {
            out = new FileOutputStream("config.properties");
            Properties prop = new Properties();
            prop.setProperty("host", txtHost.getText());
            prop.setProperty("name", txtName.getText());

            prop.store(out, null);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
