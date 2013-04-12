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
public class Wizard extends Class implements Serializable {

    public Wizard() {
        super();
        attributes.setAgility(startDice[1]);
        attributes.setIntelligence(startDice[3]);
        attributes.setWisdom(startDice[2]);
        attributes.setStrength(startDice[0]);
        attributes.setHealth(25 + Dice.d4());
        attacks.add(AbilityType.BOLT);
    }

    @Override
    protected void increaseHealth() {
        attributes.setHealth(attributes.getMaxHealth() + Dice.d4());
        attributes.setHealth(attributes.getMaxHealth());
    }

    @Override
    protected void increaseAttributes() {
        attributes.setIntelligence(attributes.getIntelligence() + 1);
        attributes.setWisdom(attributes.getWisdom() + 1);
    }

    @Override
    public int attackBonus(int i) {
        return (int) (i + (attributes.getBonus(attributes.getIntelligence()))
                * levelModifier());
    }

    @Override
    public int defenseBonus(int i) {
        return i;
    }
}
