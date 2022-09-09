package com.hohm.models;

public class FinalBoss {
    private final String keycode = "MR27314";
    public void finalBoss(String input, MemeRoom currentRoom){
        if (input.equals(keycode)){
            System.out.println("made it");
        }
    }

}
