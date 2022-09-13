package com.hohm.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.model.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameBuilder.rooms;

public class Save {

    /**
     * This functions saves current state of the game as a JSON file
     * A new file is created or overwritten, with the current state of the instanced objects of the room and characters.
     * @throws IOException
     */
    public static void save() throws IOException {
        try {
            Map<String, Player> playerMap = new HashMap<>();
            playerMap.put("player", player);
            ObjectMapper objectMapper = new ObjectMapper();
            File savedRooms = new File("saved_data/saved_Rooms.json");
            File savedCharacters = new File("saved_data/saved_Characters.json");
            objectMapper.writeValue(new FileOutputStream(savedRooms, false), rooms);
            objectMapper.writeValue(new FileOutputStream(savedCharacters, false), playerMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
