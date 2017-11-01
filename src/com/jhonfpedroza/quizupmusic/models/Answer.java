package com.jhonfpedroza.quizupmusic.models;

import java.io.Serializable;

public class Answer implements Serializable {

    private long id;

    private String text;

    public Answer(long id) {
        this.id = id;
    }

    public Answer(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return id == answer.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
