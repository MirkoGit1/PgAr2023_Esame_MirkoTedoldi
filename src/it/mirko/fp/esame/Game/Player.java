package it.mirko.fp.esame.Game;

import it.mirko.fp.esame.TamaGolem.Tamagolem;

import java.util.List;

public class Player {
    private int hp = 20;
    private int damage = 5;
    private List<Tamagolem> golemList;
    private int currentPosition;

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public List<Tamagolem> getGolemList() {
        return golemList;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
