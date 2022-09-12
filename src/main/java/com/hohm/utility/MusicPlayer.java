package com.hohm.utility;

import com.hohm.model.MemeRoom;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.hohm.controller.TextInteractor.description;
import static com.hohm.controller.TextInteractor.printSeparator;

public class MusicPlayer {
    public static FloatControl gainControl;
    public static ClassLoader classLoader;
    public static Clip clip;
    //Able to play music with a .wav file using javax
    public static void musicPlayer(String input, MemeRoom currentRoom) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //controller for the music player
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
        printSeparator();
        description(currentRoom);
    }
    // Plays the music starting the game.
    public static void play() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        //Uses input stream->buffer for jar file, creates a clip that you're able to play,stop,volume up/down.
        classLoader = Thread.currentThread().getContextClassLoader();
        InputStream musicStream = classLoader.getResourceAsStream("background.wav");
        InputStream bufferedMusic = new BufferedInputStream(musicStream);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedMusic);
        try {
            if(clip.isActive()){
                clip.stop();
            }
        }catch (NullPointerException e){
            //Do nothing if no clip exists, proceed as usual
        }
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        clip.start();
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }
    public static void stops() {
        //stops the current clip
        clip.stop();
    }
    public static void volumeIncrease() {
        //increases the volume
        float currentVolume = gainControl.getValue();
        if (currentVolume+1.0 <= 6.0) {
            gainControl.setValue(currentVolume+1);
        }
    }
    public static void volumeDecrease() {
        //decreases the volume
        float currentVolume = gainControl.getValue();
        if(currentVolume-5 > -75) {
            gainControl.setValue(currentVolume-5);
        }
    }
    public static void volumeNormal() {
        //resets the volume
        gainControl.setValue(0.0f);
    }
}

