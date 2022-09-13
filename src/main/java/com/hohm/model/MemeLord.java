package com.hohm.model;

public class MemeLord {

    private int hp;

    public MemeLord(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void hit(int hp){
        this.hp -= hp;
    }
}
