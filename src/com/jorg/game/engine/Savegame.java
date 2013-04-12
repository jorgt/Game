/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine;

import com.jorg.game.entities.Inventory;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.Powerbar;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
class Savegame implements Serializable {

    protected ArrayList<Object> objects = new ArrayList<Object>();

    public Savegame(Map map) {
        objects.add(map);
        objects.add(Player.get());
        objects.add(Powerbar.get());
        objects.add(Inventory.get());
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public String getSaveName() {
        return "name";
    }
}
