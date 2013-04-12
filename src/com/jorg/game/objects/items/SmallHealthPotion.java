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
public class SmallHealthPotion extends Consumable {

    public SmallHealthPotion(int weight, String name) {
        super(weight, name);
        this.sprite = new Sprite(0, 0, 32 * 12, Color.get(144, 72, 0, 255));
    }

    @Override
    public void use(MovingEntity entity) {
        int hp = Dice.d4() + 3;
        Log.add("You quaffed a potion! You regain " + hp + " health.");
        entity.gainHealth(hp);
    }
}
