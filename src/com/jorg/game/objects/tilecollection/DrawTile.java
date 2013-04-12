/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.objects.Map;

/**
 *
 * @author jorgthuijls
 */
public class DrawTile extends CoreTile {

    public DrawTile(int id, int x, int y) {
        super(x, y);
        this.spriteID = id;
    }
    
    protected boolean flip = false; // flip sprite
    protected boolean mirr = false; // mirror sprite
    protected int spriteID; // sprite number

    public int getSpriteID() {
        return spriteID;
    }
    
    @Override
    public void initialize(Screen screen, Map map) {
    }
    

    @Override
    public void render(Screen screen, Map map, int posX, int posY, int hue) {
        screen.render(posX, posY, spriteID, type.color, flip, mirr, type.gray, false,hue);
    }
    
}
