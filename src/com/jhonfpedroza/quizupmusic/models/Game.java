package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;
import java.util.Date;

public class Game implements Serializable {

    private long id;

    private User user1;

    private User user2;

    private Date date;

    public Game(long id) {
        this.id = id;
    }

    public Game(long id, User user1, User user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.date = new Date();
    }

    public long getId() {
        return id;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
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
