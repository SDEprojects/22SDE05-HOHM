package com.hohm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameRunner {

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void gameInit() throws IOException {

        while(true){
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = reader.readLine();
            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                GameRunner.run();
                break;
            }else if(confirm.equalsIgnoreCase("n") || confirm.equalsIgnoreCase("no")){
                System.out.println("That's unfortunate, we hope to see you again soon!");
                break;
            }else{
                System.out.println("That wasn't valid input... \nWould you like to start a new game(y/n)?:");
            }
        }
    }

    public static void run() throws IOException {


        //Initiating the game loop
        while (true){
            System.out.println("Game Running");
            System.out.print("What would you like to do?\n>");
            String userInput = reader.readLine().toLowerCase();


            //TODO move this logic to a text parser
            if(userInput.equalsIgnoreCase("quit")){
                System.out.println("Thanks for playing come back soon!!");
                break;
            }else if(userInput.equalsIgnoreCase("help")){
                com.hohm.UtilLoader.help();
            }
            else {
                UtilLoader.commands(userInput);
            }
        }

    }
}
