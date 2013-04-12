/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.pathfinding;

import com.jorg.game.objects.Map;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author jorgthuijls
 */
public class FieldOfVision {

    private Point position;
    private int mapHeight;
    private int mapWidth;
    private Map map;
    private ArrayList<Point> sight = new ArrayList<Point>();
    private int visualRange = 15;

    /*
     * constructor. doesn't do anything atm. 
     */
    public FieldOfVision() {
    }

    /*
     * sets the visual range. do this before executing GO, otherwise the default
     * value of 10 will be used. can't be larger than 20 for performance sake. 
     */
    public void setVisualRange(int i) {
        visualRange = (i > 20) ? 20 : i;
        visualRange = (i < 5) ? 5 : i;

    }

    public ArrayList<Integer> go(Point start, Map m) {
        return go(start, m, visualRange);
    }
    /*
     * GO, the entry point of FOV. execute this by entering the position
     * coordinates in as a Point, and load the map. the map is an instance of
     * Map.
     */

    public ArrayList<Integer> go(Point start, Map m, int visual) {
        if (visual > 25) {
            visual = 25;
        }
        if (visual < 1) {
            visual = 3;
        }
        setVisualRange(visual);
        position = start;
        mapHeight = m.width - 1;
        mapWidth = m.height - 1;
        map = m;
        sight.clear();

        for (int octant = 1; octant < 9; octant++) {
            scan(1, octant, 1.0, 0.0);
        }

        //int[] s = new int[sight.size()];
        ArrayList<Integer> ss = new ArrayList<Integer>();
        ss.add(start.x + start.y * map.width);
        for (int i = 0; i < sight.size(); i++) {
            ss.add(sight.get(i).x + sight.get(i).y * map.width);
        }
        return ss;
        //return sight;

    }

    /*
     * checks if a cell is within map range. if it is, it will check
     * if the tile is within visible range. if it is within visible range,
     * this will check if the cell is blocking the view or not. 
     */
    private boolean testCell(int x, int y, boolean test) {

        if (x < 0 || y < 0 || x > mapHeight || y > mapWidth) {
            return false;
        }

        if (inRange(position.x, position.y, x, y)) {
            return test == map.isBlocked(x, y);
        } else {
            return false;
        }
    }

    /*
     * calculates the slope of a horizontal ray. 1 up, 2 to the right will
     * have a slope of 0.5, etc. 
     */
    private double slope(double x1, double y1, double x2, double y2, int i) {
        if (i == 0) {
            return getSlope(x1, y1, x2, y2);
        } else {
            return getSlopeInv(x1, y1, x2, y2);
        }
    }

    private double getSlope(double x1, double y1, double x2, double y2) {
        return (x1 - x2) / (y1 - y2);
    }

    /*
     * same as getSlope, only for vertical rays. 
     */
    private double getSlopeInv(double x1, double y1, double x2, double y2) {
        return (y1 - y2) / (x1 - x2);
    }

    /*
     * the heart of the field of vision. it divides the position's field of vision
     * in 8 parts (octants) and casts beams in outwards expanding lines.
     * if it encounters a cell that blocked the users vision, new beams will be
     * cast from the blocking cell outwards to see  which cells are visible
     * and which ones aren't.
     * it does this for every octant. 
     */
    private void scan(int depth, int octant, double startSlope, double endSlope) {
        int x = 0;
        int y = 0;
        switch (octant) {
            case 1:
                y = position.y - depth;
                x = position.x - (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x >= mapHeight || y >= mapHeight) {
                    break;
                }
                while (getSlope(x, y, position.x, position.y) >= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x - 1, y, false)) {
                                scan(depth + 1, octant, startSlope, getSlope(x - 0.5, y + 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x - 1, y, true)) {
                                startSlope = getSlope(x - 0.5, y - 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    x++;
                }
                x--; //we step back as the last step of the while has taken us past the limit
                break;
            case 2:
                y = position.y - depth;
                x = position.x + (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }
                while (getSlope(x, y, position.x, position.y) <= endSlope) {

                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x + 1, y, false)) {
                                scan(depth + 1, octant, startSlope, getSlope(x + 0.5, y + 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x + 1, y, true)) {
                                startSlope = -getSlope(x + 0.5, y - 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    x--;
                }
                x++; //we step back as the last step of the while has taken us past the limit
                break;
            case 3:
                x = position.x + depth;
                y = position.y - (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }
                while (getSlopeInv(x, y, position.x, position.y) <= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x, y - 1, false)) {
                                scan(depth + 1, octant, startSlope, getSlopeInv(x - 0.5, y - 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x, y - 1, true)) {
                                startSlope = -getSlopeInv(x + 0.5, y - 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    y++;
                }
                y--; //we step back as the last step of the while has taken us past the limit
                break;
            case 4:
                x = position.x + depth;
                y = position.y + (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }
                while (getSlopeInv(x, y, position.x, position.y) >= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x, y + 1, false)) {
                                scan(depth + 1, octant, startSlope, getSlopeInv(x - 0.5, y + 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x, y + 1, true)) {
                                startSlope = getSlopeInv(x + 0.5, y + 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    y--;
                }
                y++; //we step back as the last step of the while has taken us past the limit
                break;

            case 5:
                y = position.y + depth;
                x = position.x + (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }
                while (getSlope(x, y, position.x, position.y) >= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x + 1, y, false)) {
                                scan(depth + 1, octant, startSlope, getSlope(x + 0.5, y - 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x + 1, y, true)) {
                                startSlope = getSlope(x + 0.5, y + 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    x--;
                }
                x++; //we step back as the last step of the while has taken us past the limit
                break;

            case 6:
                y = position.y + depth;
                x = position.x - (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }

                while (getSlope(x, y, position.x, position.y) <= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x - 1, y, false)) {
                                scan(depth + 1, octant, startSlope, getSlope(x - 0.5, y - 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x - 1, y, true)) {
                                startSlope = -getSlope(x - 0.5, y + 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    x++;
                }
                x--; //we step back as the last step of the while has taken us past the limit
                break;

            case 7:
                x = position.x - depth;
                y = position.y + (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }

                while (getSlopeInv(x, y, position.x, position.y) <= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            if (testCell(x, y + 1, false)) {
                                scan(depth + 1, octant, startSlope, getSlopeInv(x + 0.5, y + 0.5, position.x, position.y));
                            }
                        } else {
                            if (testCell(x, y + 1, true)) {
                                startSlope = -getSlopeInv(x - 0.5, y + 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    y--;
                }
                y++; //we step back as the last step of the while has taken us past the limit
                break;

            case 8:
                x = position.x - depth;
                y = position.y - (int) (startSlope * (double) depth);
                if (x < 0 || y < 0 || x > mapHeight || y > mapHeight) {
                    break;
                }

                while (getSlopeInv(x, y, position.x, position.y) >= endSlope) {
                    if (inRange(position.x, position.y, x, y)) {
                        if (map.isBlocked(x, y)) {
                            // blocked, if prior open AND within range
                            if (testCell(x, y - 1, false)) { //recursion
                                scan(depth + 1, octant, startSlope, getSlopeInv(x + 0.5, y - 0.5, position.x, position.y));
                            }
                        } else {
                            // not blocked, if prior closed AND within range
                            if (testCell(x, y - 1, true)) {
                                startSlope = getSlopeInv(x - 0.5, y - 0.5, position.x, position.y);
                            }
                        }
                        sight.add(new Point(x, y));
                    }
                    y++;
                }
                y--; //we step back as the last step of the while has taken us past the limit
                break;

        }
        if (inRange(position.x, position.y, x, y) && !map.isBlocked(x, y)) {
            scan(depth + 1, octant, startSlope, endSlope);
        }
    }

    @SuppressWarnings("unused")
	private void octant(int depth, int octant, double startSlope, double endSlope) {
        int[] vars = getOctant(octant);
        int y = position.y + (depth * vars[0]);
        int x = position.x + (((int) (startSlope * (double) depth)) * vars[1]);
        //System.out.println("octant: " + octant);
        //System.out.print(slope(x, y, position.x, position.y, vars[2]) * vars[3] + " ");
        //System.out.println(slope(x, y, position.x, position.y, vars[2]) + " " + endSlope);

        if (x < 0 || y < 0 || x >= mapHeight || y >= mapHeight) {
        } else {
            while (slope(x, y, position.x, position.y, vars[2]) * vars[3] >= endSlope) {
                //System.out.println(" " + slope(x, y, position.x, position.y, vars[2]) * vars[3]);
                if (inRange(position.x, position.y, x, y)) {
                    if (map.isBlocked(x, y)) {
                        // blocked, if prior open AND within range
                        if (testCell(x + vars[4], y + vars[5], false)) {
                            //recursion
                            octant(depth + 1, octant, startSlope,
                                    slope(x + vars[6] / 2, y + vars[7] / 2, position.x, position.y, vars[2]));
                        }
                    } else {
                        // not blocked, if prior closed AND within range
                        if (testCell(x + vars[8], y + vars[9], true)) {
                            startSlope = slope(x + vars[10] / 2, y + vars[11] / 2, position.x, position.y, vars[2]) * vars[12];
                        }
                    }
                    sight.add(new Point(x, y));
                }
                x += vars[13];
                y += vars[14];
            }
            x += vars[15]; //we step back as the last step of the while has taken us past the limit
            y += vars[16];
        }

        if (inRange(position.x, position.y, x, y) && !map.isBlocked(x, y)) {
            octant(depth + 1, octant, startSlope, endSlope);
        }
    }

    private int[] getOctant(int i) {
        int[] vars = new int[16];
        int[] vars1 = {-1, -1, 0, 1, -1, 0, -1, 1, -1, 0, -1, 1, 1, 1, 0, -1, 0};
        int[] vars2 = {-1, 1, 0, -1, 1, 0, 1, 1, 1, 0, 1, -1, 1, -1, 0, 1, 0};
        int[] vars3 = {1, -1, 1, -1, 0, -1, -1, -1, 0, -1, -1, 1, -1, 0, -1, 0, 1};
        int[] vars4 = {1, 1, 1, 1, 0, 1, -1, 1, 0, 1, 1, 1, 1, 0, -1, 0, 1};
        int[] vars5 = {1, 1, 0, 1, 1, 0, 1, -1, 1, 0, 1, 1, 1, -1, 0, 1, 0};
        int[] vars6 = {1, -1, 0, -1, -1, 0, -1, -1, -1, 0, -1, 1, -1, 1, 0, -1, 0};
        int[] vars7 = {-1, 1, 1, -1, 0, 1, 1, 1, 0, 1, -1, 1, -1, 0, 1, 0, -1};
        int[] vars8 = {-1, -1, 1, 1, 0, -1, 1, -1, 0, -1, -1, -1, 1, 0, 1, 0, -1};

        switch (i) {
            case 1:
                return vars1;
            case 2:
                return vars2;
            case 3:
                return vars3;
            case 4:
                return vars4;
            case 5:
                return vars5;
            case 6:
                return vars6;
            case 7:
                return vars7;
            case 8:
                return vars8;
        }
        return vars;

    }

    //See if the provided points are within visual range
    public boolean inRange(int x1, int y1, int x2, int y2) {
        if (x1 < 0 || y1 < 0 || x1 > mapHeight || y1 > mapHeight
                || x1 < 0 || y1 < 0 || x1 > mapHeight || y1 > mapHeight) {

            return false;
        }

        //if they're on the same axis, we only need to test one
        //value, which is computationaly cheaper than what we do below
        if (x1 == x2) return Math.abs(y1 - y2) < visualRange;
        if (y1 == y2) return Math.abs(x1 - x2) < visualRange;
        return (Math.pow((double) (x1 - x2), 2.0) + Math.pow((double) (y1 - y2), 2.0)
                <= Math.pow((double) visualRange, 2.0));

    }

    //See if the provided points are within visual range
    public static double distance(double x1, double y1, double x2, double y2) {
        if (x1 == x2) return Math.abs(y1 - y2);
        if (y1 == y2) return Math.abs(x1 - x2);
        return (Math.pow((double) (x1 - x2), 2.0) + Math.pow((double) (y1 - y2), 2.0));
    }
}
