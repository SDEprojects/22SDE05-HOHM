package com.hohm;

import com.hohm.models.MemeRoom;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.hohm.GameInit.player;
import static com.hohm.GameInit.startingItems;
import static com.hohm.GameInit.rooms;
import static com.hohm.TextInteractor.printSeparator;


public class GameRunner {
    //Creating game objects to reference during game play
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void run() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        boolean restartGame = false;
        //Initiating the game loop
        while (true) {
            //Current room starts as the hallway
            if (player.getRoom().equals("dead")) {
                System.out.println("Seems you have died... how very unfortunate. Would you like to play again (y/n)?");
                System.out.print(">");
                String playAgain = reader.readLine();
                if (playAgain.equals("y") || playAgain.equals("yes")) {
                    restartGame = true;
                }
                break;

            } else {
                MemeRoom currentRoom = rooms.get(player.getRoom());
                //Check room and check user inventory if hallway
                System.out.println();
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
        if (restartGame) {
            //Resetting everything in the game for game restart
            player.setRoom("hallway");
            player.setItems(startingItems);
            player.setClues(0);
            try {
                rooms = Generator.generateRooms();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            restartGame = false;
            printSeparator();
            run();
        } else {
            System.out.println("Thanks for playing, we hope to meme with you again soon!");
        }
    }
}
