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

import static com.hohm.controller.TextInteractor.*;
import static com.hohm.utility.JsonParser.commands;

public class TextParser {

    public static void parseText(String input, MemeRoom currentRoom) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        JsonNode node = commands();

        String goTo = String.valueOf(node.get("go")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String lookAt = String.valueOf(node.get("look")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String getIt = String.valueOf(node.get("get")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String useIt = String.valueOf(node.get("use")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String[] inputArr = input.toLowerCase().strip().split(" ");

        if (input.equalsIgnoreCase("help")) {
            printSeparator();
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
                    printSeparator();
                    System.out.println("You have saved your game.");
                } else {
                    printSeparator();
                    System.out.println("You have chosen not to overwrite");
                }
            } else {
                Save.save();
                printSeparator();
                System.out.println("You have saved your game");
            }
        } else if (input.contains("where am i")) {
            printSeparator();
            JsonParser.mapPrint(currentRoom.getTitle());
        } else if (input.contains("music") || input.contains("volume")) {
            MusicPlayer.musicPlayer(input, currentRoom);
        } else {
            printSeparator();
            System.out.println("Please enter a valid command");
            JsonParser.utilPrint("help");
        }
    }
}
