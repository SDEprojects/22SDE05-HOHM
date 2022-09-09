package com.hohm;

import java.io.IOException;

import static com.hohm.GameBuilder.player;
import static com.hohm.TextInteractor.printSeparator;

public class BasementDoor {

    static String DOOR_CODE;

    static {
        try {
            DOOR_CODE = UtilLoader.code();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int openAttempts = 0;

    public static void openDoor(String input){
        if(openAttempts<2){
            if(input.equals(DOOR_CODE)){
                player.setRoom("depths");
                printSeparator();
                System.out.println("The door opens, and you walk down into the basement depths....");
            }else if(input.equalsIgnoreCase("kitchen")){
                player.setRoom("kitchen");
                printSeparator();
            }
            else{
                printSeparator();
                openAttempts++;
                System.out.printf("ERROR WRONG CODE: %s More Chances%n", 3 - openAttempts);
            }
        }else{
            openAttempts = 0;
            System.out.println("Three strikes your out buddy, as in you just got #emotionaldamage from the door");
            player.setRoom("dead");
        }
    }
}
