package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.models.MemeRoom;
import com.hohm.models.Player;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Generator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode parse(InputStream file) throws IOException {
        return  objectMapper.readTree(file);
    }

    //allows you to parse a JSON file and create an object from the information in the JSON file
    public static Map<String, MemeRoom> generateRooms() throws IOException {
        //String pathName = String.format("src/main/resources/%s.json",roomName);
        // Dynamically create String array/map from JSON instead

        List<String> rooms = new ArrayList<>();

        //Reading the rooms.json file and generating a list of rooms
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream file = classLoader.getResourceAsStream("rooms.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);
        Iterator<String> iterator = rootNode.fieldNames();
        iterator.forEachRemaining(rooms::add);
        Map<String, MemeRoom> roomMap = new HashMap<>();

        //Populating the arraylist with the room objects
        for (String room : rooms) {
            JsonNode roomNode = rootNode.path(room);
            roomMap.put(room, objectMapper.convertValue(roomNode, MemeRoom.class));
        }
        return roomMap;
    }

    public static Player generatePlayer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream file = classLoader.getResourceAsStream("characters.json");
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode playerNode = rootNode.get("player");
        Player player = objectMapper.convertValue(playerNode,Player.class);

        return player;
    }
}
