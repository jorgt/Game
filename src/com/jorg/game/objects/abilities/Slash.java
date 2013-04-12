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
public class Slash extends Ability {

    protected int effectTile = 0;
    protected boolean eflip, emirr;

    public Slash(MovingEntity entity, AbilityType a) {
        super(entity, a);
        color = Color.get(215, 0, 50, 255);
        tileID = 13 * 32 + 5;
        modDir();
    }

    final public void modDir() {
        x = (left) ? x - 4 : x;
        x = (right) ? x + 4 : x;
        y = (up) ? y - 4 : y;
        y = (down) ? y + 4 : y;
    }

    @Override
    public void update(Map map) {
        super.update(map);
        eflip = left;
        emirr = down;
        flip = right;
        effectTile = (down || up) ? 13 * 32 + 6 : 13 * 32 + 7;
        turn = up || down;
    }

    @Override
    public void render(Screen screen) {
        screen.render(x, y, effectTile, color, eflip, emirr, false, turn, 0);
        super.render(screen);
    }
    
    @Override
    public int getDamage() {
        return Dice.d4();
    }
}
