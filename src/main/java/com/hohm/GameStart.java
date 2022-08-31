package main.java.com.hohm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


public class GameStart {

    public static void main(String[] args) throws IOException {

        String title = "'##::::'##::'#######::'##::::'##::'######::'########:::::'#######::'########:                                                    \n" +
                " ##:::: ##:'##.... ##: ##:::: ##:'##... ##: ##.....:::::'##.... ##: ##.....::                                                    \n" +
                " ##:::: ##: ##:::: ##: ##:::: ##: ##:::..:: ##:::::::::: ##:::: ##: ##:::::::                                                    \n" +
                " #########: ##:::: ##: ##:::: ##:. ######:: ######:::::: ##:::: ##: ######:::                                                    \n" +
                " ##.... ##: ##:::: ##: ##:::: ##::..... ##: ##...::::::: ##:::: ##: ##...::::                                                    \n" +
                " ##:::: ##: ##:::: ##: ##:::: ##:'##::: ##: ##:::::::::: ##:::: ##: ##:::::::                                                    \n" +
                " ##:::: ##:. #######::. #######::. ######:: ########::::. #######:: ##:::::::                                                    \n" +
                "..:::::..:::.......::::.......::::......:::........::::::.......:::..::::::::                                                    \n" +
                "'##::::'##::::'###::::'##::::'##:'##::: ##:'########:'########:'########:::::'##::::'##:'########:'##::::'##:'########::'######::\n" +
                " ##:::: ##:::'## ##::: ##:::: ##: ###:: ##:... ##..:: ##.....:: ##.... ##:::: ###::'###: ##.....:: ###::'###: ##.....::'##... ##:\n" +
                " ##:::: ##::'##:. ##:: ##:::: ##: ####: ##:::: ##:::: ##::::::: ##:::: ##:::: ####'####: ##::::::: ####'####: ##::::::: ##:::..::\n" +
                " #########:'##:::. ##: ##:::: ##: ## ## ##:::: ##:::: ######::: ##:::: ##:::: ## ### ##: ######::: ## ### ##: ######:::. ######::\n" +
                " ##.... ##: #########: ##:::: ##: ##. ####:::: ##:::: ##...:::: ##:::: ##:::: ##. #: ##: ##...:::: ##. #: ##: ##...:::::..... ##:\n" +
                " ##:::: ##: ##.... ##: ##:::: ##: ##:. ###:::: ##:::: ##::::::: ##:::: ##:::: ##:.:: ##: ##::::::: ##:.:: ##: ##:::::::'##::: ##:\n" +
                " ##:::: ##: ##:::: ##:. #######:: ##::. ##:::: ##:::: ########: ########::::: ##:::: ##: ########: ##:::: ##: ########:. ######::\n" +
                "..:::::..::..:::::..:::.......:::..::::..:::::..:::::........::........::::::..:::::..::........::..:::::..::........:::......:::";
        for (int i = 0; i<title.length(); i++) {
            System.out.print(title.charAt(i));
        }

        GameRunner.gameInit();

    }
}
