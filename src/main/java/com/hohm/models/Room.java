package com.hohm.models;

import java.lang.reflect.Array;
import java.util.Map;

public abstract class Room {

    private Map<String, String> description;
    private Array exit;

    public Room(Map<String, String> description, Array exit) {
        this.description = description;
        this.exit = exit;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Array getExit() {
        return exit;
    }

    public void setExit(Array exit) {
        this.exit = exit;
    }
}
