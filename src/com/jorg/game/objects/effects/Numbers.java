/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.effects;

import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Map;

/**
 * 
 * @author jthuijls
 */
public class Numbers extends Effect {

    protected String number;
    protected static byte corner = 0;

    public Numbers(double x, double y, MovingEntity entity, int d, int number, int color) {
        super(x, y, entity, d, 0, 1.0);
        this.color = color;
        this.number = String.valueOf(number);
        switch (corner++) {
            case 0:
                left = false;
                up = false;
                break;
            case 1:
                left = true;
                up = false;
                break;
            case 2:
                left = false;
                up = true;
                break;
            case 3:
                left = true;
                up = true;
                corner = 0;
                break;
        }
        right = !left;
        down = !up;
        this.x = (left) ? x - 8 : x + 8;
    }

    @Override
    public void update(Map map) {
        int now = (int) (System.currentTimeMillis() / 100);
        double spd = speed;
        double sp2 = speed;
        delta = now - start;
        if (delta >= duration) {
            remove = true;
        } else {
            if (delta > duration / 2) {
                sp2 *= -1;
            }
            if ((left || right) && (up || down)) {
                spd = Math.sqrt(2 * Math.pow(spd, 2)) / 2;
            }
            x -= (left) ? spd : 0;
            x += (right) ? spd : 0;
            y -= (up) ? sp2 / 2 : 0;
            y += (down) ? sp2 / 2 : 0;
        }
        if (map.isBlocked((int) x / 8, (int) y / 8)) {
            remove = true;
        }
    }

    @Override
    public void render(Screen screen) {
        Text.write(screen, number, x - 1, y - 1, Color.get(0, 255, 255, 255));
        Text.write(screen, number, x, y, color);
    }

}