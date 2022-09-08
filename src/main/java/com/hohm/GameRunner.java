package com.hohm;

import com.hohm.models.MemeRoom;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.hohm.GameInit.player;
import static com.hohm.GameInit.rooms;


public class GameRunner {
    //Creating game objects to reference during game play
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void run() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //Initiating the game loop
        while (true) {
            //Current room starts as the hallway
            if (player.getRoom().equals("dead")) {
                System.out.println("Seems you have died... how very unfortunate.");
                GameInit.gameInit();
                break;
            } else {
                MemeRoom currentRoom = rooms.get(player.getRoom());
                //Check room and check user inventory if hallway
                TextInteractor.description(currentRoom);

                System.out.print(">");

                //Wait for user input and then act based on what the user types
                String userInput = reader.readLine();

                //handle logic based on user input
                if (userInput.equalsIgnoreCase("quit")) {
                    break;
                } else {
                    TextParser.parseText(userInput, currentRoom);
                }
            }
        }
    }
}
