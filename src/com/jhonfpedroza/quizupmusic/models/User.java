package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;

public class User implements Serializable {

    private long id;

    private String name;

    private boolean logged;

    public User(int id) {
        this.id = id;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
        this.logged = false;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
