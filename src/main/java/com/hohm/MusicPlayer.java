package com.hohm;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {

    public static FloatControl gainControl;
    public static ClassLoader classLoader;
    public static Clip clip;

    public static void musicPlayer(String input) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (input.contains("start")) {
            play();
        } else if (input.contains("stop")) {
            stops();
        } else if (input.contains("up")) {
            volumeIncrease();
        } else if (input.contains("down")) {
            volumeDecrease();
        } else if (input.contains("normal")) {
            volumeNormal();
        } else {
            System.out.println("Sorry music player might not have that ability.");
        }
    }

    public static void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        classLoader = Thread.currentThread().getContextClassLoader();
        InputStream musicStream = classLoader.getResourceAsStream("background.wav");
        InputStream bufferedMusic = new BufferedInputStream(musicStream);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedMusic);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.start();
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public static void stops() {
        clip.stop();
    }

    public static void volumeIncrease() {
        gainControl.setValue(6.0f);
    }

    public static void volumeDecrease() {
        gainControl.setValue(-10.0f);
    }

    public static void volumeNormal() {
        gainControl.setValue(0.0f);
    }

}

