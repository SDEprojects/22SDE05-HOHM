package com.hohm;

import com.hohm.models.MemeRoom;
import com.hohm.models.Player;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import static com.hohm.Generator.generatePlayer;
import static com.hohm.Generator.generateRooms;
import static com.hohm.TextInteractor.printSeparator;

public class GameInit {

    static String[] startingItems = {"bucket"};
    public static Player player;
    public static Map<String, MemeRoom> rooms;

    public static void gameInit() throws IOException {

        while (true) {
            File saved = new File("saved_data/saved_Rooms.json");
            if (saved.exists()) {
                System.out.print("Would you like to restore your previous progress (y/n)?: ");
                String response = GameRunner.reader.readLine();
                if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                    try {
                        rooms = Loader.loadRooms();
                        player = Loader.loadPlayer();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    printSeparator();
                    GameRunner.run();
                    break;
                }
            }
            System.out.print("Would you like to start a new game (y/n)?: ");
            String confirm = GameRunner.reader.readLine();
            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                try {
                    rooms = generateRooms();
                    player = generatePlayer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println();
                printSeparator();
                UtilLoader.utilPrint("begin story");
                GameRunner.run();
                break;
            } else if (confirm.equalsIgnoreCase("n") || confirm.equalsIgnoreCase("no")) {
                System.out.println("That's unfortunate, we hope to meme with you again soon!");
                break;
            } else {
                System.out.println("That wasn't valid input... \n");
            }
        }
    }
}
