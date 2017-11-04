package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements Serializable {

    private long id;

    private User player1;

    private User player2;

    private Date date;

    private Status status;

    private ArrayList<Question> questions;

    private HashMap<Question, QuestionAnswer> player1Answers;
    private HashMap<Question, QuestionAnswer> player2Answers;

    public Game(long id) {
        this.id = id;
    }

    public Game(long id, User player1, User player2, Status status) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date();
        this.status = status;
        this.questions = null;
        player1Answers = new HashMap<>();
        player2Answers = new HashMap<>();
    }

    public Game(User player1, User player2, Status status) {
        this(System.currentTimeMillis(), player1, player2, status);
        this.id = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public Date getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public HashMap<Question, QuestionAnswer> getPlayer1Answers() {
        return player1Answers;
    }

    public HashMap<Question, QuestionAnswer> getPlayer2Answers() {
        return player2Answers;
    }

    public void setAnswer(User player, Question question, Answer answer, long time) {
        int points = 0;
        if (question.getCorrectAnswer().equals(answer)) {
            points = (int) time;
        }

        if (questions.get(questions.size() - 1).equals(question)) {
            points *= 2;
        }

        QuestionAnswer questionAnswer = new QuestionAnswer(answer, points);
        if (player.equals(player1)) {
            player1Answers.put(question, questionAnswer);
        } else if (player.equals(player2)) {
            player2Answers.put(question, questionAnswer);
        } else {
            Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Player " + player + " not found. P1 = " + player1 + ", P2 = " + player2);
        }
    }

    public int getPoints(User player) {
        HashMap<Question, QuestionAnswer> answers;
        if (player.equals(player1)) {
            answers = player1Answers;
        } else if (player.equals(player2)) {
            answers = player2Answers;
        } else {
            Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Player " + player + " not found. P1 = " + player1 + ", P2 = " + player2);
            return 0;
        }

        return answers.values().stream().mapToInt(questionAnswer -> questionAnswer.points).sum();
    }

    public boolean canBeFinished() {
        return player1Answers.size() == questions.size() && player2Answers.size() == questions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return String.format("(%s, %s vs %s)", getId(), getPlayer1(), getPlayer2());
    }

    public enum Status {
        CHALLENGED,
        ACCEPTED,
        REJECTED,
        ONGOING,
        FINISHED
    }

    public class QuestionAnswer implements Serializable {
        Answer answer;
        int points;

        public QuestionAnswer(Answer answer, int points) {
            this.answer = answer;
            this.points = points;
        }
    }
}
