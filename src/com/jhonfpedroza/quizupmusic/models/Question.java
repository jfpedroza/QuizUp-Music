package com.jhonfpedroza.quizupmusic.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jhonfpedroza.quizupmusic.server.AudioLoader;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    private long id;

    private String quest;

    private Audio audio;

    private ArrayList<Answer> answers;

    private Answer correctAnswer;

    public Question(long id) {
        this.id = id;
    }

    public Question(JsonObject object) {
        id = object.get("id").getAsLong();
        quest = object.get("quest").getAsString();
        String audioFileName = object.get("audio").getAsString();

        int caIndex = object.get("correctAnswer").getAsInt();
        JsonArray answerArray = object.getAsJsonArray("answers");
        answers = new ArrayList<>();

        for (int i = 0; i < answerArray.size(); i++) {
            Answer answer = new Answer(System.currentTimeMillis(), answerArray.get(i).getAsString());
            if (i == caIndex) {
                correctAnswer = answer;
            }
            answers.add(answer);
        }

        audio = AudioLoader.load(audioFileName);
    }

    public long getId() {
        return id;
    }

    public String getQuest() {
        return quest;
    }

    public Audio getAudio() {
        return audio;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public static ArrayList<Question> readArray(JsonArray array) {
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            Question question = new Question(array.get(i).getAsJsonObject());
            questions.add(question);
        }

        return questions;
    }
}
