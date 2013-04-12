/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game;

import com.jorg.game.engine.Engine;
import javax.swing.SwingUtilities;

/**
 *
 * @author jthuijls
 */
public class Main {

    public static void main(String[] args) {
        final Engine game = new Engine("Still Unknown");
        
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                game.gameStart();
            }
        });
    }
}
