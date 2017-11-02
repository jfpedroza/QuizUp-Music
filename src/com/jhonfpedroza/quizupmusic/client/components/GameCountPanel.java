package com.jhonfpedroza.quizupmusic.client.components;

import javax.swing.*;
import java.awt.*;

public class GameCountPanel extends JPanel {
    private JLabel countLabel;
    private JPanel content;
    private int count;

    public GameCountPanel(Runnable timeoutCallback) {
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        count = 5;
        Timer timer = new Timer(1000, null);
        timer.addActionListener(actionEvent -> {
            count--;
            countLabel.setText("" + count + "s");
            if (count == 0) {
                timer.stop();
                timeoutCallback.run();
            }
        });

        timer.setRepeats(true);
        timer.start();
    }
}
