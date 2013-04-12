/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.tools.Dice;
import java.io.Serializable;

/**
 *
 * @author jthuijls
 */
public class Warrior extends Class implements Serializable {

    public Warrior() {
        super();
        attributes.setAgility(startDice[3]);
        attributes.setIntelligence(startDice[0]);
        attributes.setWisdom(startDice[1]);
        attributes.setStrength(startDice[2]);
        attributes.setHealth(30 + Dice.d8());
        attacks.add(AbilityType.SLASH);
    }

    @Override
    protected void increaseHealth() {
        attributes.setHealth(attributes.getMaxHealth() + Dice.d8());
        attributes.setHealth(attributes.getMaxHealth());
    }

    @Override
    protected void increaseAttributes() {
        attributes.setStrength(attributes.getStrength() + 1);
        attributes.setAgility(attributes.getAgility() + 1);
    }

    @Override
    public int attackBonus(int i) {
        return (int) (i + (attributes.getBonus(attributes.getStrength()))
                * levelModifier());
    }

    @Override
    public int defenseBonus(int i) {
        return i;
    }
}
