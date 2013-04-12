/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.items;

import com.jorg.game.engine.sfx.Sprite;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Map;
import java.io.Serializable;

/**
 *
 * @author jthuijls
 */
abstract public class Item  implements Serializable{

    public final int weight;
    public final String name;
    protected boolean removed = false;
    public Sprite sprite;
    protected int color = 0;

    public Item(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public void addToMap(double x, double y) {
        if(sprite != null) {
            sprite.x = x;
            sprite.y = y;
            Map.addItem(this);
        }
    }

    public void use(MovingEntity entity) {
    }
    
    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Item other = (Item) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
            return false;
        return true;
    }
}
