package com.hohm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;


public class GameStart {

    public static void main(String[] args) throws Exception {

        GameRunner.gameInit();

        //Code below is to parse the JSON file and return single string value
//        File file = new File("src/main/resources/utils.json");
//        JsonNode node = Json.parse(file);
//        System.out.println(node.get("intro").textValue());
    }
}
