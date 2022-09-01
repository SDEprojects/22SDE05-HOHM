package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.models.MemeRoom;
import com.hohm.models.Player;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class GameRunner {
    //Creating game objects to reference during game play
    static String[] startingItems = {"bucket"};
    public static Player player = new Player("noob",startingItems, "hallway");

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static Map<String, MemeRoom> rooms;

    static {
        try {
            rooms = Json.generateRooms();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }public GameRunner() throws IOException {
    }

    public static void gameInit() throws IOException {

        while(true){
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = reader.readLine();
            if(confirm.toLowerCase().equals("y") || confirm.toLowerCase().equals("yes")){
                GameRunner.run();
                break;
            }else if(confirm.toLowerCase().equals("n") || confirm.toLowerCase().equals("no")){
                System.out.println("That's unfortunate, we hope to meme with you again soon!");
                break;
            }else{
                System.out.println("That wasn't valid input... \n");
            }
        }
    }

    public static void run() throws IOException {

        //Initiating the game loop
        while (true){
            //Current room starts as the hallway
            MemeRoom currentRoom = rooms.get(player.getRoom());

            //Check room and check user inventory if hallway
            if(currentRoom.getTitle().equals("hallway")){
                String[] currentItem = player.getItems();
                System.out.println(currentRoom.getDescription().get(currentItem[0]));
            }else{
                if(!currentRoom.getComplete()){
                    System.out.println(currentRoom.getDescription().get("memeIncomplete"));
                }else{
                    System.out.println(currentRoom.getDescription().get("memeComplete"));
                }

            }

            System.out.print(">");

            //Wait for user input and then act based on what the user types
            String userInput = reader.readLine();

            //handle logic based on user input
            if(userInput.toLowerCase().equals("quit")){
                System.out.println("Thanks for playing come back soon!!");
                break;
            }else{
                parseText(userInput, currentRoom);
            }
        }

    }

    public static void parseText(String input, MemeRoom currentRoom) throws IOException {
        JsonNode node = Json.parse( new File("src/main/resources/utils.json"));
        node = node.get("commands");
        String goTo = String.valueOf(node.get("go")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]","");
        String lookAt = String.valueOf(node.get("look")).toLowerCase().strip().replaceAll("[\\[\\](){}\"]","");
        String [] inputArr = input.toLowerCase().strip().split(" ");

        if(input.equalsIgnoreCase("help")){
            UtilLoader.help();
        }
        else if(goTo.contains(inputArr[0])){
            System.out.println(input);
            go(input, currentRoom);
        }
        else if(lookAt.contains(inputArr[0])){
            System.out.println(input);
        }
        else if(input.contains("get")){
            //TODO create a way to examine items
        }
        else if(input.contains("use")){
            //TODO create a method that lets you use items
        }
        else if(input.contains("where am i")){
            System.out.printf("You are currently in the: %s%n", currentRoom.getTitle());
            System.out.printf("Your available exits are:%s%n", Arrays.toString(currentRoom.getExit()));
        }
        else{
            System.out.println("Please enter a valid command");
            UtilLoader.help();
        }
    }

    public static void go(String input, MemeRoom currentRoom){
        if(input.toLowerCase().contains("kitchen")){
            System.out.println("Going to the Kitchen");
            player.setRoom("kitchen");
        }
        else if(input.toLowerCase().contains("living room")){
            System.out.println("Going to the Living Room");
            player.setRoom("livingroom");
        }
        else if(input.toLowerCase().contains("dining room")){
            System.out.println("Going to the Dining Room");
            player.setRoom("diningroom");
        }
        else if(input.toLowerCase().contains("hallway")){
            System.out.println("Going to the Hallway");
            player.setRoom("hallway");
        }
        else{
            System.out.println("You can't really go there, must be in your head");
        }
    }
}
