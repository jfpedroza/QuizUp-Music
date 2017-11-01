package com.jhonfpedroza.quizupmusic.server;

import com.jhonfpedroza.quizupmusic.models.Audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AudioLoader {

    public static Audio load(String path) {
        URL url = AudioLoader.class.getResource(path);
        if (url != null) {
            try {
                File file = new File(url.toURI());
                byte[] data = new byte[(int) file.length()];

                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                AudioFormat format = stream.getFormat();
                stream.read(data);
                stream.close();

                return new Audio(data, format);
            } catch (URISyntaxException | UnsupportedAudioFileException | IOException ex) {
                Logger.getLogger(AudioLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("Couldn't find file: " + path);
        }

        return null;
    }
}
