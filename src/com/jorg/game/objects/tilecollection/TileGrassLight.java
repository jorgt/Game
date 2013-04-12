/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import java.util.ArrayList;

import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.Map;

/**
 *
 * @author jthuijls
 */
class TileGrassLight extends DrawMemTile {

    public TileGrassLight(int id, int x, int y) {
        super(id, x, y);
        flip = r.nextBoolean();
        mirr = r.nextBoolean();
        type = (TileType.GRASSLIGHT);
    }

    @Override
    protected ArrayList<TileType> getTypes() {
        ArrayList<TileType> types = new ArrayList<TileType>();
        types.add(TileType.WATER);
        types.add(TileType.GRASSDARK);
        types.add(TileType.SNOWLIGHT);
        types.add(TileType.SNOWDARK);
        return types;
    }

    @Override
    public void render(Screen screen, Map map, int posX, int posY, int hue) {
        screen.render(posX, posY, pix, type.gray, hue);
    }

}
