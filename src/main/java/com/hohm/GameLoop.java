package com.hohm;

import com.hohm.models.MemeRoom;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static com.hohm.GameBuilder.player;
import static com.hohm.GameBuilder.rooms;


public class GameLoop {
    //Creating game objects to reference during game play
    public static MemeRoom previousRoom;
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void run() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //Initiating the game loop
        while (true) {
            //Current room starts as the hallway
            MemeRoom currentRoom = rooms.get(player.getRoom());
            if (player.getRoom().equals("dead")) {
                System.out.println("Seems you have died... how very unfortunate.");
                GameBuilder.gameInit();
                break;
            } else if (player.getRoom().equals("basement")) {
                TextInteractor.description(currentRoom);
                System.out.print(">Door Code:");
                String input = reader.readLine();
                BasementDoor.openDoor(input);
            } else if (player.getRoom().equals("depths")) {
                if (Arrays.asList(player.getItems()).contains("dice")) {
                    System.out.println(currentRoom.getDescription().get("hasDice"));
                    System.out.print(">");
                    String input = reader.readLine();
                } else {
                    System.out.println(currentRoom.getDescription().get("noDice"));
                    player.setRoom("dead");
                }
            } else if (player.getRoom().equals("office")) {
                TextInteractor.description(currentRoom);
                System.out.print(">");
                String input = reader.readLine();
                if (!currentRoom.getComplete() && input.equalsIgnoreCase("one does not simply walk into mordor")) {
                    TextInteractor.printSeparator();
                    System.out.println("How wise.. Good luck. I have also given you an item that will aid you in your quest");
                    currentRoom.setComplete(true);
                    player.setHasAdvantage(true);
                } else {
                    TextParser.parseText(input, currentRoom);
                }
            } else {
                //Check room and check user inventory if hallway
                TextInteractor.description(currentRoom);
                /* Another method to explore in terms of printing rooms description
                if (currentRoom != previousRoom) {
                    TextInteractor.description(currentRoom);
                } else...
                */
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
            previousRoom = currentRoom;
        }
    }
}
