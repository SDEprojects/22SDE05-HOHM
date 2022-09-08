package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hohm.models.MemeRoom;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import static com.hohm.GameInit.player;

public class TextInteractor {


    public static void go(String input, MemeRoom currentRoom) throws IOException {
        //TODO this can be simplified and based on what's in the JSON file rather than hard coded
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
        } else if (input.toLowerCase().contains("basement") && Arrays.asList(currentRoom.getExit()).contains("basement")) {
            player.setRoom("basement");
            printSeparator();
        } else {
            printSeparator();
            System.out.println("INVALID DIRECTION: Try typing 'WHERE AM I' for a list of valid exits\n");
        }

        if (!Objects.equals(GameInit.rooms.get(player.getRoom()).getTitle(), "hallway")) {
            if(GameInit.rooms.get(player.getRoom()).getComplete()){
                System.out.println(GameInit.rooms.get(player.getRoom()).getDescription().get("memeComplete"));
            }
            else if (GameInit.rooms.get(player.getRoom()).getObjectives().get("check complete").get("complete").equals("true")) {
                System.out.println(GameInit.rooms.get(player.getRoom()).getObjectives().get("clueFound").get("incomplete"));
            } else {
                System.out.println(GameInit.rooms.get(player.getRoom()).getDescription().get("memeIncomplete"));
            }
        }
    }

    public static void look(String input, MemeRoom currentRoom) throws IOException {
        //TODO - look needs to be refactored to prompt for looking at clues

        String[] currentItems = player.getItems();
        try{
            String[] currentRoomItems = currentRoom.getItems().keySet().toArray(new String[0]);
            for(String item: currentRoomItems){
                if(input.contains(item)){
                    System.out.println(currentRoom.getItems().get(item).get("description"));
                }
            }
        }catch (NullPointerException e){
            //Do Nothing if there is a null pointer exception
        }


        if (input.contains("room")) {
            if (player.getRoom().equals("hallway") || player.getRoom().equals("basement")) {
                //Do nothing, description is handled in the start of the game loop
            } else if (currentRoom.getComplete()) {
                System.out.println(currentRoom.getDescription().get("memeComplete"));
            } else if (currentRoom.getObjectives().get("check complete").get("complete").equals("false")) {
                System.out.println(currentRoom.getObjectives().get("check complete").get("incomplete"));
            } else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("false")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                System.out.printf("%s%n%s%n",
                        currentRoom.getObjectives().get("clueFound").get("incomplete"),
                        currentRoom.getObjectives().get("itemFound").get("incomplete"));
            } else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("true")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                System.out.println(currentRoom.getObjectives().get("clueFound").get("incomplete"));
            } else {
                System.out.println(currentRoom.getObjectives().get("itemFound").get("incomplete"));
            }

        }
    }

    public static void get(String input, MemeRoom currentRoom) {

        String[] key = input.split(" ", 2);
        try {
            if (currentRoom.getItems().containsKey(key[1])) {
                String objective = currentRoom.getItems().get(key[1]).get("prereq");
                boolean objComplete = Boolean.parseBoolean(currentRoom.getObjectives().get(objective).get("complete"));
                String objType = currentRoom.getItems().get(key[1]).get("type");

                if (objComplete && objType.equals("misc")) {
                    String[] items = {key[1]};
                    player.setItems(items);
                    GameInit.rooms.get(currentRoom.getTitle()).getObjectives().get("itemFound").put("complete", "true");
                    checkComplete(currentRoom);
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqMet"));
                    System.out.println("You now have: " + Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", ""));
                }
                else if (objComplete && objType.equals("clue")) {
                    if(GameInit.rooms.get(currentRoom.getTitle()).getObjectives().get("clueFound").get("complete").equals("false")){
                        player.incrementClues();
                        GameInit.rooms.get(currentRoom.getTitle()).getObjectives().get("clueFound").put("complete", "true");
                    }
                    checkComplete(currentRoom);
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqMet"));
                } else {
                    System.out.println(currentRoom.getItems().get(key[1]).get("prereqNotMet"));
                    player.setRoom("dead");
                }
            } else {
                System.out.printf("You are unable to get the %s... whatever that is%n%n", key[1]);
                System.out.println("PROBLEM HERE");
            }
        } catch (NullPointerException e) {
            System.out.printf("You are unable to get the %s... whatever that is%n%n", key[1]);
            System.out.println("PROBLEM HERE NULL");
        }

    }

    public static void use(String input, MemeRoom currentRoom) {
        String[] key = input.split(" ", 2);
        try {
            String chkObj = currentRoom.getObjectives().get("check complete").get("useItem");
            if (Arrays.asList(player.getItems()).contains(key[1]) && chkObj.equals(player.getItems()[0])) {
                currentRoom.getObjectives().get("check complete").put("complete", String.valueOf(true));
                String[] temp = {"[]"};
                player.setItems(temp);
                printSeparator();
                System.out.println(currentRoom.getObjectives().get("check complete").get("completed"));

            } else if (Arrays.asList(player.getItems()).contains(key[1])) {
                printSeparator();
                System.out.printf("That's a nice thought to use the %s... won't do anything..%n", key[1]);
            } else {
                printSeparator();
                System.out.printf("Might be nice to use the %s, but... you don't even have that!%n", key[1]);
            }
        } catch (NullPointerException e) {
            printSeparator();
            System.out.println("You can't use that here...");
        }
    }

    public static void talk(String input, MemeRoom currentRoom) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        JsonNode dialogue = Generator.parse(classLoader.getResourceAsStream("dialogue.json"));
        int random = (int) (Math.random() * 3);
        try {
            if (input.contains("doge")) {
                if (currentRoom.getTitle().equals("kitchen")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("doge").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("kermit")) {
                if (currentRoom.getTitle().equals("dining room")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("kermit").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("cat")) {
                if (currentRoom.getTitle().equals("living room")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("grumpycat").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            } else {
                System.out.println("There's no one to talk to here.");
            }
        } catch (NullPointerException e) {
            System.out.println("You can't talk to that person...");
        }
    }
    public static void description(){

    }
    public static void checkComplete(MemeRoom currentRoom) {
        Map<String, Map<String, String>> objectives = GameInit.rooms.get(currentRoom.getTitle()).getObjectives();
        if (objectives.get("itemFound").get("complete").equals("true") && objectives.get("clueFound").get("complete").equals("true")) {
            GameInit.rooms.get(currentRoom.getTitle()).setComplete(true);
        }
    }

    public static void printSeparator() {
        ClearScreen.ClearConsole();
        String dash = "- - ".repeat(29);
        String printSeparator = String.format("Current Room: %s %20sInventory: %s %20sClues Found: %s"
                , player.getRoom().toUpperCase()
                , "", player.getItems()[0].toUpperCase()
                , "", player.getClues());
        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash);
    }
}
