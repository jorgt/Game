/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.state;

import com.jorg.game.engine.StateInterface;
import com.jorg.game.Main;
import com.jorg.game.engine.Engine;
import com.jorg.game.engine.input.KeyInput;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.SpriteStore;
import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jthuijls
 */
abstract public class State implements StateInterface {

    protected Engine game;
    protected Screen screen;
    protected KeyInput input;

    protected State(Engine g) {
        this.game = g;
        this.input = g.input;
        try {
            screen = new Screen(new SpriteStore(ImageIO.read(
                    Main.class.getResourceAsStream("sources/icons.png"))));

        } catch (IOException ex) {
            System.out.println(ex);
        }
        screen.pixels = g.pixels;
        gameInit();
    }

    @Override
    public void gameInit() {
    }

    @Override
    public void gameShutDown() {
    }

    @Override
    public void gameDraw() {
    }

    @Override
    public void gameUpdate() {
    }

    @Override
    public void gameFocus() {
        
    }

    @Override
    public void gameInput() {
    }

    @Override
    public void reset() {
    }
    
    @Override 
    public Graphics changeGraphic(Graphics g) {
        return g;
    }

    public boolean isRunning() {
        return true;
    }
}
