package com.hohm;
// UtilLoader will be used
public final class UtilLoader {
    // UtilLoader will be used from the user to call the help functions.
    // Intro will also be printed.

    public static void intro() {
        String intro = "Welcome to the thrilling game House of Haunted Memes!\n" +
                "The objective of the game is to leave the house but you will be obstructed by memes!\n" +
                " Use your wit to find your way through the house and past the memes.";
        System.out.println(intro);
    }
    // Possibly be able to use help -go, and it calls separate methods.
    public static void help() {
        go();
        take();
        use();
        quit();
    }

    private static void go() {
        String goPrint = "Go: Can take you to different rooms";
        System.out.println(goPrint);
    }

    private static void take() {
        String takePrint = "Take: take any item available.";
        System.out.println(takePrint);
    }

    private static void use() {
        String usePrint = "Use: Allows items to be used.";
        System.out.println(usePrint);
    }
    private static void quit(){
        String quitPrint = "Quit: Quits the game, #EmotionalDamage.";
        System.out.println(quitPrint);
    }
}
