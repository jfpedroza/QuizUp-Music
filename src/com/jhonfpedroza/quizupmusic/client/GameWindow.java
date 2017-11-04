package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.client.components.EndGamePanel;
import com.jhonfpedroza.quizupmusic.client.components.GameCountPanel;
import com.jhonfpedroza.quizupmusic.client.components.QuestionPanel;
import com.jhonfpedroza.quizupmusic.client.components.WaitingPlayerPanel;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.Question;
import com.jhonfpedroza.quizupmusic.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameWindow extends JDialog {
    private JPanel contentPane;
    private JLabel p1Label;
    private JLabel p2Label;
    private JPanel contentPanel;
    private QuizUpInterface quizUp;
    private User currentUser;
    private int questionNumber;
    private ArrayList<QuestionPanel> questionPanels;
    private Game game;
    private CardLayout cardLayout;
    private MainWindow.WatchCallback callback;
    private EndGamePanel endGamePanel;
    private Runnable closeCallback;

    GameWindow(MainWindow window, Game game, MainWindow.WatchCallback callback, Runnable closeCallback) {
        super(window);
        setContentPane(contentPane);
        setTitle("Juego " + game.getPlayer1() + " vs " + game.getPlayer2());
        setMinimumSize(new Dimension(300, 400));
        setModal(true);
        setLocationRelativeTo(window);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (GameWindow.this.game.getStatus() == Game.Status.FINISHED) {
                    onClose();
                }
            }
        });

        quizUp = QuizUpClient.quizUp;
        currentUser = QuizUpClient.currentUser;
        this.game = game;
        this.callback = callback;
        this.closeCallback = closeCallback;

        p1Label.setText(game.getPlayer1().toString());
        p2Label.setText(game.getPlayer2().toString());

        cardLayout = (CardLayout) contentPanel.getLayout();
        addLayouts();
    }

    private void onClose() {
        questionPanels.forEach(QuestionPanel::stop);
        questionPanels.forEach(QuestionPanel::close);
        dispose();
        closeCallback.run();
    }

    private void addLayouts() {

        questionNumber = -1;
        contentPanel.add(new GameCountPanel(() -> {
            questionNumber++;
            cardLayout.show(contentPanel, "Question" + questionNumber);
            questionPanels.get(questionNumber).start();
        }), "CountPanel");

        questionPanels = new ArrayList<>();
        for (int i = 0; i < game.getQuestions().size(); i++) {
            Question question = game.getQuestions().get(i);
            QuestionPanel questionPanel = new QuestionPanel(i + 1, question, (answer, time) -> {
                try {
                    int points = quizUp.setAnswer(game, currentUser, question, answer, time);

                    questionPanels.get(questionNumber).stop();
                    questionPanels.get(questionNumber).setPoints(points);
                    questionNumber++;
                    if (questionNumber < game.getQuestions().size()) {
                        cardLayout.show(contentPanel, "Question" + questionNumber);
                        questionPanels.get(questionNumber).start();
                        questionPanels.get(questionNumber).setPoints(points);
                    } else {
                        quizUp.finishGame(game, currentUser);
                        cardLayout.show(contentPanel, "WaitingPanel");
                        callback.run(game);
                    }
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            questionPanels.add(questionPanel);
            contentPanel.add(questionPanel, "Question" + i);
        }

        User otherPlayer;
        if (currentUser.equals(game.getPlayer1())) {
            otherPlayer = game.getPlayer2();
        } else {
            otherPlayer = game.getPlayer1();
        }

        contentPanel.add(new WaitingPlayerPanel(otherPlayer), "WaitingPanel");

        endGamePanel = new EndGamePanel();
        contentPanel.add(endGamePanel, "EndGamePanel");
    }

    void setWinner(Game game) {
        this.game = game;
        endGamePanel.setGame(game);
        cardLayout.show(contentPanel, "EndGamePanel");
    }
}
