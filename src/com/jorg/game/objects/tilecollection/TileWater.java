/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Map;

/**
 *
 * @author jthuijls
 */
class TileWater extends DrawTile {

    public TileWater(int id, int x, int y) {
        super(id, x, y);
        randomize();
        type = TileType.WATER;
    }

    private void randomize() {
        spriteID = r.nextInt(4);
        flip = r.nextBoolean();
        mirr = r.nextBoolean();
    }
    
    @Override
    public void render(Screen screen, Map map, int posX, int posY, int hue) {
        screen.render(posX, posY, spriteID, type.color, flip, mirr, type.gray, false,hue);
    }
    
    @Override
    public void update() {
        super.update();
        if (r.nextInt(9) == 0) {
            randomize();
        }
    }

}
