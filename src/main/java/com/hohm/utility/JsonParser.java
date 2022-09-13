package com.hohm.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.model.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.hohm.controller.GameBuilder.player;

/**
 * Authors: Daniel An, Kaitlyn Fernelius, Agustin Duran
 * The JsonParser does the work of parsing a json file and mapping it to the appropriate object
 * or returning a certain value
 */
public final class JsonParser {

    static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parse is simply used to remove redundancy in reading the tree of a file
     * @param file - InputStream of a file
     * @return return a JsonNode of the file
     * @throws IOException - IOException thrown when file cannot be read
     */
    public static JsonNode parse(InputStream file) throws IOException {
        return  objectMapper.readTree(file);
    }

    /**
     * Parse is simply used to remove redundancy in reading the tree of a file
     * @param file - File object of a file
     * @return return a JsonNode of the file
     * @throws IOException - IOException thrown when file cannot be read
     */
    public static JsonNode parse(File file) throws IOException {
        return objectMapper.readTree(file);
    }

    /**
     * utilPrint is used to print a single node of the file based on the nodeKey passed in
     * @param nodeKey - string that points to a single key in the utils file
     * @throws IOException - thrown if file cannot be read
     */
    public static void utilPrint(String nodeKey) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = parse(file);
        System.out.println("\n" + node.get(nodeKey).textValue() + "\n");
    }

    /**
     * mapPrint is used to find and print the applicable map for the room, special cases are if riddlesBottom in the dark
     * has been completed
     * @param nodeKey - key of the map to be printed
     * @throws IOException - thrown if the map is not found
     */
    public static void mapPrint(String nodeKey) throws IOException{
        InputStream file = classLoader.getResourceAsStream("maps.json");
        JsonNode node = parse(file);
        if(node.get(nodeKey).equals(node.get("hallway")) && player.getHasSecretKey()){
            System.out.println(node.get("frontdoor").textValue());
        }else if (node.get(nodeKey).equals(node.get("hallway")) && player.getFailedKey()) {
            System.out.println(node.get("nobathroom").textValue());
        }else{
        System.out.println(node.get(nodeKey).textValue());
        }
    }

    /**
     * unlike utilPrint, utilNodeReturn() returns the string value to be used for comparison when the player is attempting to
     * enter the basement.
     * @return String of the utilNodeReturn()
     * @throws IOException - thrown if the node is not found
     */
    public static String utilNodeReturn(String nodeName) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = parse(file);
        return node.get(nodeName).asText();
    }

    /**
     * itemPrint is used to print the description of an item found in the utils file.
     * @param item - string value of the item to look up
     * @throws IOException - Thrown if the item can't be found
     */
    public static void itemPrint(String item) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode description = parse(file);
        System.out.println(description.get("items").get(item).textValue());
    }

    /**
     * Commands is used to return the Array of synonyms associated with a certain command
     * @return - synonym array list found in utils file
     * @throws IOException - thrown if file cannot be found
     */
    public static JsonNode nodeValueReturn(String nodeName, String fileName) throws IOException {
        InputStream file = classLoader.getResourceAsStream(fileName);
        return parse(file).get(nodeName);
    }

    /**
     * objMapFromFile returns a map of a list of objects contained in a file, used to create rooms and npcs
     * @param fileName - the name of the file to get from the resources
     * @param classType - The type of class that will be held in the map as the value in the key value pair
     * @return - map of the objects from the file
     * @param <T> - Made generic to be able to create maps of multiple object types
     * @throws IOException - thrown if the file can't be found
     */
    public static <T> Map<String, T> objMapFromFile(String fileName, Class<T> classType) throws IOException {
        List<String> objects = new ArrayList<>();
        Map<String, T> returnMap = new HashMap<>();

        InputStream file = classLoader.getResourceAsStream(fileName);
        JsonNode rootNode = null;

        if (file == null) {
            File altFile = new File(fileName);
            rootNode = parse(altFile);
        } else {
            rootNode = parse(file);
        }

        Iterator<String> iterator = rootNode.fieldNames();
        iterator.forEachRemaining(objects::add);

        for (String obj : objects) {
            JsonNode objNode = rootNode.path(obj);
            returnMap.put(obj, objectMapper.convertValue(objNode, classType));
        }

        return returnMap;
    }

    /**
     * playerFromfile returns a single player object created from a resource or save file.
     * @param fileName - name of the file the player object is held in
     * @return - player object created from the file
     * @throws IOException - thrown if the file can't be found
     */
    public static Player playerFromFile(String fileName) throws IOException {
        InputStream file = classLoader.getResourceAsStream(fileName);
        JsonNode rootNode = null;
        if(file == null){
            File altFile = new File(fileName);
            rootNode = parse(altFile);
        }else{
            rootNode = parse(file);
        }
        return objectMapper.convertValue(rootNode.get("player"),Player.class);
    }
}
