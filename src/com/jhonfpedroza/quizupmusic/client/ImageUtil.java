package com.jhonfpedroza.quizupmusic.client;

import javax.swing.*;
import java.awt.*;

class ImageUtil {

    /** Returns an ImageIcon, or null if the path was invalid. */
    static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = ImageUtil.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    static ImageIcon createImageIcon(String path) {
        return createImageIcon(path, null);
    }

    static ImageIcon createImageIcon(String path, int width, int height, String description) {
        ImageIcon imageIcon = createImageIcon(path, description);
        return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    static ImageIcon createImageIcon(String path, int width, int height) {
        return createImageIcon(path, width, height, null);
    }
}
