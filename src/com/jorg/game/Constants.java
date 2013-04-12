/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game;

/**
 *
 * @author jthuijls
 */
abstract public class Constants {

    /*
     * game variables like resolution and scale
     */
    public static final int GAME_WIDTH = 320;  //512
    public static final int GAME_HEIGHT = 200; //320
    public static final int SCALE = 3;         // > 0.
    /*
     * engine performance
     */
    public static final int MAX_FPS = 120;
    public static final int MAX_UPDATES = 50;
    public static final double FPS_PERIOD = 1000000000L / MAX_FPS;
    public static final double UPD_PERIOD = 1000000000L / MAX_UPDATES;
    /*
     * sprite info. 
     */
    public static final int BLOCKSIZE = 8; // width / height of a sprite
    /*
     * day / night cycle, governed by the time cycle class. 
     * TIMECYCLE determines how many different changes there are in a full color
     *           cycle
     * TIMECHANGE determines how quickly time passes
     * TIMEDARKESTDELTA/TIMELIGHTESTDELTA determine the range of the color change:
     * from RGB - darkest to RGB + 10. 
     */
    public static final int TIMECYCLES = 288; 
    public static final double TIMECHANGE = 1; // seconds
    public static final int TIMEDARKESTDELTA = -100;
    public static final int TIMELIGHTESTDELTA = 10;
    /*
     * dungeon generator specific
     */
    public static final int ROOMRATIO = 5; // promille of dungeon covered in rooms
    public static final int RANDOMNESS = 70; // between 10 and 90

    /*
     * log
     */
    public static final int LOGSIZE = 15;
    public static final boolean VERBOSE = false;
    
    public static boolean inCanvas(int x, int y) {
        if (y < 0 || y >= Constants.GAME_HEIGHT || x < 0 || x >= Constants.GAME_WIDTH) {
            return false;
        } else {
            return true;
        }
    }
}
