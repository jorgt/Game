/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

/**
 *
 * @author jthuijls
 */
class TileCorridor extends DrawTile {

    public TileCorridor(int id, int x, int y) {
        super(id, x, y);
        flip = r.nextBoolean();
        mirr = r.nextBoolean();
        type = TileType.CORRIDOR;
    }
}
