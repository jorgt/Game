/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.abilities;

import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.Map;
import com.jorg.game.tools.Dice;

/**
 *
 * @author jthuijls
 */
public class Projectile extends Ability {

    protected int color1;

    public Projectile(MovingEntity entity, AbilityType a) {
        super(entity, a);
        color = Color.get(210, 0, 255, 255);
        color1 = Color.get(0, 210, 255, 255);
        tileID = 13 * 32 + 6;
        name = "Bolt";
        type = DamageType.LIGHTNING;
    }

    @Override
    public void update(Map map) {
        super.update(map);
        turn = up || down;
        flip = right;
        mirr = down;
    }

    @Override
    public void render(Screen screen) {
        screen.render(x - 1, y - 1, tileID, ((delta % 2 == 0)) ? color1 : color, flip, mirr, false, turn, 0);
        screen.render(x, y, tileID, ((delta % 2 == 0)) ? color : color1, flip, mirr, false, turn, 0);
    }
    
    @Override
    public int getDamage() {
        return Dice.d6(2);
    }
}
