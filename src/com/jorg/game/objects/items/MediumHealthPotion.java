/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.items;

import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Sprite;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.tools.Dice;
import com.jorg.game.tools.Log;

/**
 *
 * @author jthuijls
 */
public class MediumHealthPotion extends Consumable {

    public MediumHealthPotion(int weight, String name) {
        super(weight, name);
        this.sprite = new Sprite(0, 0, 32 * 12, Color.get(108, 144, 0, 255));
    }

    @Override
    public void use(MovingEntity entity) {
        int hp = Dice.d8() + 6;
        Log.add("You quaffed a potion! You regain " + hp + " health.");
        entity.gainHealth(hp);
    }
}
