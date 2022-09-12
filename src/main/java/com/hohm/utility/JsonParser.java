package com.hohm.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hohm.model.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.hohm.controller.GameBuilder.player;


public final class JsonParser {

    static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode parse(InputStream file) throws IOException {
        return  objectMapper.readTree(file);
    }

    public static JsonNode parse(File file) throws IOException {
        return objectMapper.readTree(file);
    }
    public static void utilPrint(String nodeKey) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = parse(file);
        System.out.println("\n" + node.get(nodeKey).textValue() + "\n");
    }

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


    public static String code() throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = parse(file);
        return node.get("basement code").asText();
    }

    public static void itemPrint(String item) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode description = parse(file);
        System.out.println(description.get("items").get(item));
    }

    public static JsonNode commands() throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        return parse(file).get("commands");
    }

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
