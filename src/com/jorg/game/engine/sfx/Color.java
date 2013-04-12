/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.sfx;

/**
 *
 * @author jthuijls
 */
public class Color {

    public static int gray(int c) {
        int alpha = (c >> 24) & 0xff;
        int red = (c >> 16) & 0xff;
        int green = (c >> 8) & 0xff;
        int blue = (c) & 0xff;

        int average = (red + green + blue) / 3;
        int rgb = alpha;
        rgb = (rgb << 8) + average;
        rgb = (rgb << 8) + average;
        rgb = (rgb << 8) + average;

        return rgb;
    }

    public static int get(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int changeHue(int c, int p) {
        // the darker it gets, more blue less yellow and vice versa
        int alpha = (int) (((c >> 24) & 0xff) + p);
        int red = (int) (((c >> 16) & 0xff) + (p*1.5));
        int green = (int) (((c >> 8) & 0xff) + p*0.9);
        int blue = (int) (((c) & 0xff) + (p*0.8));
        return get(get(alpha), get(red), get(green), get(blue));
    }

    private static int get(int d) {
        d = (d > 255) ? 255 : d;
        return (d < 0) ? 0 : d;
    }
}
