/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.Inventory;
import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Abilities.AbilityType;
import com.jorg.game.objects.items.Item;
import java.io.Serializable;

/**
 *
 * @author jthuijls
 */
public class Powerbar implements Serializable {

    private static Powerbar powerbar = null;
    public final Object[] buttons = new Object[5];
    protected static final int size = 3;

    public static Powerbar get() {
        if (powerbar == null) powerbar = new Powerbar();
        return powerbar;
    }

    public static void set(Powerbar p) {
        powerbar = p;
    }

    public static void drawBlock(Screen screen) {
    }

    public static void set(int i, Object o) {
        if (i >= 0 && i < Powerbar.get().buttons.length) {
            Powerbar.get().buttons[i] = o;
        }
    }

    public static void render(Screen screen, int i) {
        int h = Constants.GAME_HEIGHT / Constants.BLOCKSIZE - size - 2;
        int w = 11;
        int c1 = Color.get(0, 0, 0, 0);
        int c2 = Color.get(0, 0, 215, 255);
        screen.drawSquare(w + 3 * i, h, size, size, c1, c2);
        try {
            int f = 102;
            int g = 172;
            if (Powerbar.get().buttons[i] instanceof Item) {
                Item item = (Item) Powerbar.get().buttons[i];
                int n = Inventory.amount(item);
                item.sprite.render(screen, f + i * 24, g);
                Text.write(screen, Integer.toString(n), f + 5 + i * 24, g + 7,
                        Color.get(215, 255, 255, 255));
            } else if (Powerbar.get().buttons[i] instanceof AbilityType) {
                AbilityType ability = (AbilityType) Powerbar.get().buttons[i];
                Text.write(screen, "A", f + 5 + i * 24, g + 7,
                        Color.get(215, 255, 255, 255));
            }
        } catch (NullPointerException e) {
        }
    }

    public static void render(Screen screen) {
        for (int i = 1; i < Powerbar.get().buttons.length; i++) {
            render(screen, i);
        }
    }

    public static void use(MovingEntity e, int i) {
        if (Powerbar.get().buttons[i] instanceof Item) {
            Inventory.use(e, (Item) Powerbar.get().buttons[i]);
        } else if (Powerbar.get().buttons[i] instanceof AbilityType) {
            Abilities.get((AbilityType) Powerbar.get().buttons[i], e);
        }
    }
}
