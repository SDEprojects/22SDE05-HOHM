package com.hohm;

public class GameStart {

    public static void main(String[] args) throws Exception {
        ClearScreen.ClearConsole();
        UtilLoader.utilPrint("title");
        UtilLoader.utilPrint("intro");
        UtilLoader.utilPrint("help");
        GameInit.gameInit();
    }
}
