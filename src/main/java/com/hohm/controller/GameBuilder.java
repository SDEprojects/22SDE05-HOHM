package com.hohm.controller;

import com.hohm.model.Meme;
import com.hohm.model.MemeRoom;
import com.hohm.model.Player;
import com.hohm.utility.JsonParser;
import com.hohm.utility.MusicPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.hohm.utility.JsonParser.objMapFromFile;
import static com.hohm.utility.JsonParser.playerFromFile;
import static com.hohm.controller.TextInteractor.printSeparator;

/**
 * Authors: Kaitlyn Fernelius, Daniel An, Agustin Duran
 * GameBuilder is used to set the static objects that are viewed and manipulated in game.
 */
public class GameBuilder {

    public static Player player;
    public static Map<String, MemeRoom> rooms;
    public static Map<String, Meme> memes;

    /**
     * GameInit is called after the user has determined that they would like to play a game.
     * GameInit holds the logic to either load a saved game (if one exists) or to create a new game from base files
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */
    public static void gameInit() throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {

        while (true) {
            File saved = new File("saved_data/saved_Rooms.json");
            if (saved.exists()) {
                System.out.print("Would you like to restore your previous progress (y/n)?: ");
                String response = GameLoop.reader.readLine();
                if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                    try {
                        rooms = objMapFromFile("saved_data/saved_Rooms.json",MemeRoom.class);
                        player = playerFromFile("saved_data/saved_Characters.json");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    printSeparator();
                    MusicPlayer.play();
                    GameLoop.run();
                    break;
                }
            }
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = GameLoop.reader.readLine();
            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                try {
                    rooms = objMapFromFile("rooms.json",MemeRoom.class);
                    player = playerFromFile("characters.json");
                    memes = objMapFromFile("npc.json",Meme.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println();
                printSeparator();
                JsonParser.utilPrint("begin story");
                MusicPlayer.play();
                GameLoop.run();
                break;
            } else if (confirm.equalsIgnoreCase("n") || confirm.equalsIgnoreCase("no")) {
                System.out.println("That's unfortunate, we hope to meme with you again soon!");
                break;
            } else {
                System.out.println("That wasn't valid input... \n");
            }
        }
    }
}
