package com.hohm.models;

import java.lang.reflect.Array;
import java.util.Map;

public class Hallway extends Room{

    private Boolean complete = false;

    public Hallway(Map<String, String> description, Array exit) {
        super(description, exit);
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}