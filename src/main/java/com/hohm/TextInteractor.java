package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.models.MemeRoom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static com.hohm.GameRunner.player;
import static com.hohm.GameRunner.rooms;

public class TextInteractor {


    public static void go(String input, MemeRoom currentRoom) throws IOException {
        if (input.toLowerCase().contains("kitchen") && Arrays.asList(currentRoom.getExit()).contains("kitchen")) {
            System.out.println("\n--------Going to the Kitchen-----------");
            player.setRoom("kitchen");
        } else if (input.toLowerCase().contains("living room") && Arrays.asList(currentRoom.getExit()).contains("living room")) {
            System.out.println("\n----------Going to the Living Room--------");
            player.setRoom("livingroom");
        } else if (input.toLowerCase().contains("dining room") && Arrays.asList(currentRoom.getExit()).contains("dining room")) {
            System.out.println("\n--------Going to the Dining Room-------");
            player.setRoom("diningroom");
        } else if (input.toLowerCase().contains("hallway") && Arrays.asList(currentRoom.getExit()).contains("hallway")) {
            System.out.println("\n--------Going to the Hallway----------");
            player.setRoom("hallway");
        } else {
            System.out.println("\n--You are unable to go that way. Type 'Where am I' for valid exits--");
        }

        if (!Objects.equals(rooms.get(player.getRoom()).getTitle(), "hallway")) {
            if (!rooms.get(player.getRoom()).getComplete()) {
                System.out.println(rooms.get(player.getRoom()).getDescription().get("memeIncomplete"));
            } else {
                System.out.println(rooms.get(player.getRoom()).getDescription().get("memeComplete"));
            }
        }

    }

    public static void look(String input) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream roomStream = classLoader.getResourceAsStream("rooms.json");
        InputStream itemStream = classLoader.getResourceAsStream("utils.json");
        JsonNode node = Json.parse(roomStream);
        JsonNode itemJson = Json.parse(itemStream);

        String itemDes = Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", "");
        if (input.contains("room")) {
            if (player.getRoom().equals("hallway")) {
                //Pass, description is handled in the start of the game loop
            } else if (String.valueOf(node.get(player.getRoom()).get("objectives").get("check complete").get("complete")).equals("false")) {
                System.out.println(node.get(player.getRoom()).get("objectives").get("check complete").get("incomplete").asText());
            } else {
                System.out.println(node.get(player.getRoom()).get("objectives").get("complete"));
            }

        } else if (input.contains(itemDes)) {
            System.out.println(itemJson.get(itemDes));
        } else {
            System.out.println("invalid choice, see help");
        }
    }

    public static void get(String input, MemeRoom currentRoom) {

        String[] key = input.split(" ", 2);
        try {
            if (currentRoom.getItems().containsKey(key[1])) {
                String objective = currentRoom.getItems().get(key[1]).get("prereq");
                boolean objComplete = Boolean.parseBoolean(currentRoom.getObjectives().get(objective).get("complete"));

                if (objComplete) {
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqMet"));
                    String[] items = {key[1]};
                    player.setItems(items);
                    System.out.println(Arrays.toString(player.getItems()));
                } else {
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqNotMet"));
                    player.setRoom("dead");
                }
            }
            else {
                System.out.printf("You are unable to get the %s... whatever that is%n%n", key[1]);
            }
        } catch (NullPointerException e) {
            System.out.printf("You are unable to get the %s... whatever that is%n%n", key[1]);
        }

    }
}
