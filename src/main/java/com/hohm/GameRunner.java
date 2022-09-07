package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.hohm.models.MemeRoom;
import com.hohm.models.Player;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Objects;

import static com.hohm.TextInteractor.*;


public class GameRunner {
    //Creating game objects to reference during game play
    static String[] startingItems = {"bucket"};
    public static int clueCount = 0;
    public static Player player = new Player("noob", startingItems, "hallway");
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void run() throws IOException {
        boolean newGame = false;
        UtilLoader.startText();
        GameRunner.musicPlayer();
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
                MemeRoom currentRoom = GameInit.rooms.get(player.getRoom());
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
                if (userInput.equalsIgnoreCase("quit")) {
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

    public static void musicPlayer(){
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream musicStream = classLoader.getResourceAsStream("background.wav");
            InputStream bufferedMusic = new BufferedInputStream(musicStream);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedMusic);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setMicrosecondPosition(0);
            clip.start();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
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
            File savedRooms = new File("saved_data/saved_Rooms.json");
            savedRooms.getParentFile().mkdirs();
            if (!savedRooms.createNewFile()) {
                System.out.print("You already have saved files, would you like to overwrite (y/n)?: ");
                String saveConfirm = reader.readLine();
                if (saveConfirm.equalsIgnoreCase("y") || saveConfirm.equalsIgnoreCase("yes")) {
                    Save.save((GameInit.rooms));
                    System.out.println("You have saved your game.");
                } else {
                    System.out.println("You have chosen not to overwrite");
                }
            } else {
                Save.save(GameInit.rooms);
                System.out.println("You have saved your game");
            }
        }
        else if (input.contains("where am i")) {
            printSeparator();
            UtilLoader.houseMap(currentRoom.getTitle());
        } else {
            System.out.println("Please enter a valid command");
            UtilLoader.help();
        }
    }
}
