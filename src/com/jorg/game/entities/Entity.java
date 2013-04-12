/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.TimeCycle;
import com.jorg.game.objects.generators.MapGenerator;
import java.io.Serializable;

/**
 * 
 * @author jthuijls
 */
abstract public class Entity implements Serializable {

    private static final long serialVersionUID = 8407545876304436455L;
    // rendering, collision etc
    public double x, y; // location in grid
    protected int color;
    protected boolean gray = false;
    protected boolean flip = false;
    protected boolean mirr = false;
    protected int shade = 0;
    protected boolean remove = false;
    protected int spriteID;
    public int mapColor;
    protected boolean left = false;
    protected boolean right = false;
    protected boolean up = false;
    protected boolean down = false;
    protected boolean fleft = false;
    protected boolean fright = false;
    protected boolean fup = false;
    protected boolean fdown = false;
    // game stuff
    protected boolean blocks = false; // when bumped into, does it block
    // movement?

    public Entity() {
    }

    public void attack() {
    }

    public boolean blocks() {
        return blocks;
    }

    final public int getTileX() {
        return (int) x / Constants.BLOCKSIZE;
    }

    final public int getTileY() {
        return (int) y / Constants.BLOCKSIZE;
    }

    public void interact() {
    }

    public boolean intersects(int x1, int y1, int range) {
        boolean inRange = false;
        resetDirection();
        if (getTileX() == x1) {
            int yy = getTileY() < y1 ? y1 - getTileY() : getTileY() - y1;
            fdown = getTileY() < y1;
            fup = !fdown;
            inRange = yy <= range;
        } else if (getTileY() == y1) {
            int xx = getTileX() < x1 ? x1 - getTileX() : getTileX() - x1;
            fright = getTileX() < x1;
            fleft = !fright;
            inRange = xx <= range;
        }

        if (inRange) {
            return checkBlockers(range);
        }
        return false;
    }

    private boolean checkBlockers(int range) {
        for (int i = 0; i < range; i++) {
            int xx = getTileX();
            int yy = getTileY();
            if (fleft || fright) {
                xx = (fright) ? getTileX() + i : getTileX() + i * -1;
                xx = (xx >= MapGenerator.getMap().width) ? xx - MapGenerator.getMap().width : xx;
                xx = (xx < 0) ? xx + MapGenerator.getMap().width : xx;
            } else if (fup || fdown) {
                yy = (fdown) ? getTileY() + i : getTileY() + i * -1;
                yy = (yy >= MapGenerator.getMap().height) ? yy - MapGenerator.getMap().height : yy;
                yy = (yy < 0) ? yy + MapGenerator.getMap().height : yy;
            }
            if (MapGenerator.getMap().tiles[xx + yy * MapGenerator.getMap().width].type.blocking) return false;
        }
        return true;
    }

    public boolean isFdown() {
        return fdown;
    }

    public boolean isFleft() {
        return fleft;
    }

    public boolean isFright() {
        return fright;
    }

    public boolean isFup() {
        return fup;
    }

    public void move() {
    }

    public void render(Screen screen) {
        int a = screen.xOff;
        int b = screen.yOff;
        int xx = (int) (x - a);
        int yy = (int) (y - b);
        if ((x < a || x > a + Constants.GAME_HEIGHT)
                && (y < b || y > b + Constants.GAME_HEIGHT))
            return;
        else
            screen.render(yy, xx, spriteID, color, flip, mirr, gray, false, shade);
    }

    final protected void resetDirection() {
        fup = fdown = fleft = fright = false;
    }

    final protected void setDirection(boolean u, boolean d, boolean l, boolean r) {
        fup = u;
        fdown = d;
        fleft = l;
        fright = r;
    }

    public void setFdown(boolean fdown) {
        this.fdown = fdown;
    }

    public void setFleft(boolean fleft) {
        this.fleft = fleft;
    }

    public void setFright(boolean fright) {
        this.fright = fright;
    }

    public void setFup(boolean fup) {
        this.fup = fup;
    }

    public void start(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (MapGenerator.getMap().affectedByTime) {
            shade = TimeCycle.hue();
        }
    }

    public void use() {
    }

    public int vision() {
        return 0;
    }

    public boolean isRemoved() {
        if (remove) {
            this.drop();
        }
        return remove;
    }

    public void drop() {
    }
}
