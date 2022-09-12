package com.hohm;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

import static com.hohm.controller.BasementDoor.openAttempts;
import static com.hohm.controller.BasementDoor.openDoor;
import static com.hohm.controller.GameBuilder.gameInit;
import static com.hohm.controller.GameBuilder.player;
import static com.hohm.utility.JsonParser.playerFromFile;
import static org.junit.jupiter.api.Assertions.*;

class BasementDoorTest{

    @Test
    void openDoorTestPass() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {

        System.setIn(new ByteArrayInputStream("y".getBytes()));

        try{
            player = playerFromFile("characters.json");

        }
        catch (NullPointerException ignored){

        }
        openDoor("MR723");
        assertEquals("depths", player.getRoom());
    }

    @Test
    void openDoorTestFail(){
        System.setIn(new ByteArrayInputStream("y".getBytes()));
        try {
            gameInit();
        }catch (NullPointerException | IOException | UnsupportedAudioFileException | LineUnavailableException |
                InterruptedException ignored){

        }
        openDoor("MR777");
        assertEquals(openAttempts, 1);
    }

    @Test
    void openDoorTestGameEnd(){
        openAttempts = 2;
        System.setIn(new ByteArrayInputStream("y".getBytes()));
        try {
            gameInit();
        }catch (NullPointerException | IOException | UnsupportedAudioFileException | LineUnavailableException |
                InterruptedException ignored){

        }
        openDoor("MR777");
        assertEquals(player.getRoom(),"dead");
    }


}