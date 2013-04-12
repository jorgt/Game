/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects;

import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.abilities.Projectile;
import com.jorg.game.objects.abilities.Slash;

/**
 *
 * @author jthuijls
 */
public abstract class Abilities {

    public enum AbilityType {

        SLASH(3, 8, 0),
        BOLT(5, 10, 3);

        private AbilityType(int d, int r, double s) {
            this.duration = d;
            this.radius = r;
            this.speed = s;
        }
        public final int duration;
        public final int radius;
        public final double speed;

        public int range() {
            int r = (int) (duration * speed);
            return (r < 1) ? 1 : r;
        }
    }

    public static void get(AbilityType a, MovingEntity e) {
        switch (a) {
            case SLASH:
                Effects.addEffect(new Slash(e, a));
                break;
            case BOLT:
                Effects.addEffect(new Projectile(e, a));
                break;
        }
    }
}
