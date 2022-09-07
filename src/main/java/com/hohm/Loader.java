package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.models.MemeRoom;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Loader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, MemeRoom> load() throws IOException {

        File data = new File("saved_data/saved_Rooms.json");
        JsonNode rootNode = objectMapper.readTree(data);
        List<String> rooms = new ArrayList<>();
        Iterator<String> iterator = rootNode.fieldNames();
        iterator.forEachRemaining(rooms::add);

        Map<String, MemeRoom> roomMap = new HashMap<>();
        for (String room : rooms) {
            JsonNode roomNode = rootNode.path(room);
            roomMap.put(room, objectMapper.convertValue(roomNode, MemeRoom.class));
        }

        return roomMap;
    }
}
