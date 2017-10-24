package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;

public class User implements Serializable {

    private long id;

    private String name;

    public User(int id) {
        this.id = id;
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
