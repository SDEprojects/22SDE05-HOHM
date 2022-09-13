package com.hohm.controller;

import com.hohm.model.Meme;
import com.hohm.model.MemeRoom;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hohm.controller.GameBuilder.*;
import static com.hohm.utility.JsonParser.itemPrint;
import static java.lang.System.exit;

public class TextInteractor {

    /**
     * go is used to move the player from room to room
     * Go checks to see if the user input contains a valid direction and if the exit is available
     * If both are true the player is moved to that room, otherwise the player remains in the current room
     *
     * @param input       - text input from the player
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
            PrintSeparators.printSeparatorMain();
        } else if(player.getHasSecretKey() && input.contains("front door")) {
            PrintSeparators.printSeparatorMain();
            System.out.println("Congrats you were able to let the house burn down without getting mimed..");
            exit(0);
        }
    }

    /**
     * Look checks to see what the player would like to look at and then provides descriptive output accordingly
     * If the item or NPC does not exist the player gets a prompt to use look room see a description of the room
     *
     * @param input       - input received from the player
     * @param currentRoom - The current room the player is in
     * @throws IOException - Exception thrown if there is input output errors
     */
    public static void look(String input, MemeRoom currentRoom) throws IOException {

        String[] currentItems = player.getItems();
        if (input.contains("room")) {
            PrintSeparators.printSeparatorMain();
            description(currentRoom);
        } else if (input.contains("inventory")) {
            PrintSeparators.printSeparatorMain();
            System.out.println("Inventory: " + Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""));
        } else if (input.contains(Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", ""))) {
            PrintSeparators.printSeparatorMain();
            String itemLookUp = Arrays.toString(currentItems).replaceAll("[\\[\\](){}\\\\\"]", "");
            itemPrint(itemLookUp);
        } else {
            lookItem(currentRoom, input);
            lookNPC(currentRoom, input);
        }

    }

    /**
     * Look item checks to see if the user input contains an item name/altname
     * If user input is valid it prints the description of the item
     *
     * @param currentRoom - Room the player is currently in
     * @param input       - input obtained from the player
     */
    public static void lookItem(MemeRoom currentRoom, String input) {
        try {
            String[] currentRoomItems = currentRoom.getItems().keySet().toArray(new String[0]);
            String[] currentRoomAltGet = currentRoom.getAltGet().keySet().toArray(new String[0]);

            Set<String> set = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toCollection(LinkedHashSet::new));
            set.retainAll(List.of(currentRoomAltGet));

            for (String item : currentRoomItems) {
                if (input.contains(item) || (set.iterator().hasNext()
                        && currentRoom.getAltGet().get(set.iterator().next()).equals(item))) {
                    PrintSeparators.printSeparatorMain();
                    System.out.println(currentRoom.getItems().get(item).get("description"));
                }
            }
        } catch (NullPointerException e) {
            PrintSeparators.printSeparatorMain();
            System.out.println("That item may exist in the world, but you can't look at it here.\nTry using 'look room' to check your surroundings.");
        }
    }

    /**
     * Look NPC checks to see if the user input contains a valid npc name and if they are in the current room
     * If user input is valid it prints a description of the npc in the room
     *
     * @param currentRoom - Room the player is currently in
     * @param input       - input obtained from the player
     */
    public static void lookNPC(MemeRoom currentRoom, String input) {
        try {
            Set<Map.Entry<String, Meme>> set = memes.entrySet().stream().filter(a -> a.getValue().getRoom().equals(currentRoom.getTitle())).collect(Collectors.toSet());
            List<String> inputArr = Arrays.stream(input.split(" ")).map(String::toLowerCase).collect(Collectors.toList());
            Meme currentMeme = set.iterator().next().getValue();
            String[] altName = currentMeme.getAltName().keySet().toArray(new String[0]);
            inputArr.retainAll(List.of(altName));

            if (currentMeme.getRoom().equals(currentRoom.getTitle()) && input.contains(set.iterator().next().getKey()) || inputArr.iterator().hasNext()) {
                PrintSeparators.printSeparatorMain();
                System.out.println(currentMeme.getDescription());
            } else {
                PrintSeparators.printSeparatorMain();
                System.out.println("There's no one here by that name, must be a dream, try using 'look room' to check your surroundings");
            }
        } catch (NoSuchElementException e) {
            //Do Nothing if the NPC isn't in the room
        }
    }

    /**
     * Get checks to see if the user input contains an item name/altname.
     * It then checks to see if the check complete objective is completed.
     * If the check complete objective is completed the user obtains the item.
     * If the check complete objective is not complete the user is unable to obtain the item and loses HP.
     *
     * @param input       - input obtained from the user
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
                    PrintSeparators.printSeparatorMain();
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqMet"));
                    System.out.println("You now have: " +
                            Arrays.toString(player.getItems()).replaceAll("[\\[\\](){}\"]", "").toUpperCase() + "\n");
                } else if (objComplete && objType.equals("clue")) {
                    if (currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                        player.incrementClues();
                        currentRoom.getObjectives().get("clueFound").put("complete", "true");
                    }
                    checkComplete(currentRoom);
                    PrintSeparators.printSeparatorMain();
                    System.out.println("CLUE FOUND:");
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqMet") + "\n");
                } else {
                    player.hit(1);
                    PrintSeparators.printSeparatorMain();
                    System.out.println(currentRoom.getItems().get(keyVal).get("prereqNotMet"));
                    if (player.getHp() <= 0) {
                        player.setRoom("dead");
                    }
                }
            } else {
                PrintSeparators.printSeparatorMain();
                System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
            }
        } catch (NullPointerException e) {
            PrintSeparators.printSeparatorMain();
            e.printStackTrace();
            System.out.printf("You are unable to get the %s... whatever that is%n%n", keyVal);
        }

    }

    /**
     * Use checks to see if the useItem is applicable to the check complete objective (which is the objective associated with an item to be used)
     * If the item is, the item is removed from the inventory and check complete is marked as completed
     * If the item is not applicable the user is told they are unable to use that item, or that they don't have the item
     *
     * @param input       - input passed in from the user
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
                PrintSeparators.printSeparatorMain();
                System.out.println(currentRoom.getObjectives().get("check complete").get("completed"));

            } else if (Arrays.asList(player.getItems()).contains(key[1])) {
                PrintSeparators.printSeparatorMain();
                System.out.printf("That's a nice thought to use the %s... won't do anything..%n", key[1]);
            } else {
                PrintSeparators.printSeparatorMain();
                System.out.printf("Might be nice to use the %s, but... you don't even have that!%n", key[1]);
            }
        } catch (NullPointerException e) {
            PrintSeparators.printSeparatorMain();
            System.out.println("You can't use that here...");
        }
    }

    /**
     * Talk is used to interact with available npc characters in the room.
     * If the character is located in the room then the player can get a randomly generated phrase from the NPC
     * If the NPC is smeagol the player can play a riddle game with him to obtain a key
     * @param input - input obtained from the user
     * @param currentRoom - the room the player is currently in
     */
    public static void talk(String input, MemeRoom currentRoom) {
        int random = (int) (Math.random() * 3);
        try {
            Set<Map.Entry<String, Meme>> set = memes.entrySet().stream().filter(a -> a.getValue().getRoom().equals(currentRoom.getTitle())).collect(Collectors.toSet());
            Meme currentMeme = set.iterator().next().getValue();
            if (input.contains("smeagol") && currentRoom.getTitle().equals("bathroom")) {
                    RiddleEncounter.riddlesInTheDark();
                    PrintSeparators.printSeparatorMain();
            }
            if (currentMeme.getRoom().equals(currentRoom.getTitle()) && input.contains(set.iterator().next().getKey())) {
                PrintSeparators.printSeparatorMain();
                System.out.println(currentMeme.getDialogue()[random]);
            }
        } catch (NoSuchElementException | IOException e) {
            PrintSeparators.printSeparatorMain();
            System.out.println("There's no one to talk to here.");
        }
    }

    /**
     * Description is used to print the appropriate output based on the status of the room.
     * If the room is the hallway a separate status is printed based on the user's inventory.
     * This is because the current item shows the progress in the game.
     * If the room is complete then the rooms final state is printed
     * Otherwise the description is based on objectives that are in the room that still must be completed
     *
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
            } else if (currentRoom.getObjectives().get("check complete").get("complete").equals("false")) {
                System.out.println(currentRoom.getDescription().get("memeIncomplete"));
            } else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("false")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {
                System.out.printf("%s%n%s%n",
                        currentRoom.getObjectives().get("itemFound").get("incomplete"),
                        currentRoom.getObjectives().get("clueFound").get("incomplete"));
            } else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("true")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("false")) {

                System.out.println(currentRoom.getObjectives().get("clueFound").get("incomplete"));
            } else if (currentRoom.getObjectives().get("itemFound").get("complete").equals("false")
                    && currentRoom.getObjectives().get("clueFound").get("complete").equals("true")) {
                System.out.println(currentRoom.getObjectives().get("itemFound").get("incomplete"));
            }
        }
    }

    /**
     * checkComplete is used to determine if all objectives have been completed in the room for a standard room
     * if the objectives of the room have been completed then the rooms complete status is set to true
     * Otherwise no change is made
     *
     * @param currentRoom - The current room that the player is in
     */
    public static void checkComplete(MemeRoom currentRoom) {
        Map<String, Map<String, String>> objectives = currentRoom.getObjectives();
        if (objectives.get("itemFound").get("complete").equals("true") && objectives.get("clueFound").get("complete").equals("true")) {
            currentRoom.setComplete(true);
        }
    }

}
