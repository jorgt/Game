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
import com.jorg.game.objects.generators.MapGenerator;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
final class StateMenu extends State implements StateInterface {

    private static State instance = null;
    private int a, c, e;
    private int b, d, f;
    private ArrayList<String> options = new ArrayList<String>();
    private int index = 0;
    public boolean saved = false;

    public static State instance(Engine g) {
        System.out.println("State: Menu");
        if (instance == null) {
            instance = new StateMenu(g);
        }
        instance.gameInit();
        return instance;
    }

    private StateMenu(Engine g) {
        super(g);
        a = c = e = (Constants.GAME_WIDTH / 2) - ((4 * Constants.BLOCKSIZE) / 2);
        b = d = f = (Constants.GAME_HEIGHT / 4) - ((1 * Constants.BLOCKSIZE) / 2);
        options.add("New game");
        options.add("Save game");
        options.add("Load game");
    }

    @Override
    public void gameInit() {
        gameUpdate();
        if (MapGenerator.getMap() != null) {
            options = new ArrayList<String>();
            options.add("Continue");
            options.add("load game");
            options.add("save game");
            options.add("quit game");
        } else {
            options = new ArrayList<String>();
            options.add("New game");
            options.add("Load game");
            options.add("quit game");
        }
        saved = false;
    }

    @Override
    public void gameDraw() {
        Text.write(screen, "Menu", c, d, Color.get(9, 255, 255, 255));
        Text.write(screen, "Menu", e, f, Color.get(100, 255, 255, 255));
        for (int i = 0; i < options.size(); i++) {
            int v = (Constants.GAME_WIDTH / 2)
                    - ((options.get(i).length() * Constants.BLOCKSIZE) / 2);
            int col = Color.get(215, 255, 255, 255);
            if (index == i) col = Color.get(100, 255, 255, 255);
            Text.write(screen, options.get(i), v, b + 40 + 20 * i, col);
        }
        if (saved) {
            int v = (Constants.GAME_WIDTH / 2)
                    - (("game saved".length() * Constants.BLOCKSIZE) / 2);
            Text.write(screen, "game saved", v, b + 40 + 20 * 5, Color.get(144, 255, 255, 255));
        }
    }

    @Override
    public void gameUpdate() {
        java.util.Random r = new java.util.Random();
        if (r.nextInt(19) == 0) {
            c = a - 1 + r.nextInt(5);
            d = b - 1 + r.nextInt(5);
            e = a + r.nextInt(5);
            f = b + r.nextInt(5);
        }

    }

    private void changeIndex(boolean dir) {
        if (dir) {
            if (index > 0) index--;
        } else {
            if (index <= options.size() - 2) index++;
        }
    }

    private void go() {
        switch (index) {
            case 0:
                game.setState(StatePlay.instance(game));
                break;
            case 1:
                game.setState(StateLoad.instance(game));
                break;
            case 2:
                if (MapGenerator.getMap() != null)
                    saved = MapGenerator.save();
                else
                    game.setState(StateQuit.instance(game));
                break;
            case 3:
                game.setState(StateQuit.instance(game));
        }
    }

    @Override
    public void gameInput() {
        if (input.enter.once()) go();
        if (input.escape.once()) game.setState(StateQuit.instance(game));
        if (input.down.once()) changeIndex(false);
        if (input.up.once()) changeIndex(true);
    }
}
