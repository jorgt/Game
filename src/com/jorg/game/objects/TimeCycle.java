/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects;

import com.jorg.game.Constants;

/**
 *
 * @author jthuijls
 */
public class TimeCycle {

    private static double start = System.currentTimeMillis() / 1000;
    private static boolean go = true;
    private static int cycle = Constants.TIMECYCLES / 2; // start.
    private static int mod = 1;

    public static int get() {
        mod = (go) ? 1 : -1;
        double now = System.currentTimeMillis() / 1000;
        double delta = now - start;
        if (delta == Constants.TIMECHANGE) {
            cycle += mod;
            if (cycle == Constants.TIMECYCLES - 1 || cycle == 0) {
                go = !go;
            }
            start = now;
        }
        return cycle;
    }

    public static int hue() {
        int a = Constants.TIMEDARKESTDELTA;
        int b = Constants.TIMELIGHTESTDELTA - a;
        int sin = (int) (Math.sin(get() * (Math.PI) / (Constants.TIMECYCLES * 2 - 1)) * b) + a;
        return sin;
    }
}
