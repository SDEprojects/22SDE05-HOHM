package main.java.com.hohm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameRunner {
    public static void gameInit() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = reader.readLine();
            if(confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")){
                GameRunner.run();
                break;
            }else if(confirm.toLowerCase().equals("n") || confirm.toLowerCase().equals("no")){
                System.out.println("That's unfortunate, we hope to see you again soon!");
                break;
            }else{
                System.out.println("That wasn't valid input... \nWould you like to start a new game(y/n)?:");
            }
        }
    }

    public static void run(){

        while (true){
            System.out.println("Game Running");
            break;
        }

    }
}
