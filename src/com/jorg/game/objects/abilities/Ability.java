/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.abilities;

import com.jorg.game.Constants;
import com.jorg.game.entities.Monster;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.effects.Effect;

/**
 *
 * @author jthuijls
 */
public abstract class Ability extends Effect {

    protected DamageType type;
    protected String name;

    public enum DamageType {

        MELEE, FIRE, LIGHTNING, ARCANE, NATURE;
    }

    public Ability(MovingEntity entity, AbilityType a) {
        super(entity.x, entity.y, entity, a.duration, a.radius, a.speed);
    }

    @Override
    public void collides(Monster m, int range, Map map) {
        if (originator instanceof Monster) {
            if (originator.hasFriendlyFire() && intersects(m, range, map)) {
                originator.hurt(m, this);
                remove = true;
            } else if (intersects(m, range, map)) {
                remove = true;
            }
        } else if (intersects(m, range, map)) {
            originator.hurt(m, this);
            remove = true;
        }
    }

    @Override
    public void collides(Player p, int range, Map map) {
        if (originator instanceof Monster) {
            if (intersects(p, range, map)) {
                originator.hurt(p, this);
                remove = true;
            }
        }
    }

    public int getDamage() {
        return 0;
    }

    public int getDistance() {
        int r = (int) (duration * speed);
        return (r < Constants.BLOCKSIZE / 2) ? Constants.BLOCKSIZE / 2 : r;
    }
}
