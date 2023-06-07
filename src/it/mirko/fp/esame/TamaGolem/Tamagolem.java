package it.mirko.fp.esame.TamaGolem;

import java.util.List;

public class Tamagolem {
    private static int hp = 20;
    private List<Element> stoneList;

    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        Tamagolem.hp = hp;
    }

}
