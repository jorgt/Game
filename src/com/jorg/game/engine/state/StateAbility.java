/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Abilities;
import com.jorg.game.objects.Powerbar;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
public class StateAbility extends State {

    int xx = Constants.BLOCKSIZE * 2;
    int yy = Constants.BLOCKSIZE / 2 - 2;
    int y = Constants.BLOCKSIZE;
    int x = Constants.BLOCKSIZE;
    int bottom = 6;
    private static State instance = null;
    private Player player = Player.get();
    private int index = 0;

    public static State instance(Engine g) {
        System.out.println("State: Ability");
        if (instance == null) {
            instance = new StateAbility(g);
        }
        return instance;
    }

    private StateAbility(Engine g) {
        super(g);
    }

    @Override
    public void gameDraw() {
        //drawBackground();
        screen.drawSquare(0, 0, 25, 17, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        screen.drawSquare(25, 0, 13, 17, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        screen.drawSquare(0, 19, 38, 5, Color.get(59, 59, 59, 59), Color.get(5, 59, 215, 0));
        drawAbilities();
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

    private void drawAbilities() {
        int color = Color.get(215, 255, 255, 255);
        ArrayList<Abilities.AbilityType> entries = Player.get().cls.attacks;
        Text.write(screen, "Abilities", xx, yy, color);
        // using this method because it allows to use iterator to remove reliably. 
        for (int i = 0; i < entries.size(); i++) {
            if (i == index) {
                color = Color.get(215, 255, 255, 53);
            } else {
                color = Color.get(215, 255, 255, 255);
            }
            String write = entries.get(i).name();
            Text.write(screen, write, xx + x + 20, yy + y * (i + 2), color);
        }
        if (entries.isEmpty()) {
            Text.write(screen, "You have no abilities...", xx + x, yy + y * 2, color);
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
        if (index < Player.get().cls.attacks.size() - 1) index++;
    }

    private void use() {
    }

    @Override
    public void gameInput() {
        if (input.up.once()) up();
        if (input.down.once()) down();
        if (input.left.once()) game.setState(StateInventory.instance(game));
        if (input.right.once()) game.setState(StateInventory.instance(game));
        if (input.enter.once()) use();
        if (input.inventory.once()) game.setState(StatePlay.instance(game));
        try {
            if (input.one.once())
                Powerbar.set(1, Player.get().cls.attacks.get(index));
            if (input.two.once())
                Powerbar.set(2, Player.get().cls.attacks.get(index));
            if (input.three.once())
                Powerbar.set(3, Player.get().cls.attacks.get(index));
            if (input.four.once())
                Powerbar.set(4, Player.get().cls.attacks.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}
