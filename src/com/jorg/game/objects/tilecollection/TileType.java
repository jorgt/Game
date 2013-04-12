package com.jorg.game.objects.tilecollection;

import com.jorg.game.engine.sfx.Color;

public enum TileType {

    BLANK(false, false, false, true, 0, 0, 0, 0, false),
    CORRIDOR(false, true, false, true, 0.7, 1,
    Color.get(255, 38, 36, 255), TileType.cGrass1, false),
    DOOR(false, true, false, true, 1, 1,
    Color.get(255, 14, 18, 255), 18, false),
    FLOOR(false, true, false, false, 1, 1,
    Color.get(255, 215, 210, 255), 215, true),
    GRASSLIGHT(false, true, false, false, 1, 1,
    Color.get(TileType.cGrass2, TileType.cGrass2, TileType.cGrass1, TileType.cGrass1),
    TileType.cGrass1, false),
    GRASSDARK(false, true, false, false, 1, 0.9,
    Color.get(TileType.cGrass1, TileType.cGrass1, TileType.cGrass2, TileType.cGrass2),
    TileType.cGrass2, false),
    ROCK(false, false, true, true, 1, 1,
    Color.get(255, 3, 0, 255), 0, true),
    SNOWDARK(false, true, false, false, 1, 0.6,
    Color.get(255, TileType.cSnow1, TileType.cSnow2, 255), TileType.cSnow2, false),
    SNOWLIGHT(false, true, false, false, 1, 0.9,
    Color.get(255, TileType.cSnow2, TileType.cSnow1, 255), TileType.cSnow1, false),
    WALL(false, false, true, false, 1, 1,
    Color.get(255, 5, 2, 255), 5, true),
    WATER(true, false, false, false, 1, 0.5,
    Color.get(TileType.cGrass1, TileType.cWater2, TileType.cWater1, TileType.cGrass1),
    TileType.cWater1, false);
    
    public final boolean swimmable;
    public final boolean walkable;
    public final boolean blocking;
    public final boolean diggable;
    public final double movement;
    public final double speed;
    public final int color;
    public final int mapColor;
    public final boolean gray;
    public static final int cGrass1 = 55;
    public static final int cGrass2 = 48;
    public static final int cNothing = 255;
    public static final int cSnow1 = 215;
    public static final int cSnow2 = 59;
    public static final int cWater1 = 3;
    public static final int cWater2 = 10;

    private TileType(boolean sw, boolean wa, boolean bl, boolean di,
            double mo, double sp, int co, int mc, boolean gr) {
        this.swimmable = sw;
        this.walkable = wa;
        this.blocking = bl;
        this.diggable = di;
        this.movement = mo;
        this.speed = sp;
        this.color = co;
        this.mapColor = mc;
        this.gray = gr;
    }
}
