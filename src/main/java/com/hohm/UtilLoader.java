package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

// UtilLoader will be used
public final class UtilLoader {
    // UtilLoader will be used from the user to call the help functions.
    // Intro will also be printed.
    public static File file = new File("src/main/resources/utils.json");
    public static void intro() throws IOException {
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("intro").textValue() + "\n");
    }

    public static void help() throws IOException {
        JsonNode node = Json.parse(file);
        System.out.println("\n" + node.get("help").textValue() + "\n");
    }

}
