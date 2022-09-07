package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// UtilLoader will be used
public final class UtilLoader {
    // UtilLoader will be used from the user to call the help functions.
    // Intro will also be printed.
    static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    public static void intro() throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("intro").textValue() + "\n");
    }

    public static void help() throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = Json.parse(file);
        System.out.println("THINGS YOU SHOULD KNOW:");
        System.out.println(node.get("help").textValue() + "\n");
    }

    public static void startText() throws IOException {
        InputStream file = classLoader.getResourceAsStream("utils.json");
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("begin story").textValue() + "\n");
    }

    public static void houseMap(String input) throws IOException{
        InputStream file = classLoader.getResourceAsStream("maps.json");
        JsonNode node = Json.parse(file);
        System.out.println(node.get(input).textValue());
    }

}
