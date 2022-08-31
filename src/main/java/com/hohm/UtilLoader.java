package main.java.com.hohm;

public final class UtilLoader {

    public static void intro() {
        String intro = "\"Welcome to the thrilling game House of Haunted Memes!\n" +
                "The objective of the game is to leave the house but you will be obstructed by memes!\n" +
                " Use your wit to find your way through the house and past  the memes.\"";
        System.out.println();
    }

    public static void help() {
        go();
        take();
        use();
        quit();
    }

    private static void go() {
        System.out.println("Go: Can take you to different rooms");
    }

    private static void take() {
        System.out.println("Take: take any item available.");
    }

    private static void use() {
        System.out.println("Use: Allows items to be used.");
    }
    private static void quit(){
        System.out.println("Quit: Quits the game, #EmotionalDamage.");
    }
}
