/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.engine.sfx.Color;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.tools.Dice;

/**
 *
 * @author jorgthuijls
 */
public class SwimmingMonster extends Monster {

    public SwimmingMonster() {
        // for drawing
        color = Color.get(4 * 16, 0, 15, 255);
        mapColor = 4 * 16;
        //attributes
        cls = Class.get(Class.ClassType.WIZARD);
        cls.attributes.setSwim(true);
        cls.attributes.setWalk(false);
        cls.attributes.setLightRadius(15);
        cls.attributes.setHealth(Dice.d4(2));
        cls.attributes.setWisdom(12);
        cls.attributes.setYExp(200);
        //attacks
        currentAttack = cls.attacks.get(0);
    }
}
