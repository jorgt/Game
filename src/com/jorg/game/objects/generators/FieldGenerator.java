/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.generators;

import java.util.ArrayList;
import java.util.Random;

import com.jorg.game.objects.Map;
import com.jorg.game.objects.tilecollection.Tiles;

/**
 *
 * @author jorgthuijls
 */
abstract class FieldGenerator {

    private static Random r = new Random();
    private static int height;
    private static int width;
    private static Map map;
    private static ArrayList<byte[]> t;
    private static byte[] tileBytes;

    public static Map create(int x, int y) {
        System.out.println("-----------------------------------------------------------");
        System.out.println("  Starting field builder, generating map: " + x + " by " + y);

        reset();
        width = x;
        height = y;
        map = new Map(width, height);
        map.affectedByTime = true;
        tileBytes = new byte[width * height];

        // first layer is the "base" layer and has to be two proper tiles.
        // anything you want to add is added with "blank". 
        fillTiles(3, 4, 55, (byte) 1, (byte) 2);
        fillTiles(1, 4, 70, (byte) 0, (byte) 3);
        fillTiles(3, 4, 55, (byte) 0, (byte) 4);
        fillTiles(9, 4, 50, (byte) 0, (byte) 5);
        t = null;
        //tiles = new CoreTile[width * height];
        convert();
        map.generateMonsters();
        System.out.println("-----------------------------------------------------------");
        return map;
    }

    private static void fillTiles(int repeats, int surr,
            int probability, byte from, byte to) {

        System.out.print("  Generating " + from + " and " + to + ". ");
        System.out.print("Repeats: " + repeats + ", Surround: " + surr
                + ", Probability: " + probability + ". ");
        t.add(new byte[width * height]);
        byte[] tls = t.get(t.size() - 1);
        int surr2 = 0;
        for (int i = 0; i < width * height; i++) {
            if (r.nextInt(100) > probability) {
                tls[i] = to;//Tiles.get(to, i % width, (i - (i % width)) / width);
            } else {
                tls[i] = from;//Tiles.get(from, i % width, (i - (i % width)) / width);
            }
        }
        while (repeats-- > 0) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    surr2 = 0;
                    for (int x = -1; x < 2; x++) {
                        for (int y = -1; y < 2; y++) {
                            int xx = (i + x) + (j + y) * width;
                            if (xx < 0) continue;
                            if (xx > width * height - 1) continue;
                            if (tls[xx] == to) surr2++;
                        }
                    }
                    if (surr2 > surr) {
                        tls[i + j * width] = to;//Tiles.get(to, i, j);
                    } else {
                        tls[i + j * width] = from;//Tiles.get(from, i, j);
                    }
                }
            }
        }
        System.out.println("Merging layer into base.");
        merge();
    }

    private static void merge() {
        for (int x = 0; x < t.size(); x++) {
            for (int i = 0; i < t.get(x).length; i++) {
                if (t.get(x)[i] != (byte) 0) {
                    tileBytes[i] = t.get(x)[i];
                }
            }
            t.remove(x);
        }
    }

    private static void convert() {
        for (int i = 0; i < tileBytes.length; i++) {
            map.tiles[i] = Tiles.get(tileBytes[i], i % width, (i - (i % width)) / width);
            tileBytes[i] = 0;
        }
        tileBytes = null;
    }

    public static void reset() {
        height = 0;
        width = 0;
        map = null;
        t = new ArrayList<byte[]>();
        tileBytes = null;
    }
}
