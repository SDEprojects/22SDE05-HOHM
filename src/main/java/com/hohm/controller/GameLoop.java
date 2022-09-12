package com.hohm.controller;

import com.hohm.model.MemeRoom;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameBuilder.rooms;
import static com.hohm.controller.TextInteractor.commandList;


public class GameLoop {
    //Creating game objects to reference during game play
    public static MemeRoom previousRoom;
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void run() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        //Initiating the game loop
        label:
        while (true) {
            //Current room starts as the hallway
            MemeRoom currentRoom = rooms.get(player.getRoom());
            switch (player.getRoom()) {
                case "win":
                    System.out.println("You win, the meme lord is defeated!!");
                    break label;
                case "dead":
                    System.out.println("Seems you have died... how very unfortunate.");
                    GameBuilder.gameInit();
                    break label;
                case "basement": {
                    TextInteractor.description(currentRoom);
                    System.out.print(">Door Code:");
                    String input = reader.readLine();
                    BasementDoor.openDoor(input);
                    break;
                }
                case "depths":
                    if (Arrays.asList(player.getItems()).contains("dice")) {
                        System.out.println(currentRoom.getDescription().get("hasDice"));
                        System.out.print(">");
                        reader.readLine();
                        BossEncounter.encounter(currentRoom);
                    } else {
                        System.out.println(currentRoom.getDescription().get("noDice"));
                        player.setRoom("dead");
                    }
                    break;
                case "office": {
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
                    break;
                }
                default:
                    if (currentRoom != previousRoom) {
                        TextInteractor.description(currentRoom);
                    }

                    commandList();
                    System.out.print(">");

                    String userInput = reader.readLine();

                    if (userInput.equalsIgnoreCase("quit")) {
                        break label;
                    } else {
                        TextParser.parseText(userInput, currentRoom);
                    }
                    break;
            }
            previousRoom = currentRoom;
        }
    }
}
