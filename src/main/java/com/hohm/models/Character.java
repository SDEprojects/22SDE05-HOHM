package com.hohm.models;

import java.util.ArrayList;

public abstract class Character {
    private String name;
    private ArrayList<Object> items;
    private String room;

    public Character(String name, ArrayList<Object> items, String room) {
        this.name = name;
        this.items = items;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public void setItems(ArrayList<Object> items) {
        this.items = items;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
