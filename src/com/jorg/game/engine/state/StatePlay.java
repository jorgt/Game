/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.StateInterface;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.Class.ClassType;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.Powerbar;
import com.jorg.game.objects.generators.MapGenerator;
import com.jorg.game.tools.Log;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
final public class StatePlay extends State implements StateInterface {

    private static State instance = null;
    private Player player;
    private boolean paused = false;
    private boolean generating = false;
    private boolean log = false;

    public static State instance(Engine g) {
        System.out.println("State: Play");
        if (instance == null) {
            instance = new StatePlay(g);
        }
        return instance;
    }

    private StatePlay(Engine g) {
        super(g);
    }

    @Override
    public void reset() {
        instance = null;
        Player.reset();
    }

    @Override
    public void gameInit() {
        player = Player.instance(input, "John", ClassType.WIZARD);
        if (MapGenerator.getMap() == null) generateMap(true);
        player.update(screen);
    }

    @Override
    public void gameDraw() {
        screen.allGray = log;
        MapGenerator.getMap().draw(screen);
        player.render(screen);
        if (paused) {
            int a = (Constants.GAME_WIDTH / 2) - ((6 * Constants.BLOCKSIZE) / 2);
            int b = (Constants.GAME_HEIGHT / 2) - ((1 * Constants.BLOCKSIZE) / 2);
            Text.write(screen, "Paused", a + 1, b + 1, Color.get(0, 255, 255, 255));
            Text.write(screen, "Paused", a, b, Color.get(144, 255, 255, 255));
        }
        screen.allGray = false;
        gui();
    }

    private void gui() {
        int p = player.cls.attributes.getLevelPercent();
        String all =
                "Lvl: " + player.cls.attributes.getLevel() + ", " + p
                + "% HP: " + player.cls.attributes.getHealth();
        int a = Constants.GAME_WIDTH - 4 - (all.length() * Constants.BLOCKSIZE);
        Text.write(screen, all, a - 1, 4, Color.get(0, 255, 255, 255));
        Text.write(screen, all, a, 5, Color.get(215, 255, 255, 255));
        Powerbar.render(screen);
    }

    @Override
    public void gameUpdate() {
        if (player.cls.attributes.getHealth() <= 0)
            game.setState(StateGameOver.instance(game));

        if (!paused && !generating && !log) {
            player.update(screen);
            MapGenerator.getMap().update(screen);
        }
    }

    @Override
    public void gameInput() {
        if (input.escape.once()) game.setState(StateMenu.instance(game));
        if (input.map.once()) game.setState(StateMap.instance(game));
        if (input.nine.once()) generateMap(true);
        if (input.zero.once()) generateMap(false);
        if (input.pause.once()) paused = !paused;
        if (input.log.once()) log = !log;
        if (input.inventory.once())
            game.setState(StateInventory.instance(game));
    }

    @Override
    public void gameFocus() {
        int a = (Constants.GAME_WIDTH / 2) - ((6 * Constants.BLOCKSIZE) / 2);
        int b = (Constants.GAME_HEIGHT / 2) - ((1 * Constants.BLOCKSIZE) / 2);
        Text.write(screen, "Focus!", a + 1, b + 1, Color.get(0, 255, 255, 255));
        Text.write(screen, "Focus!", a, b, Color.get(144, 255, 255, 255));
    }

    @Override
    public Graphics changeGraphic(Graphics g) {
        if (log) {
            Font font = new Font("Courier New", Font.BOLD, 8 * Constants.SCALE);
            g.setColor(java.awt.Color.red);
            g.setFont(font);
            ArrayList<String> logs = Log.getLog();
            for (int i = logs.size() - 1; i >= 0; i--) {
                g.drawString(logs.get(i), 15 * Constants.SCALE, 20 * Constants.SCALE + i * 10 * Constants.SCALE);
            }
        }
        return g;
    }

    private void generateMap(boolean field) {
        generating = true;
        int x = (int) (220);
        int y = (int) (170);
        if (field) {
            MapGenerator.field(x, y, screen, player);
        } else {
            MapGenerator.dungeon(x, y, screen, player);
        }
        MapGenerator.reset();
        generating = false;
    }
}
