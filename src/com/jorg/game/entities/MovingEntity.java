/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.Constants;
import com.jorg.game.engine.pathfinding.AStarPoint;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.abilities.Ability;
import com.jorg.game.objects.generators.MapGenerator;
import com.jorg.game.objects.tilecollection.CoreTile;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
abstract public class MovingEntity extends Entity implements Serializable {

    // attributes
    private static final long serialVersionUID = 8407545876304436455L;
    //public Attributes attributes = new Attributes();
    public Class cls = null;
    // stuff
    protected int baseTile = 32 * 14;
    protected boolean hasFov;
    protected boolean friendlyFire = false;
    protected boolean tookDamage = false;
    protected boolean changedTile = true;
    public ArrayList<AStarPoint> travel = new ArrayList<AStarPoint>();
    protected AStarPoint go;
    protected AbilityType currentAttack;

    public MovingEntity() {
        super(); 
    }

    @Override
    public void update() {
        super.update();
        handleMovement();
    }

    public void update(Player player) {
    }

    public void combat(Player player) {
    }

    public void combat(Monster monster) {
    }

    protected void handleMovement() {
        int mw = Constants.BLOCKSIZE * MapGenerator.getMap().width;
        int mh = MapGenerator.getMap().height * Constants.BLOCKSIZE;

        // determine if player can move. if so, actually move.
        double pX = x, pY = y;

        CoreTile current = MapGenerator.getMap().getTile(getTileY(), getTileX());

        double spd = current.type.speed;
        // diagonals: speed pythagorassed instead of simply doubled
        if ((left() || right()) && (up() || down())) {
            spd = Math.sqrt(2 * Math.pow(spd, 2)) / 2;
        }
        spd *= cls.attributes.getSpeed();
        pX -= (left()) ? spd : 0;
        pX += (right()) ? spd : 0;
        pY -= (up()) ? spd : 0;
        pY += (down()) ? spd : 0;
        pX = (pX >= (mw) - 1) ? 0 : pX;
        pY = (pY >= (mh) - 1) ? 0 : pY;
        pX = (pX < 0) ? mw - 1 : pX;
        pY = (pY < 0) ? mh - 1 : pY;

        int newX = (int) (pX / Constants.BLOCKSIZE);
        int newY = (int) (pY / Constants.BLOCKSIZE);

        if (getTileY() == newY && getTileX() == newX) {
            x = pX;
            y = pY;
            changedTile = false;
        } else if (checkTile(pX, pY)) {
            x = pX;
            y = pY;
            changedTile = true;
        } else if (checkTile(x, pY)) {
            y = pY;
            changedTile = true;
        } else if (checkTile(pX, y)) {
            x = pX;
            changedTile = true;
        }
    }

    protected boolean checkTile(double px, double py) {
        int newX = (int) (px / Constants.BLOCKSIZE);
        int newY = (int) (py / Constants.BLOCKSIZE);
        CoreTile tile = MapGenerator.getMap().getTile(newY, newX);
        if ((tile.type.walkable && cls.attributes.canWalk())
                || (tile.type.swimmable && cls.attributes.canSwim())) {
            x = px;
            y = py;
            changedTile = true;
            return true;
        }

        return false;
    }

    public void hurt(Monster m, Ability attack) {
        // attack to be type of attack. determine actual damage here, 
        // with modifiers and shit. 		
    }

    public void hurt(Player p, Ability attack) {
        // attack to be type of attack. determine actual damage here, 
        // with modifiers and shit. 
    }

    public void takeDamage(int damage) {
        // attack after modifier. mitigation by entity here. 
        if (damage >= cls.attributes.getHealth()) {
            remove = true;
        }
        cls.attributes.removeHealth(damage);
        tookDamage = true;
    }

    public void gainHealth(int health) {
        cls.attributes.addHealth(health);
    }

    protected boolean left() {
        return false;
    }

    protected boolean right() {
        return false;
    }

    protected boolean up() {
        return false;
    }

    protected boolean down() {
        return false;
    }

    final protected void resetMovement() {
        left = right = up = down = false;
    }

    public boolean hasFriendlyFire() {
        return friendlyFire;
    }
}
