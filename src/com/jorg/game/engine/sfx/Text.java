/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.sfx;

/**
 *
 * @author jthuijls
 */
public class Text {

    private static final String LINE1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LINE2 = "0123456789.,!?'\"-+=/\\%()<>:;";

    public static void write(Screen screen, String msg, int x, int y, int color) {
        int row = 0, col = 0;
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            if (LINE1.indexOf(msg.charAt(i)) >= 0) {
                col = LINE1.indexOf(msg.charAt(i));
                row = 30;
            } else {
                col = LINE2.indexOf(msg.charAt(i));
                row = 31;
            }
            // render
            screen.render(x + i * 8, y, col + row * 32, color, false, false, false, false, 0);
        }
    }

    public static void write(Screen screen, String msg, double x, double y, int color) {
        int row = 0, col = 0;
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            if (LINE1.indexOf(msg.charAt(i)) >= 0) {
                col = LINE1.indexOf(msg.charAt(i));
                row = 30;
            } else {
                col = LINE2.indexOf(msg.charAt(i));
                row = 31;
            }
            // render
            screen.render(x + i * 8, y, col + row * 32, color, false, false, false, false, 0);
        }
    }
}
