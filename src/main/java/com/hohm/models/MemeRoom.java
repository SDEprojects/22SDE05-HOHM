package com.hohm.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class MemeRoom {
    private String title;
    private Map<String, Map<String, String>> objectives;
    private Map<String, String> description;
    private ArrayList<Object> items;
    private Array exit;
    private Boolean complete = false;

    public MemeRoom(String title, Map<String, Map<String, String>> objectives, Map<String, String> description, ArrayList<Object> items, Array exit) {
        this.title = title;
        this.objectives = objectives;
        this.description = description;
        this.items = items;
        this.exit = exit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Map<String, String>> getObjectives() {
        return objectives;
    }

    public void setObjectives(Map<String, Map<String, String>> objectives) {
        this.objectives = objectives;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public void setItems(ArrayList<Object> items) {
        this.items = items;
    }

    public Array getExit() {
        return exit;
    }

    public void setExit(Array exit) {
        this.exit = exit;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
