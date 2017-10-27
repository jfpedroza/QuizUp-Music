package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {

    private long id;

    private User player1;

    private User player2;

    private Date date;

    public Game(long id) {
        this.id = id;
    }

    public Game(long id, User player1, User player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.date = new Date();
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
}
