/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.effects;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.Entity;
import com.jorg.game.entities.Monster;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Map;

/**
 *
 * @author jthuijls
 */
public abstract class Effect {

    protected int tileID;
    protected boolean flip = false;
    protected boolean mirr = false;
    protected boolean turn = false;
    protected int start;
    protected int delta;
    protected double x, y;
    protected int duration;
    protected int radius = 1;
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected MovingEntity originator;
    protected double speed = 0;
    protected boolean remove = false;
    protected int color;

    public Effect(double x, double y, MovingEntity entity, int d, int r, double s) {
        this.start = (int) (System.currentTimeMillis() / 100);
        this.duration = d;
        this.radius = r;
        this.speed = s;
        this.originator = entity;
        this.x = x;
        this.y = y;
        init();
    }

    protected final void init() {
        this.up = originator.isFup();
        this.right = originator.isFright();
        this.down = originator.isFdown();
        this.left = originator.isFleft();
        x = (this.left) ? x - 4 : x;
        x = (this.right) ? x + 4 : x;
        y = (this.up) ? y - 4 : y;
        y = (this.down) ? y + 4 : y;
    }

    public void update(Map map) {
        // direction: up = 0, right=1,down=2,left=3.
        int now = (int) (System.currentTimeMillis() / 100);
        double spd = speed;
        delta = now - start;
        if (delta >= duration) {
            remove = true;
        } else {
            if ((left || right) && (up || down)) {
                spd = Math.sqrt(2 * Math.pow(spd, 2)) / 2;
            }
            x -= (left) ? spd : 0;
            x += (right) ? spd : 0;
            y -= (up) ? spd : 0;
            y += (down) ? spd : 0;
        }
        if (map.isBlocked((int) (x / 8), (int) (y / 8)))
            remove = true;

    }

    public void render(Screen screen) {
        screen.render(x, y, tileID, color, flip, mirr, false, turn, 0);
    }

    public void collision() {
    }

    public boolean isRemoved() {
        return remove;
    }

    public Entity getOriginator() {
        return originator;
    }

    public boolean intersects(Entity e, int range, Map map) {
        if (originator != e) {
            double x1 = e.x;
            double y1 = e.y;
            double x2 = x;
            double y2 = y;
            double x1a = (x1 + radius > map.width * Constants.BLOCKSIZE) ? map.width
                    * Constants.BLOCKSIZE - x1 + radius : x1 + radius;
            double x1b = (x1 - radius < 0) ? map.width * Constants.BLOCKSIZE + x1
                    - radius : x1 - radius;
            double y1a = (y1 + radius > map.height * Constants.BLOCKSIZE) ? map.height
                    * Constants.BLOCKSIZE - y1 + radius : y1 + radius;
            double y1b = (y1 - radius < 0) ? map.height * Constants.BLOCKSIZE + y1
                    - radius : y1 - radius;

            if ((x1a > x2 && x1b < x2)) {
                double yy = y1 - y2;
                yy = (yy < 0) ? yy * -1 : yy;
                return yy < range;
            } else if ((y1a > y2 && y1b < y2)) {
                double xx = x1 - x2;
                xx = (xx < 0) ? xx * -1 : xx;
                return xx < range;
            }
        }
        return false;
    }

    public void collides(Monster m, int range, Map map) {
    }

    public void collides(Player p, int range, Map map) {
    }
}
