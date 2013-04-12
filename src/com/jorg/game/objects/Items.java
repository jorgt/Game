/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects;
import com.jorg.game.objects.items.Item;
import com.jorg.game.objects.items.LargeHealthPotion;
import com.jorg.game.objects.items.MediumHealthPotion;
import com.jorg.game.objects.items.SmallHealthPotion;

/**
 *
 * @author jorgthuijls
 */
public abstract class Items {
    
    public static Item smallHealthPotion() {
        return new SmallHealthPotion(1, "Small Potion");
    }
    
    public static Item mediumHealthPotion() {
        return new MediumHealthPotion(1, "Medium Potion");
    }
    
    public static Item largeHealthPotion() {
        return new LargeHealthPotion(1, "Large Potion");
    }
    
}
