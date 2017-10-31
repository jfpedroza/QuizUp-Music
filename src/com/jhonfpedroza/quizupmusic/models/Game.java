package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {

    private long id;

    private User player1;

    private User player2;

    private Date date;

    private Status status;

    public Game(long id) {
        this.id = id;
    }

    public Game(long id, User player1, User player2, Status status) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date();
        this.status = status;
    }

    public Game(User player1, User player2, Status status) {
        this.id = System.currentTimeMillis();
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date();
        this.status = status;
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

    public enum Status {
        CHALLENGED,
        ACCEPTED,
        REJECTED,
        ONGOING,
        FINISHED
    }
}
