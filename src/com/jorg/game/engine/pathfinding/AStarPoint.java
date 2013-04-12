package com.jorg.game.engine.pathfinding;

/**
 *
 * @author jorgthuijls
 */
public class AStarPoint extends java.awt.Point {

    /**
     * 
     */
    private static final long serialVersionUID = 2456269266097237674L;
    private double movement;
    private double heur;
    public double f, g, h;
    private AStarPoint parent;
    public int maptile;
    public boolean start = false;
    public boolean end = false;

    public AStarPoint(int x, int y) {
        super(x, y);
    }

    public void setHeuristic(double a) {
        heur = a;
    }

    public void setMovement(double a) {
        movement = a;
    }

    public double getMovement() {
        return movement;
    }

    public double getHeuristic() {
        return heur;
    }

    public void setParent(AStarPoint p) {
        parent = p;
    }

    public AStarPoint getParent() {
        return parent;
    }

    public double getF() {
        return movement + heur;
    }

    public void setMaptile(int widthInBlocks) {
        maptile = x + y * widthInBlocks;
    }
}
