package com.hohm.utility;

/**
 * Authors: Daniel An, Agustin Duran, Kaitlyn Fernelius
 * Clear screen is used to clear the console so that the player only sees relevant text printed
 */
public class ClearScreen {
    /**
     * clearConsole works in a console based environment.
     * First it will check the operating system and perform the command based on the OS
     * (for Windows command is "cls" for all other OS it's "clear)
     */
    public static void clearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name");

            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
