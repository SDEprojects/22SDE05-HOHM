package com.hohm.model;

public abstract class Character {
    private String name;
    private String[] items;
    private String room;

    public Character(){

    }
    public Character(String name, String[] items, String room) {
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

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
