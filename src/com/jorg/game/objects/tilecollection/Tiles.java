/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.objects.tilecollection;

import java.util.Random;

/**
 *
 * @author jthuijls
 */
abstract public class Tiles {

    private static Random r = new Random();

    public Tiles() {
    }

    public static CoreTile get(byte type, int x, int y) {
        CoreTile tile = blank();
        switch (type) {
            case 2:
                return grassDark(x, y);
            case 1:
                return grassLight(x, y);
            case 5:
                return water(x, y);
            case 3:
                return snowDark(x, y);
            case 4:
                return snowLight(x, y);
            case 0:
                return blank();
        }
        return tile;
    }
    
    public static CoreTile get(TileType type, int x, int y) {
        CoreTile tile = blank();
        switch (type) {
            case GRASSDARK:
                return grassDark(x, y);
            case GRASSLIGHT:
                return grassLight(x, y);
            case WATER:
                return water(x, y);
            case SNOWDARK:
                return snowDark(x, y);
            case SNOWLIGHT:
                return snowLight(x, y);
            case BLANK:
                return blank();
        }
        return tile;
    }

    public static CoreTile grassLight(int x, int y) {
        return new TileGrassLight(r.nextInt(3), x, y);
    }

    public static CoreTile grassDark(int x, int y) {
        return new TileGrassDark(r.nextInt(3), x, y);
    }

    public static CoreTile water(int x, int y) {
        return new TileWater(r.nextInt(3), x, y);
    }

    public static CoreTile rock(int x, int y) {
        return new TileRock(r.nextInt(3), x, y);
    }

    public static CoreTile wall(int x, int y) {
        return new TileWall(r.nextInt(3), x, y);
    }

    public static CoreTile floor(int x, int y) {
        return new TileFloor(r.nextInt(3), x, y);
    }

    public static CoreTile corridor(int x, int y) {
        return new TileCorridor(r.nextInt(3), x, y);
    }

    public static CoreTile door(int x, int y) {
        return new TileDoor(r.nextInt(3), x, y);
    }

    public static CoreTile blank() {
        return new TileBlank();
    }

    public static CoreTile snowLight(int x, int y) {
        return new TileSnowLight(r.nextInt(3), x, y);
    }

    public static CoreTile snowDark(int x, int y) {
        return new TileSnowDark(r.nextInt(3), x, y);
    }
}
