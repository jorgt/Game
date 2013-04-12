/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.effects;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Map;

/**
 *
 * @author jthuijls
 */
public class TextFlash extends Effect {

    private String message;

    public TextFlash(MovingEntity entity, String txt, int d, int color) {
        super(0, 0, entity, d, 0, 0);
        up = left = right = down = false;
        message = txt;
        this.color = color;
        speed = 2;
    }

    @Override
    public void render(Screen screen) {
        int a = (Constants.GAME_WIDTH / 2) - ((message.length() * Constants.BLOCKSIZE) / 2);
        int b = (Constants.GAME_HEIGHT / 4);
        Text.write(screen, message, a - 1, b - delta * 2 - 1, Color.get(0, 255, 255, 255));
        Text.write(screen, message, a, b - delta * 2, color);
    }

    @Override
    public void update(Map map) {
        int now = (int) (System.currentTimeMillis() / 100);
        delta = now - start;
        if (delta >= duration) {
            remove = true;
        }
    }
}
