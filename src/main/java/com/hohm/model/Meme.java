package com.hohm.model;

import java.util.Map;

public class Meme {
    private String[] dialogue;
    private String description;
    private String room;
    private Map<String,String> altName;

    Meme(){

    }

    public Meme(String[] dialogue, String description, String room, Map<String,String> altName) {
        this.dialogue = dialogue;
        this.description = description;
        this.room = room;
        this.altName = altName;
    }

    public String[] getDialogue() {
        return dialogue;
    }

    public String getDescription() {
        return description;
    }

    public String getRoom() {
        return room;
    }

    public Map<String,String> getAltName() {
        return altName;
    }
}
