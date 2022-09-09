package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hohm.models.MemeRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.hohm.GameBuilder.player;

public class TextInteractor {


    public static void go(String input, MemeRoom currentRoom) {

        String[] rooms = GameBuilder.rooms.keySet().toArray(new String[0]);
        String room = String.join(" ", rooms);

        List<String> roomArr = Arrays.stream(room.split(" ")).map(String::toLowerCase).collect(Collectors.toList());
        Set<String> set = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));

        set.retainAll(roomArr);
        set.retainAll(Arrays.asList(currentRoom.getExit()));

        if (set.iterator().hasNext()) {
            player.setRoom(set.iterator().next());
            printSeparator();
        } else {
            printSeparator();
            System.out.println("INVALID DIRECTION: Try typing 'WHERE AM I' for a list of valid exits\n");
        }
    }

    public static void look(String input, MemeRoom currentRoom) throws IOException {
        String[] currentItems = player.getItems();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        JsonNode description = ObjectGenerator.parse(classLoader.getResourceAsStream("dialogue.json"));

        try {
            String[] currentRoomItems = currentRoom.getItems().keySet().toArray(new String[0]);
            String[] currentRoomAltGet = currentRoom.getAltGet().keySet().toArray(new String[0]);

            Set<String> set = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));
            set.retainAll(List.of(currentRoomAltGet));

            for (String item : currentRoomItems) {
                if (input.contains(item) || (set.iterator().hasNext()
                        && currentRoom.getAltGet().get(set.iterator().next()).equals(item))) {
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(item).get("description"));
                }
            }
        } catch (NullPointerException e) {
            printSeparator();
            System.out.println("That item may exist in the world, but you can't look at it here\n");
            //Do Nothing if there is a null pointer exception
        }
        if(input.contains("inventory")){
            System.out.println("Inventory: "+ Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""));
        } else if (input.contains(Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""))) {
            String itemLookUp = Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", "");
            System.out.println(description.get("items").get(itemLookUp));
        } else if (input.contains("doge")||input.contains("dog")) {
            if (currentRoom.getTitle().equals("kitchen")) {
                System.out.println(description.get("doge").get("description").toPrettyString().replaceAll("[\\[\\](){}\\\\\"]", ""));
            }
        } else if (input.contains("kermit")||input.contains("frog")) {
            if (currentRoom.getTitle().equals("diningroom")) {
                System.out.println(description.get("kermit").get("description").toPrettyString().replaceAll("[\\[\\](){}\\\\\"]", ""));
            }
        } else if (input.contains("cat")||input.contains("grumpy")) {
            if (currentRoom.getTitle().equals("livingroom")) {
                System.out.println(description.get("grumpycat").get("description").toPrettyString().replaceAll("[\\[\\](){}\\\\\"]", ""));
            }
        }
    }

    public static void get(String input, MemeRoom currentRoom) {

        String[] key = input.split(" ", 2);
        String[] altGet = currentRoom.getAltGet().keySet().toArray(new String[0]);
        String keyVal = Arrays.asList(altGet).contains(key[1]) ?
                currentRoom.getAltGet().get(key[1]) : key[1];
        boolean itemsBool = currentRoom.getItems().containsKey(keyVal);
        boolean altItems = currentRoom.getItems().containsKey(keyVal);

        try {
            if (itemsBool || altItems) {
                String objective = currentRoom.getItems().get(keyVal).get("prereq");
                boolean objComplete = Boolean.parseBoolean(currentRoom.getObjectives().get(objective).get("complete"));
                String objType = currentRoom.getItems().get(keyVal).get("type");

                if (objComplete && objType.equals("misc")) {
                    String[] items = {keyVal};
                    player.setItems(items);
                    currentRoom.getObjectives().get("itemFound").put("complete", "true");
                    checkComplete(currentRoom);
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqMet"));
                    System.out.println("You now have: " +
                            Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", "").toUpperCase() + "\n");
                } else if (objComplete && objType.equals("clue")) {
                    if (currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                        player.incrementClues();
                        currentRoom.getObjectives().get("clueFound").put("complete", "true");
                    }
                    checkComplete(currentRoom);
                    printSeparator();
                    System.out.println("CLUE FOUND:");
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqMet") + "\n");
                } else {
                    printSeparator();
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqNotMet"));
                    player.setRoom("dead");
                }
            } else {
                printSeparator();
                System.out.println("NO ERROR");
                System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
            }
        } catch (NullPointerException e) {
            printSeparator();
            e.printStackTrace();
            System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
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
        JsonNode dialogue = ObjectGenerator.parse(classLoader.getResourceAsStream("dialogue.json"));
        int random = (int) (Math.random() * 3);
        try {
            if (input.contains("doge")) {
                if (currentRoom.getTitle().equals("kitchen")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("doge").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("kermit")) {
                if (currentRoom.getTitle().equals("dining")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("kermit").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("cat")) {
                if (currentRoom.getTitle().equals("living")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("grumpycat").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            }else if (input.contains("smeagol")) {
                if (currentRoom.getTitle().equals("bathroom")) {
                    BufferedReader smeagolRiddle = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("You want to play a game of riddles with smeagol.");
                    System.out.println("What has roots as nobody sees, Is taller than trees, Up, up, up it goes And yet, never grows");
                    String first = smeagolRiddle.readLine().toLowerCase();
                    if(first.contains("mountain")){
                        System.out.println("Voiceless it cries,\n" +
                                "Wingless flutters,\n" +
                                "Toothless bites,\n" +
                                "Mouthless mutters.");
                        String second = smeagolRiddle.readLine().toLowerCase();
                        if(second.contains("wind")){
                            System.out.println("It cannot be seen, cannot be felt,\n" +
                                    "Cannot be heard, cannot be smelt.\n" +
                                    "It lies behind stars and under hills,\n" +
                                    "And empty holes it fills.\n" +
                                    "It comes first and follows after,\n" +
                                    "Ends life, kills laughter");
                            String third = smeagolRiddle.readLine().toLowerCase();
                            if(third.contains("Darkness")){

                            }
                        }
                    }
                    else{

                    }
                }
            }else if (input.contains("gollum")) {
                if (currentRoom.getTitle().equals("bathroom")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("gollum").get("dialogue");
                    System.out.println(returnDialogue.get(random));
                }
            }
            else {
                System.out.println("There's no one to talk to here.");
            }
        } catch (NullPointerException e) {
            System.out.println("You can't talk to that person...");
        }
    }

    public static void description(MemeRoom currentRoom) {
        if (currentRoom.getTitle().equals("hallway")) {
            String[] currentItem = player.getItems();
            if (Objects.equals(currentItem[0], "[]")) {
                System.out.println(currentRoom.getDescription().get("nullHallway"));
            } else {
                System.out.println(currentRoom.getDescription().get(currentItem[0]));
            }
        } else {
            if (currentRoom.getComplete()) {
                System.out.println(currentRoom.getDescription().get("memeComplete"));
            }
            else if (currentRoom.getObjectives().get("check complete").get("complete").equals("false")) {
                System.out.println(currentRoom.getDescription().get("memeIncomplete"));
            }
            else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("false")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                System.out.printf("%s%n%s%n",
                        currentRoom.getObjectives().get("itemFound").get("incomplete"),
                        currentRoom.getObjectives().get("clueFound").get("incomplete"));
            }
            else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("true")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {

                System.out.println(currentRoom.getObjectives().get("clueFound").get("incomplete"));
            }
            else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("false")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("true")) {
                System.out.println(currentRoom.getObjectives().get("itemFound").get("incomplete"));
            }
        }
    }

    public static void checkComplete(MemeRoom currentRoom) {
        Map<String, Map<String, String>> objectives = currentRoom.getObjectives();
        if (objectives.get("itemFound").get("complete").equals("true") && objectives.get("clueFound").get("complete").equals("true")) {
            currentRoom.setComplete(true);
        }
    }

    public static void printSeparator() {
        String printRoom = player.getRoom();
        switch (printRoom){
            case "living":
            case "dining":
                printRoom = player.getRoom() + " room";
                break;
            case "basement":
                printRoom = player.getRoom() + " door";
                break;
            case "depths":
                printRoom = "basement " + player.getRoom();
            case "bathroom":
                printRoom = player.getRoom();
        }

        ClearScreen.ClearConsole();
        String dash = "- - ".repeat(29);
        String printSeparator = String.format("Current Room: %s %20sInventory: %s %20sClues Found: %s"
                , printRoom.toUpperCase()
                , "", player.getItems()[0].toUpperCase()
                , "", player.getClues());
        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash);
    }
}
