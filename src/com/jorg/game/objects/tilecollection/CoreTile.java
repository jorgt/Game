/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.Map;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author jthuijls
 */
abstract public class CoreTile  implements Serializable {

    public int x, y;                    // location in grid
    public boolean visible = false;
    public boolean visited = false;
    public TileType type;
    protected static Random r = new Random();

    public CoreTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
    }

    public void render(Screen screen, Map map, int posX, int posY, int hue) {
    }

    public void initialize(Screen screen, Map map) {
    }
  
    final protected int fix(int i, int max) {
        i = (i < 0) ? max - 1 : i;
        i = (i >= max) ? 0 : i;
        return i;
    }
}
