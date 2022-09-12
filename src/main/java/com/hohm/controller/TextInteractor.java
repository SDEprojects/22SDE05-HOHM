package com.hohm.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hohm.model.Meme;
import com.hohm.model.MemeRoom;
import com.hohm.utility.ClearScreen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.hohm.controller.GameBuilder.*;
import static com.hohm.utility.JsonParser.itemPrint;
import static com.hohm.utility.JsonParser.parse;

public class TextInteractor {

    /**
     * go is used to move the player from room to room
     * Go checks to see if the user input contains a valid direction and if the exit is available
     * If both are true the player is moved to that room, otherwise the player remains in the current room
     * @param input - text input from the player
     * @param currentRoom - the current room the player is in
     */
    public static void go(String input, MemeRoom currentRoom) {

        String[] rooms = GameBuilder.rooms.keySet().toArray(new String[0]);
        String room = String.join(" ", rooms);

        List<String> roomArr = Arrays.stream(room.split(" ")).map(String::toLowerCase).collect(Collectors.toList());
        Set<String> set = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));

        set.retainAll(roomArr);
        set.retainAll(Arrays.asList(currentRoom.getExit()));

        if (set.iterator().hasNext()) {
            GameLoop.previousRoom = currentRoom;
            player.setRoom(set.iterator().next());
            printSeparator();
        } else {
            printSeparator();
            System.out.println("INVALID DIRECTION: Try typing 'WHERE AM I' for a list of valid exits\n");
        }
    }

    /**
     * Look checks to see what the player would like to look at and then provides descriptive output accordingly
     * If the item or NPC does not exist the player gets a prompt to use look room see a description of the room
     * @param input - input received from the player
     * @param currentRoom - The current room the player is in
     * @throws IOException - Exception thrown if there is input output errors
     */
    public static void look(String input, MemeRoom currentRoom) throws IOException {

        String[] currentItems = player.getItems();
        if(input.contains("room")){
            printSeparator();
            description(currentRoom);
        }else if(input.contains("inventory")){
            printSeparator();
            System.out.println("Inventory: " + Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""));
        }
        else if (input.contains(Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""))) {
            printSeparator();
            String itemLookUp = Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", "");
            itemPrint(itemLookUp);
        }else{
            lookItem(currentRoom,input);
            lookNPC(currentRoom, input);
        }

    }

    /**
     * Look item checks to see if the user input contains an item name/altname
     * If user input is valid it prints the description of the item
     * @param currentRoom - Room the player is currently in
     * @param input - input obtained from the player
     */
    public static void lookItem(MemeRoom currentRoom, String input){
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
            System.out.println("That item may exist in the world, but you can't look at it here.\nTry using 'look room' to check your surroundings.");
        }
    }

    /**
     * Look NPC checks to see if the user input contains a valid npc name and if they are in the current room
     * If user input is valid it prints a description of the npc in the room
     * @param currentRoom - Room the player is currently in
     * @param input - input obtained from the player
     */
    public static void lookNPC(MemeRoom currentRoom, String input){
        try{
            Set<Map.Entry<String,Meme>> set = memes.entrySet().stream().filter(a->a.getValue().getRoom().equals(currentRoom.getTitle())).collect(Collectors.toSet());
            List<String> inputArr = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toList());
            Meme currentMeme = set.iterator().next().getValue();
            String[] altName = currentMeme.getAltName().keySet().toArray(new String[0]);
            inputArr.retainAll(List.of(altName));

            if(currentMeme.getRoom().equals(currentRoom.getTitle()) && input.contains(set.iterator().next().getKey()) || inputArr.iterator().hasNext()){
                printSeparator();
                System.out.println(currentMeme.getDescription());
            }else{
                printSeparator();
                System.out.println("There's no one here by that name, must be a dream, try using 'look room' to check your surroundings");
            }
        }catch (NoSuchElementException e){
            //Do Nothing if the NPC isn't in the room
        }
    }

    /**
     * Get checks to see if the user input contains an item name/altname.
     * It then checks to see if the check complete objective is completed.
     * If the check complete objective is completed the user obtains the item.
     * If the check complete objective is not complete the user is unable to obtain the item and loses HP.
     * @param input - input obtained from the user
     * @param currentRoom - the current room the player is in
     */
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
                    //TODO fix prereqNotMet descriptions to do damage instead of instakill
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqNotMet"));
                    player.hit(2);
                    if(player.getHp() <= 0){
                        player.setRoom("dead");
                    }
                }
            } else {
                printSeparator();
                System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
            }
        } catch (NullPointerException e) {
            printSeparator();
            e.printStackTrace();
            System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
        }

    }

    /**
     * Use checks to see if the useItem is applicable to the check complete objective (which is the objective associated with an item to be used)
     * If the item is, the item is removed from the inventory and check complete is marked as completed
     * If the item is not applicable the user is told they are unable to use that item, or that they don't have the item
     * @param input - input passed in from the user
     * @param currentRoom - The current room that the player is in
     */
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

    //TODO - place the riddles in a json file and print from the json document
    //TODO - refactor talk so that it is loosely coupled and npc's can be added without having to update this method
    public static void talk(String input, MemeRoom currentRoom) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        JsonNode dialogue = parse(classLoader.getResourceAsStream("npc.json"));
        int random = (int) (Math.random() * 3);
        try {
            if (input.contains("doge")) {
                if (currentRoom.getTitle().equals("kitchen")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("doge").get("dialogue");
                    printSeparator();
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("kermit")) {
                if (currentRoom.getTitle().equals("dining")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("kermit").get("dialogue");
                    printSeparator();
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("cat")) {
                if (currentRoom.getTitle().equals("living")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("grumpycat").get("dialogue");
                    printSeparator();
                    System.out.println(returnDialogue.get(random));
                }
            } else if (input.contains("boromir")) {
                if (currentRoom.getTitle().equals("office")) {
                    ArrayNode returnDialogue = (ArrayNode) dialogue.get("boromir").get("dialogue");
                    printSeparator();
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
                            if(third.contains("darkness")){
                                player.setHasSecretKey(true);
                                System.out.println("You have completed smeagol's riddle challenge.. he sits there with disbelief as you steal his ring of keys.");
                                System.out.println("You quickly exit the room and the door slowly fades away, you hear a faint whisper *precioussss*");
                                player.setRoom("hallway");
                                rooms.remove("bathroom");
                            }
                        }
                    }
                    else{
                        System.out.println("What did you say?.. Leave now and never come back! We told him to go away and away he goes precious!");
                        player.setFailedKey(true);
                        System.out.println("You quickly exit the room and the door slowly fades away, you hear a faint whisper *precioussss*");
                        player.setRoom("hallway");
                        rooms.remove("bathroom");
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

    /**
     * Description is used to print the appropriate output based on the status of the room.
     * If the room is the hallway a separate status is printed based on the user's inventory.
     * This is because the current item shows the progress in the game.
     * If the room is complete then the rooms final state is printed
     * Otherwise the description is based on objectives that are in the room that still must be completed
     * @param currentRoom - The current room that the player is in
     */
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

    /**
     * checkComplete is used to determine if all objectives have been completed in the room for a standard room
     * if the objectives of the room have been completed then the rooms complete status is set to true
     * Otherwise no change is made
     * @param currentRoom - The current room that the player is in
     */
    public static void checkComplete(MemeRoom currentRoom) {
        Map<String, Map<String, String>> objectives = currentRoom.getObjectives();
        if (objectives.get("itemFound").get("complete").equals("true") && objectives.get("clueFound").get("complete").equals("true")) {
            currentRoom.setComplete(true);
        }
    }

    /**
     * Print Separator is used to clear the console and display information to the player.
     * Player should see the following at the top of the screen: Current Room, Inventory, Clues Found, HP
     */
    public static void printSeparator() {
        String printRoom = player.getRoom();

        //Updating room print if case is living, dining, basement, or depths to print out a logical statement for the player
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
        }

        ClearScreen.clearConsole();
        String dash = "= = ".repeat(29);
        String printSeparator = String.format("Current Room: %s %12sInventory: %s %12sClues Found: %s %12sPlayer HP: %s"
                , printRoom.toUpperCase()
                , "", player.getItems()[0].toUpperCase()
                , "", player.getClues()
                , "", player.getHp());

        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash + "\n\n");

    }

    /**
     * commandList will show the player the commands that they can use for the game
     * Command list should be displayed at the bottom of the screen right before player input is read
     */
    public static void commandList(){
        String dash = "= = ".repeat(29);
        String availableCommands = " BASIC COMMANDS: Look Room or <Item/npc>  |  Go <Room>  |  Get <Item>  |  Use <Item>  | Save  | Quit | Help";
        System.out.println("\n\n" + dash);
        System.out.println(availableCommands);
        System.out.println(dash);
    }
}
