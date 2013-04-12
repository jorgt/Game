/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.sfx;

import com.jorg.game.Constants;
import com.jorg.game.entities.Entity;
import java.io.Serializable;

/**
 *
 * @author jthuijls
 */
public class Sprite implements Serializable {

    public double x, y;
    public final int spriteID;
    public final int color;
    public boolean removed;

    public Sprite(double x, double y, int spriteID, int color) {
        this.x = x;
        this.y = y;
        this.spriteID = spriteID;
        this.color = color;
    }

    public void update() {
    }

    public void render(Screen screen) {
        screen.render(x, y, spriteID, color, false, false, false, false, 0);
    }

    public void render(Screen screen, int xx, int xy) {
        screen.render(xx, xy, spriteID, color, false, false, false, false, 0);
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean intersect(Entity e) {
        int b = Constants.BLOCKSIZE;
        return (x < e.x + b && x > e.x - b && y < e.y + b && y > e.y - b);
    }
}
