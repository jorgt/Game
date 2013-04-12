/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.engine.StateInterface;
import com.jorg.game.engine.Engine;

/**
 *
 * @author jthuijls
 */
final class StateQuit extends State implements StateInterface {

    private static State instance = null;

    public static State instance(Engine g) {
        System.out.println("State: Quit");
        if (instance == null) {
            instance = new StateQuit(g);
        }
        return instance;
    }

    private StateQuit(Engine g) {
        super(g);
    }
    
    @Override
    public boolean isRunning() {
        return false;
    }
}
