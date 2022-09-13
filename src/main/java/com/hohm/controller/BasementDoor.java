package com.hohm.controller;

import com.hohm.utility.JsonParser;

import java.io.IOException;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.PrintSeparators.printSeparatorMain;


/**
 * Authors: Daniel An, Kaitlyn Fernelius, Agustin Duran
 * BasementDoor is a class that holds the functionality for when a player tries to go to the basement.
 */
public class BasementDoor {

    static String DOOR_CODE;

    static {
        try {
            DOOR_CODE = JsonParser.utilNodeReturn("basement code");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int openAttempts = 0;

    /**
     * @param input
     * openDoor is called when the player tries to enter the basement.
     * Input is compared to the DOOR_CODE pulled from a .json file.
     * If successful the player is sent to the basement depths.
     * If three unsuccessful attempts occur the game ends.
     */
    public static void openDoor(String input){
        if(openAttempts<2){
            if(input.equals(DOOR_CODE)){
                player.setRoom("depths");
                printSeparatorMain();
                System.out.println("The door opens, and you walk down into the basement depths....");
            }else if(input.equalsIgnoreCase("kitchen")){
                player.setRoom("kitchen");
                printSeparatorMain();
            }
            else{
                printSeparatorMain();
                openAttempts++;
                System.out.printf("ERROR WRONG CODE: %s More Chances%n", 3 - openAttempts);
            }
        }else{
            openAttempts = 0;
            System.out.println("Three strikes you're out buddy, as in you just got MAX damage from failing at this door code business.\n");
            player.setRoom("dead");
        }
    }
}
