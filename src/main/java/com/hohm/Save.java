package com.hohm;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.hohm.GameBuilder.player;
import static com.hohm.GameBuilder.rooms;

public class Save {

    public static void save() throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File savedRooms = new File("saved_data/saved_Rooms.json");
            File savedCharacters = new File("saved_data/saved_Characters.json");
            objectMapper.writeValue(new FileOutputStream(savedRooms, false), rooms);
            objectMapper.writeValue(new FileOutputStream(savedCharacters, false), player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
