package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.models.MemeRoom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static com.hohm.GameRunner.*;

public class TextInteractor {


    public static void go(String input, MemeRoom currentRoom) throws IOException {
        if (input.toLowerCase().contains("kitchen") && Arrays.asList(currentRoom.getExit()).contains("kitchen")) {
            player.setRoom("kitchen");
            printSeparator();
        } else if (input.toLowerCase().contains("living room") && Arrays.asList(currentRoom.getExit()).contains("living room")) {
            player.setRoom("livingroom");
            printSeparator();
        } else if (input.toLowerCase().contains("dining room") && Arrays.asList(currentRoom.getExit()).contains("dining room")) {
            player.setRoom("diningroom");
            printSeparator();
        } else if (input.toLowerCase().contains("hallway") && Arrays.asList(currentRoom.getExit()).contains("hallway")) {
            player.setRoom("hallway");
            printSeparator();
        } else {
            printSeparator();
            System.out.println("INVALID DIRECTION: Try typing 'WHERE AM I' for a list of valid exits\n");
        }

        //Checking the
        if (!Objects.equals(rooms.get(player.getRoom()).getTitle(), "hallway")) {
            if (rooms.get(player.getRoom()).getObjectives().get("check complete").get("complete").equals("true")) {
                System.out.println(rooms.get(player.getRoom()).getObjectives().get("clueFound").get("incomplete"));
            } else if(!rooms.get(player.getRoom()).getComplete()){
                System.out.println(rooms.get(player.getRoom()).getDescription().get("memeIncomplete"));
            }
            else {
                System.out.println(rooms.get(player.getRoom()).getDescription().get("memeComplete"));
            }
        }

    }

    public static void look(String input) throws IOException {
        //TODO - look needs to be refactored to prompt for looking at clues
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream itemStream = classLoader.getResourceAsStream("utils.json");
        JsonNode itemJson = Json.parse(itemStream);

        String itemDes = Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", "");
        if (input.contains("room")) {
            if (player.getRoom().equals("hallway")) {
                //Pass, description is handled in the start of the game loop
            } else if (String.valueOf(rooms.get(player.getRoom()).getObjectives().get("check complete").get("complete")).equals("false")) {
                System.out.println(rooms.get(player.getRoom()).getObjectives().get("check complete").get("incomplete"));
            } else {
                System.out.println(rooms.get(player.getRoom()).getObjectives().get("check complete").get("completed"));
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
                    String[] items = {key[1]};
                    player.setItems(items);
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqMet"));
                    System.out.println("You now have: " + Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", ""));
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

    public static void use(String input, MemeRoom currentRoom){

        String[] key = input.split(" ", 2);
        try{
            String chkObj = currentRoom.getObjectives().get("check complete").get("useItem");
            if(Arrays.asList(player.getItems()).contains(key[1]) && chkObj.equals(player.getItems()[0])){
                currentRoom.getObjectives().get("check complete").put("complete", String.valueOf(true));
                objectiveCount++;
                String[] temp = {"[]"};
                player.setItems(temp);
                printSeparator();
                System.out.println(currentRoom.getObjectives().get("check complete").get("completed"));

            }else if(Arrays.asList(player.getItems()).contains(key[1])){
                System.out.printf("That's a nice thought to use the %s... won't do anything..%n", key[1]);
            }else{
                System.out.printf("Might be nice to use the %s, but... you don't even have that!%n",key[1]);
            }

        }catch (NullPointerException e){
            System.out.println("You can't use that...");
        }
    }

    //TODO change objectives complete to clues found and increment based on clues found rather than objectives completed
    public static void printSeparator(){
        String dash = "- - ".repeat(29);
        String printSeparator = String.format("Current Room: %s %20sInventory: %s %20sObjectives Complete: %s"
                , player.getRoom().toUpperCase()
                , "", player.getItems()[0].toUpperCase()
                , "", String.valueOf(objectiveCount));
        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash);
    }
}
