package com.hohm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.models.MemeRoom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class Save {

    public static void save(Map<String, MemeRoom> currentRooms) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File savedRooms = new File("saved_data/saved_Rooms.json");
            objectMapper.writeValue(new FileOutputStream(savedRooms, false), currentRooms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
