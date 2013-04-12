/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects;

import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.effects.Effect;
import com.jorg.game.objects.effects.Numbers;
import com.jorg.game.objects.effects.TextFlash;
import java.util.ArrayList;

/**
 * 
 * @author jthuijls
 */
public abstract class Effects {

    protected final static ArrayList<Effect> effects = new ArrayList<Effect>();

    static public void number(double x, double y,
            MovingEntity entity, int duration, int number, int color) {
        addEffect(new Numbers(x, y, entity, duration, number, color));
    }

    static public void textFlash(MovingEntity entity, String msg, int duration, int color) {
        addEffect(new TextFlash(entity, msg, duration, color));
    }

    static public void addEffect(Effect e) {
        effects.add(e);
    }

    static public void updateEffects(Map map) {
        for (int i = 0; i < Effects.effects.size(); i++) {
            if (Effects.effects.get(i).isRemoved())
                Effects.effects.remove(i);
            else
                Effects.effects.get(i).update(map);
        }
    }

    static public void drawEffects(Screen screen) {
        for (Effect e : Effects.effects) {
            e.render(screen);
        }
    }
}
