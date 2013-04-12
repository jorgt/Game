/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.engine.input.KeyInput;
import com.jorg.game.engine.pathfinding.FieldOfVision;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.Class.ClassType;
import com.jorg.game.objects.Effects;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.Powerbar;
import com.jorg.game.objects.abilities.Ability;
import com.jorg.game.objects.generators.MapGenerator;
import com.jorg.game.objects.tilecollection.CoreTile;
import com.jorg.game.tools.Log;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
public final class Player extends MovingEntity implements Serializable {

    private static final long serialVersionUID = 8407545876304436455L;
    private KeyInput input;
    private String name;
    private int[] ptiles = new int[4];
    private static Player player;
    private ArrayList<Integer> fov = new ArrayList<Integer>();
    private Point[] crumbs = new Point[5];
    public int steps = 0;

    public static synchronized Player instance(KeyInput i, String n, ClassType t) {
        if (player == null) {
            player = new Player(i, n, t);
        }
        return player;
    }

    public static Player get() {
        return player;
    }

    public static void reset() {
        player = null;
        Inventory.clear();
    }

    public static void loadPlayer(KeyInput i, Player p) {
        player = p;
        Player.get().cls = p.cls;
        Player.get().input = i;
    }

    private Player(KeyInput i, String n, ClassType t) {
        super();
        cls = Class.get(t);
        name = n;
        input = i;
        baseTile = 32 * 14;
        color = Color.get(215, 9 * 16, 0, 255);
        cls.attributes.setWalk(true);
        cls.attributes.setSwim(true);
        cls.attributes.setLightRadius(8);
        ptiles[0] = 33;
        ptiles[1] = 1;
        ptiles[2] = 32;
        ptiles[3] = 0;
        currentAttack = cls.attacks.get(0);
        Powerbar.set(1, currentAttack);
    }

    public void setMap(Map m) {
        FieldOfVision f = new FieldOfVision();
        fov = f.go(new Point(getTileX(), getTileY()), m, cls.attributes.getLightRadius());
        m.setFoV(fov);
    }

    public void gainExperience(int xp) {
        if (cls.attributes.addExperience(xp)) {
            this.levelUp();
        }
    }

    private void levelUp() {
        cls.levelUp();
        Effects.textFlash(this, "level up!", 10, Color.get(144, 255, 255, 255));
        Log.add("Level up! Health increased to " + cls.attributes.getMaxHealth());
    }

    public void update(Screen screen) {
        super.update();
        screen.setOffset(this, MapGenerator.getMap().height, MapGenerator.getMap().width);
        keys();

        // movement
        if (up()) {
            baseTile = 14 * 32 + 2;
            flip = false;
            flip = !(((getTileY()) % 2) == 1);
            setDirection(true, false, false, false);
        } else if (down()) {
            baseTile = 14 * 32;
            flip = false;
            flip = ((int) ((getTileY()) % 2) == 1) ? !flip : flip;
            setDirection(false, true, false, false);
        } else if (left()) {
            baseTile = 14 * 32 + 4 + (int) ((getTileX()) % 2) * 2;
            flip = true;
            mirr = false;
            setDirection(false, false, true, false);
        } else if (right()) {
            baseTile = 14 * 32 + 4 + (int) ((getTileX()) % 2) * 2;
            flip = false;
            mirr = false;
            setDirection(false, false, false, true);
        }
        // what happens when the tile changes
        if (changedTile) {
            FieldOfVision f = new FieldOfVision();
            fov = f.go(new Point(getTileX(), getTileY()),
                    MapGenerator.getMap(), cls.attributes.getLightRadius());
            MapGenerator.getMap().setFoV(fov);
            steps++;
            setCrumbs();
            changedTile = false;
        }
    }

    private void keys() {
        // power bar
        if (input.one.once()) Powerbar.use(this, 1);
        if (input.two.once()) Powerbar.use(this, 2);
        if (input.three.once()) Powerbar.use(this, 3);
        if (input.four.once()) Powerbar.use(this, 4);

    }

    private void setCrumbs() {
        if (steps % 20 == 0) {
            for (int i = 0; i < crumbs.length - 1; i++) {
                crumbs[i + 1] = crumbs[1];
            }
            crumbs[0] = new Point(getTileX(), getTileY());
        }
    }

    public Point[] getCrumbs() {
        return crumbs;
    }

    @Override
    public void render(Screen screen) {
        CoreTile current = MapGenerator.getMap().getTile((int) (y / 8), (int) (x / 8));
        int k = 0;
        for (int i = -4; i < 5; i += 8) {
            for (int j = -4; j < 5; j += 8) {
                int ii = (flip) ? i * -1 : i;
                if (current.type.swimmable && j < 0) {
                    k++;
                    continue;
                }
                screen.render(screen.middleX - ii, screen.middleY - j,
                        ptiles[k] + baseTile, color, flip, mirr, gray, false, shade);
                k++;
            }
        }
    }

    @Override
    protected boolean left() {
        return input.left.pressed();
    }

    @Override
    protected boolean right() {
        return input.right.pressed();
    }

    @Override
    protected boolean up() {
        return input.up.pressed();
    }

    @Override
    protected boolean down() {
        return input.down.pressed();
    }

    @Override
    public void hurt(Monster m, Ability attack) {
        int damage = attack.getDamage();
        damage = cls.attackBonus(damage);
        m.takeDamage(damage);
        if (m.remove) {
            this.gainExperience(m.cls.attributes.getYExp());
        }
    }

    @Override
    public void takeDamage(int damage) {
        damage = cls.defenseBonus(damage);
        super.takeDamage(damage);
        Effects.number(x, y, this, 6, damage, Color.get(15, 255, 255, 255));
        Log.add("Monster: attacking for " + damage + " damage. Player: taking " + damage);
    }

    @Override
    public void gainHealth(int health) {
        super.gainHealth(health);
        Effects.number(x, y, this, 6, health, Color.get(24, 255, 255, 255));
    }

    public ArrayList<Integer> getFov() {
        return fov;
    }
}
