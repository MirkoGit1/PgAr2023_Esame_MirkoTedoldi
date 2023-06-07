package it.mirko.fp.esame.Game;

import java.util.Random;

public class Enemy {
    private int hp;
    private int damage;

    public Enemy() {
        this.hp = generateRandomEnemyHp();
        this.damage = generateRandomEnemyDamage();
    }

    //Metodi che generano casualmente le statistiche di un nemico
    private static int generateRandomEnemyHp(){
        Random rand = new Random();
        return rand.nextInt(11) + 7;
    }

    private static int generateRandomEnemyDamage(){
        Random rand = new Random();
        return rand.nextInt(5) + 1;
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
