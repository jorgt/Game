/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.Map;
import java.util.ArrayList;

/**
 *
 * @author jorgthuijls
 */
public class DrawMemTile extends DrawTile {

	public int[] pix = new int[Constants.BLOCKSIZE * Constants.BLOCKSIZE];
	public int color;
	
    public DrawMemTile(int id, int x, int y) {
        super(id, x, y);
    }

    protected ArrayList<TileType> getTypes() {
        return new ArrayList<TileType>();
    }

    final protected int colorTransition(TileType from, TileType to) {
        int c1 = TileType.cNothing;
        int c2 = TileType.cNothing;

        if (to == TileType.GRASSLIGHT) c1 = TileType.cGrass1;
        if (to == TileType.GRASSDARK) c1 = TileType.cGrass2;
        if (to == TileType.SNOWDARK) c1 = TileType.cSnow2;
        if (to == TileType.SNOWLIGHT) c1 = TileType.cSnow2;
        if (to == TileType.WATER) c1 = TileType.cWater2;

        if (to == TileType.GRASSLIGHT) c2 = TileType.cGrass1;
        if (to == TileType.GRASSDARK) c2 = TileType.cGrass2;
        if (to == TileType.SNOWDARK) c2 = TileType.cSnow2;
        if (to == TileType.SNOWLIGHT) c2 = TileType.cSnow1;
        if (to == TileType.WATER) c2 = TileType.cWater1;

        // c1, c2 = to, c3 = from
        return Color.get(c2, c1, TileType.cNothing, TileType.cNothing);
    }

    @Override
    public final void initialize(Screen screen, Map map) {
        ArrayList<TileType> types = getTypes();
        pix = screen.toPixels(pix, spriteID, type.color, flip, mirr, false, 0);
        for (TileType t : types) {
            boolean up = map.getTile(fix(this.y - 1, map.height), this.x).type == t;
            boolean down = map.getTile(fix(this.y + 1, map.height), this.x).type == t;
            boolean left = map.getTile(this.y, fix(this.x - 1, map.width)).type == t;
            boolean right = map.getTile(this.y, fix(this.x + 1, map.width)).type == t;
            if (up || down || left || right) {
                int s = spriteID;
                boolean f = false;
                boolean m = false;
                if (up) {
                    s = (left || right) ? 11 : 12;
                    f = (right) ? true : false;
                } else if (down) {
                    s = (left || right) ? 11 : 12;
                    f = (right) ? true : false;
                    m = true;
                } else if (left) {
                    s = 32 + 11;
                } else if (right) {
                    s = 32 + 11;
                    f = true;
                }
                pix = screen.toPixels(pix, s, colorTransition(type, t), f, m, false, 0);
            }
        }
    }
}
