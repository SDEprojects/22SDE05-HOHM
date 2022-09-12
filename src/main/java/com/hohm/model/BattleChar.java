package com.hohm.model;

public class BattleChar {

    private int hp;

    public BattleChar(int hp) {
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
