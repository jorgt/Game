/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.sfx;

import com.jorg.game.Constants;
import java.awt.image.BufferedImage;

/**
 *
 * @author jthuijls
 */
public class SpriteStore {

    private int[] pixels;
    private int pixHeight, pixWidth;
    private int blockHeight, blockWidth;

    public SpriteStore(BufferedImage image) {
        pixHeight = image.getHeight();
        pixWidth = image.getWidth();
        blockHeight = pixHeight / Constants.BLOCKSIZE;
        blockWidth = pixWidth / Constants.BLOCKSIZE;
        pixels = new int[image.getHeight() * image.getWidth()];
        pixels = image.getRGB(0, 0, pixWidth, pixHeight, null, 0, pixWidth);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (pixels[i] & 0xff) / 64;
        }
    }

    public int[] getTile(int tileID) {
        int start = blockToStartPixel(tileID);
        int i = 0;
        int[] tile = new int[Constants.BLOCKSIZE * Constants.BLOCKSIZE];

        for (int x = 0; x < Constants.BLOCKSIZE; x++) {
            for (int y = 0; y < Constants.BLOCKSIZE; y++) {
                tile[i++] = pixels[x + y * pixWidth + start];
            }
        }

        return tile;
    }

    private int blockToStartPixel(int block) {
        int row = block % blockWidth;
        int col = (block - row) / blockHeight;
        return col * Constants.BLOCKSIZE * pixWidth + row * Constants.BLOCKSIZE;
    }
}
