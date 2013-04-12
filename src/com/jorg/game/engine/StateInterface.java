/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine;

import java.awt.Graphics;

/**
 *
 * @author jthuijls
 */
public interface StateInterface {
        
    public void gameInit();

    public void gameShutDown();

    public void gameDraw();

    public void gameUpdate();
    
    public void gameInput();
    
    public void gameFocus();
    
    public Graphics changeGraphic(Graphics g);
    
    public void reset();

}
