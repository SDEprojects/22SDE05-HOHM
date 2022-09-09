package com.hohm.models;

public class Player extends Character{
    private int clues;
    private boolean hasAdvantage = false;
    private boolean hasSecretKey = false;
    private boolean hasFailedKey = false;

    public Player(){

    }
    public Player(String name, String[] items, String room, int clues) {
        super(name, items, room);
        this.clues = clues;
    }

    public Player(String name, String[] items, String room) {
        super(name, items, room);
    }

    public int getClues() {
        return clues;
    }

    public void setClues(int clues) {
        this.clues = clues;
    }

    public void incrementClues(){
        clues++;
    }

    public boolean getHasAdvantage() {
        return hasAdvantage;
    }

    public void setHasAdvantage(boolean hasAdvantage) {
        this.hasAdvantage = hasAdvantage;
    }

    public boolean getHasSecretKey() {
        return hasSecretKey;
    }

    public void setHasSecretKey(boolean hasSecretKey) {
        this.hasSecretKey = hasSecretKey;
    }
    public boolean getFailedKey() {
        return hasFailedKey;
    }

    public void setFailedKey(boolean hasSecretKey) {
        this.hasFailedKey = hasSecretKey;
    }
}
