/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.engine.StateInterface;
import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Text;

/**
 *
 * @author jthuijls
 */
final class StateGameOver extends State implements StateInterface {

    private static State instance = null;
    int a, c, e;
    int b, d, f;

    public static State instance(Engine g) {
        System.out.println("State: Game Over");
        if (instance == null) {
            instance = new StateGameOver(g);
        }
        return instance;
    }

    private StateGameOver(Engine g) {
        super(g);
        a = c = e = (Constants.GAME_WIDTH / 2) - ((10 * Constants.BLOCKSIZE) / 2);
        b = d = f = (Constants.GAME_HEIGHT / 2) - ((1 * Constants.BLOCKSIZE) / 2);
    }

    @Override
    public void gameDraw() {
        Text.write(screen, "game over!", c, d, Color.get(2 * 16 + 6, 255, 255, 255));
        Text.write(screen, "game over!", e, f, Color.get(144, 255, 255, 255));
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
    public void gameInput() {
        if (input.escape.once()) game.setState(StateQuit.instance(game));
        if (input.enter.once()) {
            game.setState(StateInitial.instance(game));
            StatePlay.instance(game).reset();
        }
    }
}
