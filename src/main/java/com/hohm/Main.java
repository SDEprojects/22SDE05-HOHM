package com.hohm;

import com.hohm.controller.GameBuilder;
import com.hohm.utility.ClearScreen;
import com.hohm.utility.JsonParser;

/**
 * Authors: Daniel An, Kaitlyn Fernelius, Agustin Duran
 * The main class is the single entry point to the program, clearing the console and then starting the game
 */
public class Main {

    /**
     * The main method will take in any system args and then start the game by printing the title, intro and help text.
     * The rest of the work for the game is passed to the GameBuilder class to initiate all the necessary objects for play
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ClearScreen.clearConsole();
        JsonParser.utilPrint("title");
        JsonParser.utilPrint("intro");
        JsonParser.utilPrint("help");
        GameBuilder.gameInit();
    }
}
