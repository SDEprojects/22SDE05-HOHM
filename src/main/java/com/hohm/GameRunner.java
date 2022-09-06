package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.models.MemeRoom;
import com.hohm.models.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.hohm.TextInteractor.*;


public class GameRunner {
    //Creating game objects to reference during game play
    static String[] startingItems = {"bucket"};
    public static int clueCount = 0;
    public static Player player = new Player("noob", startingItems, "hallway");
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static Map<String, MemeRoom> rooms;

    static {
        try {
            rooms = Json.generateRooms();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void gameInit() throws IOException {

        while (true) {
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = reader.readLine();
            if (confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")) {
                System.out.println();
                printSeparator();
                GameRunner.run();
                break;
            } else if (confirm.toLowerCase().equals("n") || confirm.toLowerCase().equals("no")) {
                System.out.println("That's unfortunate, we hope to meme with you again soon!");
                break;
            } else {
                System.out.println("That wasn't valid input... \n");
            }
        }
    }

    public static void run() throws IOException {
        boolean newGame = false;
        UtilLoader.startText();
        //Initiating the game loop
        while (true) {
            //Current room starts as the hallway
            if (player.getRoom().equals("dead")) {
                System.out.println("Seems you have died... how very unfortunate. Would you like to play again (y/n)?");
                System.out.print(">");
                String playAgain = reader.readLine();
                if(playAgain.equals("y") || playAgain.equals("yes")){
                    newGame = true;
                }
                break;

            } else {
                MemeRoom currentRoom = rooms.get(player.getRoom());
                //Check room and check user inventory if hallway
                if (currentRoom.getTitle().equals("hallway")) {
                    String[] currentItem = player.getItems();
                    if(Objects.equals(currentItem[0], "[]")){
                        System.out.println(currentRoom.getDescription().get("nullHallway"));
                    }else{
                        System.out.println(currentRoom.getDescription().get(currentItem[0]));
                    }

                }

                System.out.print(">");

                //Wait for user input and then act based on what the user types
                String userInput = reader.readLine();

                //handle logic based on user input
                if (userInput.toLowerCase().equals("quit")) {
                    break;
                } else {
                    parseText(userInput, currentRoom);
                }
            }
        }
        if(newGame){
            player.setRoom("hallway");
            player.setItems(startingItems);
            newGame = false;
            run();
        }else{
            System.out.println("Thanks for playing, we hope to meme with you again soon!");
        }
    }

    public static void parseText(String input, MemeRoom currentRoom) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream utils = classLoader.getResourceAsStream("utils.json");
        JsonNode node = Json.parse(utils);
        node = node.get("commands");

        String goTo = String.valueOf(node.get("go")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String lookAt = String.valueOf(node.get("look")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]", "");
        String[] inputArr = input.toLowerCase().strip().split(" ");

        if (input.equalsIgnoreCase("help")) {
            UtilLoader.help();
        } else if (goTo.contains(inputArr[0])) {
            go(input, currentRoom);
        } else if (lookAt.contains(inputArr[0])) {
            look(input);
        } else if (input.contains("get")) {
            get(input, currentRoom);
        } else if (input.contains("use")) {
            use(input, currentRoom);
        }
        else if (input.contains("talk")) {
            talk(input,currentRoom);
        }
        else if (input.contains("save")) {
            Save.save(rooms);
            System.out.println("You have saved the room state");
        }
        else if (input.contains("where am i")) {
            System.out.printf("You are currently in the: %s%n", currentRoom.getTitle());
            System.out.printf("Your available exits are: %s%n", Arrays.toString(currentRoom.getExit()).replaceAll("[\\[\\](){}\"]", ""));
        } else {
            System.out.println("Please enter a valid command");
            UtilLoader.help();
        }
    }
}
