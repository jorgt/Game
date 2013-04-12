/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.abilities.Ability;
import com.jorg.game.objects.items.Item;
import com.jorg.game.tools.Dice;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jthuijls
 */
public class Class implements Serializable {

    public enum ClassType {

        WARRIOR, WIZARD;
    }
    public final Attributes attributes = new Attributes();
    protected int level = 0;
    protected int[] startDice = new int[4];
    public final ArrayList<AbilityType> attacks = new ArrayList<AbilityType>();

    public static Class get(ClassType type) {
        switch (type) {
            case WARRIOR:
                return new Warrior();
            case WIZARD:
                return new Wizard();
        }
        return null;
    }

    public Class() {
        for (int i = 0; i < startDice.length; i++) {
            int x = Dice.d6(3);
            int y = Dice.d6(3);
            startDice[i] = (x > y) ? x : y;
        }
        Arrays.sort(startDice);
        attacks.clear();
    }

    public void levelUp() {
        if (level % 1 == 0) this.increaseHealth();
        if (level % 4 == 0) this.increaseAttributes();
    }

    public double levelModifier() {
        try {
            return Math.sqrt(1 + (10 / level));
        } catch (ArithmeticException e) {
            return 1;
        }
    }

    protected void increaseHealth() {
    }

    protected void increaseAttributes() {
    }

    public int attackBonus(int i) {
        return 0;
    }

    public int defenseBonus(int i) {
        return 0;
    }

    public boolean canUse(Item item) {
        return false;
    }

    public boolean canUse(Ability ability) {
        return false;
    }
}
