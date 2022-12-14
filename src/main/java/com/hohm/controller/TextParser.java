package com.hohm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.model.MemeRoom;
import com.hohm.utility.JsonParser;
import com.hohm.utility.MusicPlayer;
import com.hohm.utility.Save;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hohm.controller.TextInteractor.*;
import static com.hohm.utility.JsonParser.nodeValueReturn;

/**
 * Authors: Kaitlyn Fernelius, Daniel An, Agustin Duran
 * TextParser is used to parse the text and determine if a command is valid then call the appropriate method
 */
public class TextParser {

    /**
     * parseText takes in the user input and checks to see if any commands or synonyms of commands are called.
     * If they are then it is passed to the appropriate method to handle the output logic of the command.
      * @param input - user input
     * @param currentRoom - current room the player is in
     * @throws IOException - thrown if user input is incorrect
     * @throws UnsupportedAudioFileException - thrown if the music file called is unsupported
     */
    public static void parseText(String input, MemeRoom currentRoom) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        JsonNode node = nodeValueReturn("commands","utils.json");
        List<String> goTo = Arrays.stream(String.valueOf(node.get("go")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "").split(",")).collect(Collectors.toList());
        List<String> lookAt = Arrays.stream(String.valueOf(node.get("look")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "").split(",")).collect(Collectors.toList());
        List<String> getIt = Arrays.stream(String.valueOf(node.get("get")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "").split(",")).collect(Collectors.toList());
        List<String> useIt = Arrays.stream(String.valueOf(node.get("use")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "").split(",")).collect(Collectors.toList());
        String[] inputArr = input.toLowerCase().strip().split(" ");

        if (input.equalsIgnoreCase("help")) {
            PrintSeparators.printSeparatorMain();
            JsonParser.utilPrint("help");
            if (currentRoom.getTitle().equalsIgnoreCase("office")) {
                System.out.println(currentRoom.getDescription().get("hint"));
            }
        } else if (goTo.contains(inputArr[0])) {
            go(input, currentRoom);
        } else if (lookAt.contains(inputArr[0])) {
            look(input, currentRoom);
        } else if (getIt.contains(inputArr[0])) {
            get(input, currentRoom);
        } else if (useIt.contains(inputArr[0])) {
            use(input, currentRoom);
        } else if (input.contains("talk")) {
            talk(input, currentRoom);
        } else if (input.contains("save")) {
            File savedRooms = new File("saved_data/saved_Rooms.json");
            savedRooms.getParentFile().mkdirs();
            if (!savedRooms.createNewFile()) {
                System.out.print("You already have saved files, would you like to overwrite (y/n)?: ");
                String saveConfirm = GameLoop.reader.readLine();
                if (saveConfirm.equalsIgnoreCase("y") || saveConfirm.equalsIgnoreCase("yes")) {
                    Save.save();
                    PrintSeparators.printSeparatorMain();
                    System.out.println("You have saved your game.");
                } else {
                    PrintSeparators.printSeparatorMain();
                    System.out.println("You have chosen not to overwrite");
                }
            } else {
                Save.save();
                PrintSeparators.printSeparatorMain();
                System.out.println("You have saved your game");
            }
        } else if (input.contains("where am i")) {
            PrintSeparators.printSeparatorMain();
            JsonParser.mapPrint(currentRoom.getTitle());
        } else if (input.contains("music") || input.contains("volume")) {
            MusicPlayer.musicPlayer(input, currentRoom);
        } else {
            PrintSeparators.printSeparatorMain();
            System.out.println("Please enter a valid command");
            JsonParser.utilPrint("help");
        }
    }
}
