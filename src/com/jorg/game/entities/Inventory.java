/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import com.jorg.game.objects.items.Item;
import com.jorg.game.tools.Log;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author jthuijls
 */
public class Inventory implements Serializable {

    private static final long serialVersionUID = 8407545876304436455L;
    protected static Inventory inventory = null;
    protected HashMap<Item, Integer> bag = new HashMap<Item, Integer>();
    protected int weight;

    public Inventory() {
    }

    public static Inventory get() {
        if (inventory == null) inventory = new Inventory();
        return inventory;
    }

    public static void set(Inventory i) {
        inventory = i;
    }

    static public void add(Item item) {
        int a = amount(item);
        if (a >= 9) return;
        if (a > 0 && a < 9)
            Inventory.get().bag.put(item, Inventory.get().bag.get(item) + 1);
        if (a == 0) Inventory.get().bag.put(item, 1);
        Inventory.get().weight += item.weight;
        Log.add("Picked up an item: " + item.name);
    }

    public static void clear() {
        Inventory.get().bag = new HashMap<Item, Integer>();
        Inventory.get().weight = 0;
    }

    public static void remove(Item item) {
        if (Inventory.get().bag.containsKey(item)) {
            Log.add("Removing an item: " + item.name);
            if (Inventory.get().bag.get(item) == 1) {
                Inventory.get().bag.remove(item);
            } else {
                Inventory.get().bag.put(item, Inventory.get().bag.get(item) - 1);
            }
            Inventory.get().weight -= item.weight;
        }
    }

    public static boolean contains(Item item) {
        if (Inventory.get().bag.get(item) == null) {
            return false;
        }
        return true;
    }

    public static int amount(Item item) {
        if (contains(item)) {
            return Inventory.get().bag.get(item);
        }
        return 0;
    }

    public static void use(MovingEntity entity, Item item) {
        if (contains(item)) {
            Inventory.get().bag.get(item);
            item.use(entity);
            Inventory.remove(item);
        }
    }

    public static Item get(int i) throws ArrayIndexOutOfBoundsException {
        Object[] items = Inventory.getAll().keySet().toArray();
        return (Item) items[i];
    }

    public static HashMap getAll() {
        return Inventory.get().bag;
    }
}
