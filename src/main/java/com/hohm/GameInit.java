package com.hohm;

import java.io.File;
import java.io.IOException;

import static com.hohm.TextInteractor.printSeparator;

public class GameInit {
    public static void gameInit() throws IOException {

        while (true) {
            File saved = new File("saved_data/saved_Rooms.json");
            if (saved.exists()) {
                System.out.println("Would you like to restore your previous progress (y/n)?: ");
                String response = GameRunner.reader.readLine();
                if (response.toLowerCase().equals("y") || response.toLowerCase().equals("yes")) {

                }
            } else {
                System.out.print("Would you like to start a new game (y/n)?: ");
                String confirm = GameRunner.reader.readLine();
                if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
                    System.out.println();
                    printSeparator();
                    GameRunner.run();
                    break;
                } else if (confirm.toLowerCase().equals("n") || confirm.toLowerCase().equals("no")) {
                    System.out.println("That's unfortunate, we hope to meme with you again soon!");
                    break;
                } else {
                    System.out.println("That wasn't valid input... \n");
                }
            }
        }
    }
}
