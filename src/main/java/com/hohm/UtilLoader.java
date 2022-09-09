package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.InputStream;

import static com.hohm.GameBuilder.player;

// UtilLoader will be used
public final class UtilLoader {
    // UtilLoader will be used from the user to call the help functions.
    // Intro will also be printed.
    static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    public static void utilPrint(String nodeKey) throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = ObjectGenerator.parse(file);
        System.out.println("\n" + node.get(nodeKey).textValue() + "\n");
    }

    public static void mapPrint(String nodeKey) throws IOException{
        InputStream file = classLoader.getResourceAsStream("maps.json");
        JsonNode node = ObjectGenerator.parse(file);
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
        JsonNode node = ObjectGenerator.parse(file);
        return node.get("basement code").asText();
    }
}
