/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.tools;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.Text;
import java.util.ArrayList;

/**
 *
 * @author jthuijls
 */
public abstract class Log {

    protected static ArrayList<String> log = new ArrayList<String>();

    public static void add(String s) {
        if (log.size() > Constants.LOGSIZE) {
            log.remove(0);
        }
        log.add(s);
        if (Constants.VERBOSE) {
            System.out.println(log.get(log.size() - 1));
        }
    }
    
    public static ArrayList<String> getLog() {
        return log;
    }

    public static void render(Screen screen) {
        int a = 5;
        int b = 16;
        Text.write(screen, "Log:", a - 1, 5, Color.get(0, 255, 255, 255));
        Text.write(screen, "Log:", a, 6, Color.get(144, 255, 255, 255));
        for (int i = log.size() - 1; i >= 0; i--) {
            Text.write(screen, log.get(i), a - 1, b + i * Constants.BLOCKSIZE + 1, Color.get(0, 255, 255, 255));
            Text.write(screen, log.get(i), a, b + i * Constants.BLOCKSIZE + 2, Color.get(144, 255, 255, 255));
        }
    }
}
