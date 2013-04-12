/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.sfx;

import com.jorg.game.Constants;
import com.jorg.game.entities.Player;

/**
 *
 * @author jthuijls
 */
public class Screen {

    public int xOff = 0, yOff = 0;
    public int playerX = 0, playerY = 0;
    public int mapX = 0, mapY = 0;
    public final int middleX;
    public final int middleY;
    protected SpriteStore sprites;
    public boolean allGray = false;
    public int[] colors = new int[256];
    public int[] pixels = new int[Constants.GAME_HEIGHT
            * Constants.GAME_WIDTH];

    /**
     * Class screen draws tiles in the right position by keeping track of the
     * current x + y offset of the map, the dimensions of a tile and the
     * dimensions of the window the game is displayed in.
     *
     * @param sprites Loads an item of type SpriteStore, so sprites can be
     * called by name.
     */
    public Screen(SpriteStore sprites) {
        middleX = Constants.GAME_WIDTH / 2 - 4;
        middleY = Constants.GAME_HEIGHT / 2 - 8;
        this.sprites = sprites;
        setColors(colors, 0);
    }

    public int getColor(int number, int hue) {
        return Color.changeHue(colors[number], hue);
    }

    /**
     * Renders a tile to screen based on its absolute position on the map. This
     * function will convert the absolute position into a position relative to
     * player and map.
     *
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param sprite - the sprite number
     * @param color
     * @param f
     * @param m
     * @param gray
     * @param hue
     */
    public void render(double x, double y, int sprite, int color, boolean f,
            boolean m, boolean gray, boolean turn, int hue) {
        gray = allGray || gray;
        int pX = (int) x;
        int pY = (int) y;
        int bl = Constants.BLOCKSIZE;
        int a = pX - playerX + middleX;
        int b = pY - playerY + middleY;

        if (pX < middleX - bl && a < -bl) {
            a = mapX - playerX + middleX + pX;
        }
        if (pY < middleY - bl && b < -bl) {
            b = mapY - playerY + middleY + pY;
        }
        if (pX > mapX - middleX - bl && a > mapX - bl) {
            a = middleX - mapX + pX - playerX;
        }
        if (pY > mapY - middleY - bl && b > mapY - bl) {
            b = middleY - mapY + pY - playerY;
        }
        /*
         * if (a >= Constants.CANVAS_WIDTH || a < 0 || b >=
         * Constants.CANVAS_HEIGHT || b < 0) return;
         */
        this.render(a, b, sprite, color, f, m, gray, turn, hue);
    }

    /**
     * Render a tile based on the position on the screen.
     *
     * @param pX
     * @param pY
     * @param sprite
     * @param color
     * @param f
     * @param m
     * @param gray
     * @param hue
     */
    public void render(int pX, int pY, int sprite, int color, boolean f,
            boolean m, boolean gray, boolean turn, int hue) {

        gray = allGray || gray;
        int[] spritepix = sprites.getTile(sprite);
        for (int x = 0; x < Constants.BLOCKSIZE; x++) {
            // width, limit to screen size.
            int xx = x + pX;
            int fX = (f) ? Constants.BLOCKSIZE - x - 1 : x;
            if (xx >= Constants.GAME_WIDTH || xx < 0)
                continue;
            for (int y = 0; y < Constants.BLOCKSIZE; y++) {
                // height, limit to screen size.
                int yy = y + pY;
                int fY = (m) ? Constants.BLOCKSIZE - y - 1 : y;
                if (yy >= Constants.GAME_HEIGHT || yy < 0)
                    continue;
                // determine color and set pixel.
                int col;
                if (turn) {
                    col = color >> ((spritepix[fX + fY * Constants.BLOCKSIZE]) * 8) & 255;
                } else {
                    col = color >> ((spritepix[fY + fX * Constants.BLOCKSIZE]) * 8) & 255;
                }
                int set = (gray) ? Color.gray(getColor(col, hue)) : getColor(
                        col, hue);
                if (col < 255) {
                    pixels[xx + yy * Constants.GAME_WIDTH] = set;
                }
            }
        }
    }

    /**
     * Renders a sprite, which is an array of pixels, to a location on screen
     *
     * @param pX
     * @param pY
     * @param sprite
     * @param gray
     * @param hue
     */
    public void render(int pX, int pY, int[] sprite, boolean gray, int hue) {
        gray = allGray || gray;
        for (int x = 0; x < Constants.BLOCKSIZE; x++) {
            // width, limit to screen size.
            int xx = x + pX;
            if (xx >= Constants.GAME_WIDTH || xx < 0)
                continue;
            for (int y = 0; y < Constants.BLOCKSIZE; y++) {
                // height, limit to screen size.
                int yy = y + pY;
                if (yy >= Constants.GAME_HEIGHT || yy < 0)
                    continue;
                // determine color and set pixel.
                int col = Color.changeHue(sprite[y + x * Constants.BLOCKSIZE], hue);
                pixels[xx + yy * Constants.GAME_WIDTH] = (gray) ? Color.gray(col) : col;
            }
        }
    }

    public void renderPixel(int pX, int pY, int color, boolean gray, int hue) {
        gray = allGray || gray;
        if (Constants.inCanvas(pX, pY)) {
            int col = (gray) ? Color.gray(getColor(color, hue)) : getColor(
                    color, hue);
            pixels[pX + pY * Constants.GAME_WIDTH] = col;
        }
    }

    final public void setColors(int[] s, int hue) {
        int pp = 0;
        for (int r = 0; r < 6; r++) {
            int rr = (r * 255 / 5);
            for (int g = 0; g < 6; g++) {
                int gg = (g * 255 / 5);
                for (int b = 0; b < 6; b++) {
                    int bb = (b * 255 / 5);
                    s[pp++] = Color.changeHue(rr << 16 | gg << 8 | bb, hue);
                }
            }
        }
    }

    public void setOffset(Player player, int h, int w) {
        mapX = Constants.BLOCKSIZE * w;
        mapY = Constants.BLOCKSIZE * h;
        // screen
        playerX = (int) player.x;
        playerY = (int) player.y;
        int a = (int) player.x - Constants.GAME_WIDTH / 2;
        int b = (int) player.y - Constants.GAME_HEIGHT / 2;
        xOff = (a >= mapX - 1) ? a - mapX : a;
        yOff = (b >= mapY - 1) ? b - mapY : b;
        xOff = (a < 0) ? mapX + a - 1 : a;
        yOff = (b < 0) ? mapY + b - 1 : b;
    }

    public int[] toPixels(int[] pix, int tile, int color, boolean f, boolean m,
            boolean g, int h) {
        g = allGray || g;

        int[] sprite = sprites.getTile(tile);
        for (int x = 0; x < Constants.BLOCKSIZE; x++) {
            int fx = (m) ? Constants.BLOCKSIZE - x - 1 : x;
            for (int y = 0; y < Constants.BLOCKSIZE; y++) {
                int fy = (f) ? Constants.BLOCKSIZE - y - 1 : y;
                int col = color >> ((sprite[fx + fy * Constants.BLOCKSIZE]) * 8) & 255;
                int set = (g) ? Color.gray(getColor(col, h)) : getColor(col, h);
                if (col < 255) {
                    pix[x + y * Constants.BLOCKSIZE] = set;
                }
            }
        }
        return pix;
    }

    public void drawSquare(int sx, int sy, int h, int w, int c1, int c2) {
        int b = Constants.BLOCKSIZE;
        boolean flip, mirr, turn;
        for (int i = sx * b; i < sx * b + h * b; i += b) {
            for (int j = sy * b; j < sy * b + w * b; j += b) {
                int side1a = sx * b;
                int side1b = sx * b + h * b - b;
                int side2a = sy * b;
                int side2b = sy * b + w * b - b;

                int tile = 32 * 32 - 1;
                int color = c1;
                flip = mirr = turn = false;
                if (i == side1a) {
                    tile = 32 * 13 + 1;
                    color = c2;
                    turn = true;
                }

                if (i == side1b) {
                    tile = 32 * 13 + 1;
                    color = c2;
                    turn = true;
                    flip = true;
                }

                if (j == side2a) {
                    tile = 32 * 13 + 1;
                    color = c2;
                }

                if (j == side2b) {
                    tile = 32 * 13 + 1;
                    color = c2;
                    mirr = true;
                }

                if (i == side1a && j == side2a) {
                    color = c2;
                    tile = 32 * 13;
                } else if (i == side1a && j == side2b) {
                    color = c2;
                    tile = 32 * 13;
                } else if (i == side1b && j == side2a) {
                    color = c2;
                    tile = 32 * 13;
                } else if (i == side1b && j == side2b) {
                    color = c2;
                    tile = 32 * 13;
                }
                render(b + i, b + j, tile, color, flip, mirr, false, turn, 0);
            }
        }
    }
}
