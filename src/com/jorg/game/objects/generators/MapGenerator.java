/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.generators;

import com.jorg.game.Constants;
import com.jorg.game.engine.SaveLoad;
import com.jorg.game.engine.input.KeyInput;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.Inventory;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.Powerbar;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
public abstract class MapGenerator {

    private static int a, b;
    private static Map map = null;

    public static void field(int x, int y, Screen screen, Player player) {
        fix(x, y);
        setup(FieldGenerator.create(a, b), screen, player);
    }

    public static void dungeon(int x, int y, Screen screen, Player player) {
        fix(x, y);
        setup(DungeonGenerator.create(a, b), screen, player);
    }

    public static boolean save() {
        boolean ret = false;
        if (map != null) ret = SaveLoad.save(map);
        return ret;
    }

    public static boolean load(KeyInput input, String file) {
        ArrayList<Object> objects = SaveLoad.load(file);
        if (objects.isEmpty()) return false;
        for (Object o : objects) {
            if (o instanceof Map) {
                map = (Map) o;
            } else if (o instanceof Player) {
                Player.loadPlayer(input, (Player) o);
            } else if (o instanceof Powerbar) {
                Powerbar.set((Powerbar) o);
            } else if (o instanceof Inventory) {
                Inventory.set((Inventory) o);
            }
        }
        Player.get().setMap(map);
        map.generateMonsters();
        return true;
    }

    public static Map getMap() {
        if (map == null) {
            return null;
        }
        return map;
    }

    private static void setup(Map map, Screen screen, Player player) {
        MapGenerator.map = map;
        map.initialize(screen, player);
        map.setFoV(player.getFov());
        player.update(screen);
        map.update(screen);
    }

    private static void fix(int x, int y) {
        a = x;
        b = y;
        if (a < (Constants.GAME_WIDTH / Constants.BLOCKSIZE) + 1) {
            a = Constants.GAME_WIDTH / Constants.BLOCKSIZE + 1;
        }
        if (b < (Constants.GAME_HEIGHT / Constants.BLOCKSIZE) + 1) {
            b = Constants.GAME_HEIGHT / Constants.BLOCKSIZE + 1;
        }
    }

    public static void reset() {
        DungeonGenerator.reset();
        FieldGenerator.reset();
    }
}
