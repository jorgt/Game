/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.Constants;
import com.jorg.game.engine.pathfinding.AStar;
import com.jorg.game.engine.pathfinding.FieldOfVision;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.Abilities;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.Effects;
import com.jorg.game.objects.Items;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.abilities.Ability;
import com.jorg.game.objects.generators.MapGenerator;
import com.jorg.game.objects.items.Item;
import com.jorg.game.objects.tilecollection.CoreTile;
import com.jorg.game.tools.Dice;
import com.jorg.game.tools.Log;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author jthuijls
 */
public class Monster extends MovingEntity {

    protected Random r = new Random();
    protected int dmgColor = 0;
    protected int[] ptiles = new int[4];
    protected int c = 0;

    public Monster() {
        super();
        cls = Class.get(Class.ClassType.WARRIOR);
        // for drawing
        spriteID = 12 * 32;
        dmgColor = Color.get(0, 215, 215, 255);
        ptiles[0] = 33;
        ptiles[1] = 1;
        ptiles[2] = 32;
        ptiles[3] = 0;
        // attacks
        currentAttack = cls.attacks.get(0);
        color = Color.get(215, 3 * 16, 0, 255);
        mapColor = 144;
        // attributes
        cls.attributes.setSpeed(r.nextDouble() + 0.5);
        cls.attributes.setWisdom(10);// AI: smarter is following you more
        cls.attributes.setHealth(Dice.d8(2) + 2);
        cls.attributes.setLightRadius(7); // if they see you, they attack
        cls.attributes.setYExp(100); // gives this much xp when killed
        cls.attributes.setSwim(false);
        cls.attributes.setWalk(true);
    }

    protected void AI(Player player) {
        determineTravelCrumb(player);

        // check a few times per second to possibly change direction. 
        if (r.nextInt(Constants.MAX_UPDATES / 4) == 0) {
            resetMovement();
            if (Dice.d20() < cls.attributes.getWisdom() && travel.size() > 0) {
                goTravel();
            } else {
                randomDirection();
            }
        }
    }

    protected void determineTravelCrumb(Player player) {
        int xx = player.getTileX();
        int yy = player.getTileY();

        double d = FieldOfVision.distance(getTileX(), getTileY(), xx, yy);

        // if the monster's in range
        if (d < cls.attributes.getRange()) {
            travel = AStar.travel(MapGenerator.getMap(), getTileX(), getTileY(), xx, yy, 5, this);
            go = travel.get(0);
        } else {
            try {
                for (Point p : player.getCrumbs()) {
                    double dd = FieldOfVision.distance(getTileX(), getTileY(), p.x, p.y);
                    if (dd < cls.attributes.getRange()) {
                        travel = AStar.travel(MapGenerator.getMap(), getTileX(), getTileY(), xx, yy, 5, this);
                        go = travel.get(0);
                        return;
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    protected void goTravel() {
        if (go.x == getTileX() && go.y == getTileY()) {
            go = travel.get(travel.size() - 1);
            travel.remove(go);
        }
        if (go.x > getTileX()) {
            right = true;
        }
        if (go.x < getTileX()) {
            left = true;
        }
        if (go.y > getTileY()) {
            down = true;
        }
        if (go.y < getTileY()) {
            up = true;
        }
    }

    protected void randomDirection() {
        left = r.nextBoolean();
        right = !left;
        up = r.nextBoolean();
        down = !up;
    }

    @Override
    public void combat(Player player) {
        if (r.nextInt(30) == 0) {
            Abilities.get(currentAttack, this);
        }
    }

    @Override
    protected boolean down() {
        return down;
    }

    @Override
    protected boolean left() {
        return left;
    }

    public void render(Screen screen, Map map, Player player) {
        if (map.isVisible(getTileY(), getTileX()) || !map.affectedByVision) {
            int co = (tookDamage) ? dmgColor : color;
            int k = 0;
            CoreTile curr = map.getTile(getTileY(), getTileX());
            for (int i = -4; i < 5; i += 8) {
                for (int j = -4; j < 5; j += 8) {
                    int ii = (flip) ? i * -1 : i;

                    if (curr.type.swimmable && j < 0) {
                        k++;
                        continue;
                    }
                    screen.render(x - ii, y - j, ptiles[k] + baseTile, co, flip,
                            mirr, gray, false, shade);
                    k++;
                }
            }
        }
    }

    @Override
    protected boolean right() {
        return right;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        Effects.number(x, y, this, 6, damage, Color.get(144, 255, 255, 255));
        Log.add("Monster: taking " + damage);
    }

    @Override
    protected boolean up() {
        return up;
    }

    @Override
    public void update(Player player) {
        // intersect range should be the same number as the range or 
        // travel time of the attack. otherwise monsters will always miss
        //System.out.println(currentAttack.range());
        if (intersects(player.getTileX(), player.getTileY(), currentAttack.range())) {
            resetMovement();
            this.combat(player);
        } else {
            this.AI(player);
            super.update();
        }

        // drawing
        if (up()) {
            baseTile = 14 * 32 + 2;
            flip = false;
            flip = ((int) ((y / 8) % 2) == 1) ? !flip : flip;
        } else if (down()) {
            baseTile = 14 * 32;
            flip = false;
            flip = ((int) ((y / 8) % 2) == 1) ? !flip : flip;
        } else if (left()) {
            baseTile = 14 * 32 + 4 + (int) ((x / 8) % 2) * 2;
            flip = true;
            mirr = false;
        } else if (right()) {
            baseTile = 14 * 32 + 4 + (int) ((x / 8) % 2) * 2;
            flip = false;
            mirr = false;
        }

        if (tookDamage) {
            tookDamage = false;
        }
    }

    protected void determineAttack(Player player) {
    }

    @Override
    public void hurt(Player p, Ability attack) {
        int damage = (int) attack.getDamage();
        p.takeDamage(damage);
    }

    @Override
    public void drop() {
        int chance = Dice.d20();
        Item item = null;
        if (chance == 1) {
            item = Items.largeHealthPotion();
        } else if (chance < 5) {
            item = Items.mediumHealthPotion();
        } else if (chance < 10) {
            item = Items.smallHealthPotion();
        }
        if (item != null) item.addToMap(x, y);

        //}
    }
}
