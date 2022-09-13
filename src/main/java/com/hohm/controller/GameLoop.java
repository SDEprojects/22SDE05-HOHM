package com.hohm.controller;

import com.hohm.model.MemeRoom;
import com.hohm.utility.JsonParser;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameBuilder.rooms;
import static com.hohm.controller.PrintSeparators.printSeparatorBottom;
import static com.hohm.controller.PrintSeparators.printSeparatorMain;

/**
 * Authors: Daniel An, Kaitlyn Fernelius, Agustin Duran
 * GameLoop is used to instantiate the game loop, and continually run until a win condition or a lose condition occurs
 */
public class GameLoop {
    //Creating game objects to reference during game play
    public static MemeRoom previousRoom;
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Run starts a game loop that gathers user input and passes the controller capabilities to the TextInteractor
     * @throws IOException - Thrown from improper input
     * @throws UnsupportedAudioFileException - Thrown if audio file is referenced that is unsupported
     */
    public static void run() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        //Initiating the game loop
        label:
        while (true) {
            //Current room starts as the hallway
            MemeRoom currentRoom = rooms.get(player.getRoom());

            //Special circumstances are checked and different courses of action are taken based on room type
            //If no special circumstances are found, the normal text parsing occurs
            switch (player.getRoom()) {
                case "win":
                    String winOut = JsonParser.utilNodeReturn("win text");
                    printSeparatorMain();
                    System.out.println(winOut + "\n");
                    System.out.println("Thanks for playing, we hope to meme with you again soon");
                    break label;
                case "dead":
                    if(previousRoom.getTitle().equals("depths")){
                        String loseOut = JsonParser.utilNodeReturn("lose text");
                        System.out.println(loseOut);
                    }else{
                        System.out.println("Seems you have died... how very unfortunate.");
                    }
                    GameBuilder.gameInit();
                    break label;
                case "basement": {
                    TextInteractor.description(currentRoom);
                    PrintSeparators.basementDoorBottom();
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
                    if(currentRoom!=previousRoom){
                        TextInteractor.description(currentRoom);
                    }
                    printSeparatorBottom();
                    System.out.print(">");
                    String input = reader.readLine();
                    if (!currentRoom.getComplete() && input.equalsIgnoreCase("one does not simply walk into mordor")) {
                        PrintSeparators.printSeparatorMain();
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

                    printSeparatorBottom();
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
