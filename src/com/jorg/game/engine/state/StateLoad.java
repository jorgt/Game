/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.SaveLoad;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.objects.generators.MapGenerator;

/**
 *
 * @author jthuijls
 */
public class StateLoad extends State {

    private static State instance = null;
    private int index = 0;
    private int a, c, e;
    private int b, d, f;
    private String[] files = null;

    public static State instance(Engine g) {
        System.out.println("State: Load");
        if (instance == null) {
            instance = new StateLoad(g);
        }
        instance.gameInit();
        return instance;
    }

    @Override
    public void gameInit() {
        files = SaveLoad.getList();
    }

    private StateLoad(Engine g) {
        super(g);
        a = c = e = (Constants.GAME_WIDTH / 2) - ((9 * Constants.BLOCKSIZE) / 2);
        b = d = f = (Constants.GAME_HEIGHT / 4) - ((1 * Constants.BLOCKSIZE) / 2);
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

    @Override
    public void gameDraw() {
        Text.write(screen, "Load game", c, d, Color.get(9, 255, 255, 255));
        Text.write(screen, "load game", e, f, Color.get(100, 255, 255, 255));
        try {
            for (int i = 0; i < files.length; i++) {
                String file = files[i].substring(0, files[i].lastIndexOf("."));
                int v = (Constants.GAME_WIDTH / 2)
                        - ((file.length() * Constants.BLOCKSIZE) / 2);
                int col = Color.get(215, 255, 255, 255);
                if (index == i) col = Color.get(100, 255, 255, 255);

                Text.write(screen, file, v, b + 40 + 20 * i, col);
            }
        } catch (NullPointerException ex) {
        }
        if (files == null || files.length == 0) {
            int v = (Constants.GAME_WIDTH / 2)
                    - (("no save files found".length() * Constants.BLOCKSIZE) / 2);
            Text.write(screen, "no save files found", v, b + 40 + 20 * 2, Color.get(144, 0, 0, 0));
        }
    }

    private void up() {
        if (index > 0) index--;
    }

    private void down() {
        if (index < files.length - 1) index++;
    }

    private void go() {
        if(MapGenerator.load(input, files[index]))
            game.setState(StatePlay.instance(game));
    }

    @Override
    public void gameInput() {
        if (input.down.once()) down();
        if (input.up.once()) up();
        if (input.enter.once()) go();
        if (input.escape.once()) game.setState(StateMenu.instance(game));
    }
}
