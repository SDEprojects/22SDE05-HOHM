package com.hohm.models;

public class Player extends Character{
    private int clues;

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
}
