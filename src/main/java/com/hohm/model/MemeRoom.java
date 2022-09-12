package com.hohm.model;

import java.util.Map;

public class MemeRoom {
    private String title;
    private Map<String, Map<String, String>> objectives;
    private Map<String, String> description;
    private Map<String, Map<String, String>> items;
    private String[] exit;
    private Boolean complete;
    private Map<String, String> altGet;

    public MemeRoom(){

    }
    public MemeRoom(String title, Map<String, Map<String, String>> objectives, Map<String, String> description, Map<String, Map<String, String>> items, String[] exit, boolean complete) {
        this.title = title;
        this.objectives = objectives;
        this.description = description;
        this.items = items;
        this.exit = exit;
        this.complete = complete;
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

    public Map<String, Map<String, String>> getItems() {
        return items;
    }

    public void setItems(Map<String, Map<String, String>> items) {
        this.items = items;
    }

    public String[] getExit() {
        return exit;
    }

    public void setExit(String[] exit) {
        this.exit = exit;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Map<String, String> getAltGet() {
        return altGet;
    }

    public void setAltGet(Map<String, String> altGet) {
        this.altGet = altGet;
    }
}
