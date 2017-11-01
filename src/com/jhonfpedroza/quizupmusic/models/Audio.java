package com.jhonfpedroza.quizupmusic.models;

import com.google.gson.Gson;

import javax.sound.sampled.AudioFormat;
import java.io.Serializable;

public class Audio implements Serializable {

    private byte[] data;

    private String format;

    public Audio(byte[] data, String format) {
        this.data = data;
        this.format = format;
    }

    public Audio(byte[] data, AudioFormat audioFormat) {
        this.data = data;

        Gson gson = new Gson();
        this.format = gson.toJson(audioFormat);
    }

    public byte[] getData() {
        return data;
    }

    public AudioFormat getFormat() {
        Gson gson = new Gson();
        return gson.fromJson(format, AudioFormat.class);
    }
}
