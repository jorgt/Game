/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.engine.StateInterface;
import com.jorg.game.Constants;
import com.jorg.game.engine.Engine;

/**
 *
 * @author jthuijls
 */
final public class StateInitial extends State implements StateInterface {

    private static State instance = null;
    protected int r = 255, test = 0;

    public static State instance(Engine g) {
        System.out.println("State: Initial");
        if (instance == null) {
            instance = new StateInitial(g);
        }
        return instance;
    }

    private StateInitial(Engine g) {
        super(g);
    }

    @Override
    public void gameInit() {
        r = 255;
    }

    @Override
    public void gameShutDown() {
    }

    @Override
    public void gameDraw() {
        for (int i = 0; i < Constants.GAME_WIDTH * Constants.GAME_HEIGHT; i++) {
            game.pixels[i] = test;
        }
        //screen.render(0, 0, 1, Color.get(131, 242, 131, 242), false, false);
        //game.pixels = screen.pixels;
    }

    @Override
    public void gameUpdate() {

        r -= 5;
        if (r <= 0) {
            //game.setState(StateMenu.instance(game));
            game.setState(StateMenu.instance(game));
        }

        test = (r << 16) + (r << 8) + (r);
    }

    @Override
    public void gameInput() {
    }
}
