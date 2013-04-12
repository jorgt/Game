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
public class LargeHealthPotion extends Consumable {

    public LargeHealthPotion(int weight, String name) {
        super(weight, name);
        this.sprite = new Sprite(0, 0, 32 * 12, Color.get(72, 144, 174, 255));
    }

    @Override
    public void use(MovingEntity entity) {
        int hp = Dice.d12() + 10;
        Log.add("You quaffed a potion! You regain " + hp + " health.");
        entity.gainHealth(hp);
    }
}
