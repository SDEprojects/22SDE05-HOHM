package com.hohm;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static com.hohm.controller.GameBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

class GameBuilderTest {

    @Test
    void gameInitSuccessNoSaveFile() {
        System.setIn(new ByteArrayInputStream("y".getBytes()));
        try {
            gameInit();
        }catch (NullPointerException | IOException | UnsupportedAudioFileException | LineUnavailableException |
                InterruptedException e){
            //Do nothing
        }
        assertNotNull(rooms);
        assertNotNull(player);
        assertNotNull(memes);
    }

    @Test
    void gameInitSuccessSaveFile(){
        //You have to run the game and save a file for this to work
        //TODO - Figure out how to have it run and save a file
        System.setIn(new ByteArrayInputStream("y".getBytes()));
        File saved = new File("saved_data/saved_Rooms.json");
        if(saved.exists()){
            //Do nothing the test will use the existing files
        }else{
            try {
                gameInit();

            }catch (NullPointerException | IOException | UnsupportedAudioFileException | LineUnavailableException |
                    InterruptedException e){
                //Do nothing
            }
        }
        assertNotNull(rooms);

    }
}