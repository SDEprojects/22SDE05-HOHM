package com.hohm.controller;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameBuilder.rooms;
import static com.hohm.utility.JsonParser.nodeValueReturn;

/**
 * RiddleEncounter is used to start the riddles in the dark mini-game
 */
public class RiddleEncounter {

    /**
     * Riddles in the dark begins a riddle sequence that the player starts with Smeagol.
     * If the player wins they obtain a secret key that allows them to leave the house.
     * If they lose, they lose the opportunity to get the key and must win the game through battling the boss.
     * @throws IOException - thrown if input stream doesn't get the user input correctly
     */
    static void riddlesInTheDark() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        PrintSeparators.printSeparatorMain();
        System.out.println("You want to play a game of riddles with Smeagol? (y/n)\n");
        PrintSeparators.printSeparatorBottom();
        System.out.print(">");
        String userAnswer = reader.readLine();

        if (userAnswer.equals("y") || userAnswer.equals("n")) {
            for (int i = 1; i < 4; i++) {
                JsonNode questionNode = nodeValueReturn(String.valueOf(i), "riddles.json");
                PrintSeparators.printSeparatorMain();
                System.out.println(questionNode.get("riddle").textValue());
                PrintSeparators.riddlesBottom();
                System.out.print(">");
                String riddleAnswer = reader.readLine().toLowerCase();

                //A wrong answer and the player loses riddlesBottom in the dark
                if (!riddleAnswer.contains(questionNode.get("answer").textValue())) {
                    PrintSeparators.printSeparatorMain();
                    System.out.println(nodeValueReturn("lose", "riddles.json").textValue());
                    player.setFailedKey(true);
                    PrintSeparators.printSeparatorBottom();
                    System.out.print(">");
                    reader.readLine();
                    player.setRoom("hallway");
                    rooms.remove("bathroom");
                    break;
                }

                //All answers are correct the player wins riddlesBottom in the dark
                if (i == 3 && riddleAnswer.contains(questionNode.get("answer").textValue())) {
                    player.setHasSecretKey(true);
                    PrintSeparators.printSeparatorMain();
                    System.out.println(nodeValueReturn("win", "riddles.json").textValue());
                    PrintSeparators.printSeparatorBottom();
                    System.out.print(">");
                    reader.readLine();
                    player.setRoom("hallway");
                    rooms.remove("bathroom");
                }
            }
        } else {
            System.out.println("It doesn't think it's smart enough preciousss.....We are the smart ones here.");
        }

    }

}
