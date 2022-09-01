package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

// UtilLoader will be used
public final class UtilLoader {
    // UtilLoader will be used from the user to call the help functions.
    // Intro will also be printed.
    public static File file = new File("src/main/resources/utils.json");
    public static File rooms = new File("src/main/resources/rooms.json");
    public static void intro() throws IOException {
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("intro").textValue() + "\n");
    }

    public static void help() throws IOException {
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("help").textValue() + "\n");
    }
    public static void commands(String input) throws IOException {
        JsonNode node = Json.parse(file);
        node = node.get("commands");
        String go = String.valueOf(node.get("go"));
        String look = String.valueOf(node.get("look"));
        JsonNode room = Json.parse(rooms);
        String currentRoom = String.valueOf(room.get("Hallway").get("exit"));

        if(look.contains(input)){
            System.out.println("looking...\n");
            System.out.println(look);
        }
        else if(go.contains(input)) {
            System.out.println("Going...\n");
            //rooms.json file

            System.out.println(currentRoom);

            if (currentRoom.toLowerCase().contains(input.toLowerCase())) {
                System.out.println(currentRoom);
            }
        }
    }


}
