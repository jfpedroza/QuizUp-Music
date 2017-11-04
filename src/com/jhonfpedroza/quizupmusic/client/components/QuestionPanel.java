package com.jhonfpedroza.quizupmusic.client.components;

import com.jhonfpedroza.quizupmusic.models.Answer;
import com.jhonfpedroza.quizupmusic.models.Question;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionPanel extends JPanel {
    private JPanel content;
    private JTextArea questLabel;
    private JLabel numberLabel;
    private JLabel countLabel;
    private JTextArea answerALabel;
    private JTextArea answerBLabel;
    private JTextArea answerCLabel;
    private JTextArea answerDLabel;
    private JLabel pointsLabel;
    private Clip clip;
    private int count;
    private Timer timer;
    private boolean answered;
    private long startTime;

    public QuestionPanel(int number, Question question, Callback callback) {

        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        questLabel.setText(question.getQuest());
        numberLabel.setText("Pregunta " + number);

        count = 20;
        countLabel.setText("" + count);
        setPoints(0);

        JTextArea anwserLabels[] = {answerALabel, answerBLabel, answerCLabel, answerDLabel};
        String answerLetters[] = {"A", "B", "C", "D"};
        Collections.shuffle(question.getAnswers());
        for (int i = 0; i < question.getAnswers().size(); i++) {
            Answer answer = question.getAnswers().get(i);
            anwserLabels[i].setText(answerLetters[i] + ". " + answer.getText());
            int finalI = i;
            anwserLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (!answered) {
                        answered = true;
                        long endTime = 20 - (System.currentTimeMillis() - startTime) / 1000;
                        if (answer.equals(question.getCorrectAnswer())) {
                            anwserLabels[finalI].setBackground(Color.GREEN);
                        } else {
                            anwserLabels[finalI].setBackground(Color.RED);
                        }

                        Timer timer2 = new Timer(1000, null);
                        timer2.addActionListener(actionEvent -> {
                            callback.run(answer, endTime);
                        });

                        timer2.setRepeats(false);
                        timer2.start();
                    }
                }
            });
        }

        byte[] data = question.getAudio().getData();
        AudioFormat format = question.getAudio().getFormat();

        try {
            clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(format, data, 0, data.length);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(QuestionPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        timer = new Timer(1000, null);
        timer.addActionListener(actionEvent -> {
            count--;
            countLabel.setText("" + count);
            if (count == 0) {
                timer.stop();
                if (!answered) {
                    callback.run(null, 0);
                }
            } else if (count == 5) {
                countLabel.setForeground(Color.RED);
            }
        });

        timer.setRepeats(true);
        answered = false;
    }

    public void start() {
        play();
        timer.start();
        startTime = System.currentTimeMillis();
    }

    private void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.setFramePosition(0);
            } else {
                clip.start();
            }
        }
    }

    public void stop() {
        if (clip != null) clip.stop();
        timer.stop();
    }

    public void close() {
        if (clip != null) clip.close();
    }

    public void setPoints(int points) {
        pointsLabel.setText("" + points);
    }

    public interface Callback {

        void run(Answer answer, long time);
    }
}
