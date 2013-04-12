/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.engine.StateInterface;
import com.jorg.game.engine.Engine;
import com.jorg.game.objects.generators.MapGenerator;

/**
 *
 * @author jthuijls
 */
final class StateMap extends State implements StateInterface {

    private static State instance = null;

    public static State instance(Engine g) {
        System.out.println("State: Map");
        if (instance == null) {
            instance = new StateMap(g);
        }
        return instance;
    }

    private StateMap(Engine g) {
        super(g);
    }

    @Override
    public void gameDraw() {
        MapGenerator.getMap().drawOverview(screen);
    }

    @Override
    public void gameInput() {
        if (input.map.once()) game.setState(StatePlay.instance(game));
    }
}
