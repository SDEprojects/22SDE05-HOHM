package com.hohm.controller;

import com.hohm.utility.ClearScreen;

import static com.hohm.controller.GameBuilder.player;

public class PrintSeparators {
    /**
     * Print Separator is used to clear the console and display information to the player.
     * Player should see the following at the top of the screen: Current Room, Inventory, Clues Found, HP
     */
    public static void printSeparatorMain() {
        String printRoom = player.getRoom();

        //Updating room print if case is living, dining, basement, or depths to print out a logical statement for the player
        switch (printRoom) {
            case "living":
            case "dining":
                printRoom = player.getRoom() + " room";
                break;
            case "basement":
                printRoom = player.getRoom() + " door";
                break;
            case "depths":
                printRoom = "basement " + player.getRoom();
        }

        ClearScreen.clearConsole();
        String dash = "= = ".repeat(29);
        String printSeparator = String.format("Current Room: %s %12sInventory: %s %12sClues Found: %s %12sPlayer HP: %s"
                , printRoom.toUpperCase()
                , "", player.getItems()[0].toUpperCase()
                , "", player.getClues()
                , "", player.getHp());

        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash + "\n\n");

    }

    /**
     * printSeparatorBottom will show the player the commands that they can use for the game
     * Command list should be displayed at the bottom of the screen right before player input is read
     */
    public static void printSeparatorBottom() {
        String dash = "= = ".repeat(29);
        String availableCommands = " BASIC COMMANDS:   Look Room  |    Go <room>  |    Get <item>  |    Use <item>  |   Talk <npc>  |  Save  |   Help";
        System.out.println("\n\n" + dash);
        System.out.println(availableCommands);
        System.out.println(dash);
    }

    /**
     * basementDoorBottom shows the available commands for the player when they are attempting to enter into the basement
     * Through the basement door.
     */
    public static void basementDoorBottom() {
        String dash = "= = ".repeat(29);
        String availableCommands = "TO RETURN TO KITCHEN: type 'kitchen' ";
        System.out.println("\n\n" + dash);
        System.out.println(availableCommands);
        System.out.println(dash);
    }

    /**
     * RiddlesBottom shows the player a prompt during the riddles in the dark sequence
     */
    public static void riddlesBottom() {
        String dash = "= = ".repeat(29);
        String availableCommands = " WHAT IS YOUR ANSWER TO THE RIDDLE? ";
        System.out.println("\n\n" + dash);
        System.out.println(availableCommands);
        System.out.println(dash);
    }

    /**
     * Encounter Separator Creates a Header for the player to know their status during the final boss encounter
     */
    public static void encounterSeparator(){
        ClearScreen.clearConsole();
        String dash = "= = ".repeat(29);
        String printSeparator = String.format("Player: Noble Cleric%7sAvailable Attacks: Sword, Spell%7sHas Advantage: %s%7sHP: %s",
                "","",player.getHasAdvantage(),"",player.getHp());
        System.out.println(dash);
        System.out.println(printSeparator);
        System.out.println(dash);
    }
}
