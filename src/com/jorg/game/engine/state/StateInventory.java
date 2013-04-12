/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.Inventory;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Powerbar;
import com.jorg.game.objects.items.Item;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author jthuijls
 */
public class StateInventory extends State {

    int xx = Constants.BLOCKSIZE * 2;
    int yy = Constants.BLOCKSIZE / 2 - 2;
    int y = Constants.BLOCKSIZE;
    int x = Constants.BLOCKSIZE;
    int bottom = 6;
    private static State instance = null;
    private Player player = Player.get();
    private int index = 0;

    public static State instance(Engine g) {
        System.out.println("State: Inventory");
        if (instance == null) {
            instance = new StateInventory(g);
        }
        return instance;
    }

    private StateInventory(Engine g) {
        super(g);
    }

    @Override
    public void gameDraw() {
        //drawBackground();
        screen.drawSquare(0, 0, 25, 17, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        screen.drawSquare(25, 0, 13, 17, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        screen.drawSquare(0, 19, 38, 5, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        drawInventory();
        drawPlayerStats();
        drawPowerBar();
    }

    private void drawPlayerStats() {
        int color = Color.get(215, 255, 255, 255);
        int xxx = Constants.BLOCKSIZE * 27;
        Text.write(screen, "Stats", xxx, yy, color);
        ArrayList<String> stats = new ArrayList<String>();
        stats.add("level   " + player.cls.attributes.getLevel());
        stats.add("health  " + player.cls.attributes.getHealth());
        stats.add("AGI     " + player.cls.attributes.getAgility());
        stats.add("STR     " + player.cls.attributes.getStrength());
        stats.add("WIS     " + player.cls.attributes.getWisdom());
        stats.add("INT     " + player.cls.attributes.getIntelligence());
        stats.add("steps   " + player.steps);
        for (int i = 0; i < stats.size(); i++) {
            Text.write(screen, stats.get(i), xxx, yy + y * (2 + i), color);
        }

    }

    private void drawInventory() {
        int i = 1;
        int color = Color.get(215, 255, 255, 255);
        Iterator entries = Inventory.getAll().entrySet().iterator();
        Text.write(screen, "Inventory", xx, yy, color);
        // using this method because it allows to use iterator to remove reliably. 
        while (entries.hasNext()) {
            i++;
            if (i - 2 == index) {
                color = Color.get(215, 255, 255, 53);
            } else {
                color = Color.get(215, 255, 255, 255);
            }
            Map.Entry entry = (Map.Entry) entries.next();
            Item key = (Item) entry.getKey();
            Integer value = (Integer) entry.getValue();
            key.sprite.render(screen, xx + x, yy + y * i);
            String write = value.toString() + " x \"" + key.name + "\"";
            Text.write(screen, write, xx + x + 20, yy + y * i, color);
        }
        if (i == 1) {
            Text.write(screen, "Your bags are empty...", xx + x, yy + y * 2, color);
        }
    }

    private void drawPowerBar() {
        int color = Color.get(215, 255, 255, 255);
        Text.write(screen, "Power bar", xx, yy + 19 * Constants.BLOCKSIZE, color);
        Powerbar.render(screen);
    }

    @Override
    public void gameUpdate() {
        player = Player.get();
    }

    private void up() {
        if (index > 0) index--;
    }

    private void down() {
        if (index < Inventory.getAll().size()) index++;
    }

    private void use() {
        if (index >= 0 && index < Inventory.getAll().size()) {
            Object[] items = Inventory.getAll().keySet().toArray();
            Inventory.use(player, (Item) items[index]);
        }
    }

    @Override
    public void gameInput() {
        if (input.up.once()) up();
        if (input.down.once()) down();
        if (input.left.once()) game.setState(StateAbility.instance(game));
        if (input.right.once()) game.setState(StateAbility.instance(game));
        if (input.enter.once()) use();
        if (input.inventory.once()) game.setState(StatePlay.instance(game));
        try {
            if (input.one.once()) Powerbar.set(1, Inventory.get(index));
            if (input.two.once()) Powerbar.set(2, Inventory.get(index));
            if (input.three.once()) Powerbar.set(3, Inventory.get(index));
            if (input.four.once()) Powerbar.set(4, Inventory.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
        }

    }
}
