package com.jorg.game.engine.pathfinding;

import com.jorg.game.entities.MovingEntity;
import com.jorg.game.objects.Map;
import java.util.ArrayList;

/**
 * A* algorithm implementation
 *
 * @author jthuijls
 */
abstract public class AStar {

    private static Map map;
    public static boolean found = false;

    public static ArrayList<AStarPoint> travel(Map map, int s, int e, MovingEntity m) {
        return AStar.travel(map, s, e, Integer.MAX_VALUE, m);
    }

    public static boolean inRange(Map map, int s, int e, int max, MovingEntity m) {
        ArrayList<AStarPoint> fov = travel(map, s, e, 50, m);
        return found && fov.size() < max;
    }

    public static boolean inRange(Map map, int x1, int y1, int x2, int y2, int max, MovingEntity m) {
        ArrayList<AStarPoint> fov = travel(map, x1 + y1 * map.width, x2 + y2 * map.width, max, m);
        return found;
    }

    public static ArrayList<AStarPoint> travel(Map map, int x1, int y1, int x2, int y2, int max, MovingEntity m) {
        int xx = x1 + y1 * map.width;
        int yy = x2 + y2 * map.width;
        return travel(map, xx, yy, max, m);
    }

    public static ArrayList<AStarPoint> travel(Map map, int s, int e, int max, MovingEntity m) {
        AStar.map = map;
        found = false;
        AStarPoint start = new AStarPoint(s % map.width, (s - s % map.width) / map.width);
        AStarPoint end = new AStarPoint(e % map.width, (e - e % map.width) / map.width);
        AStarPoint curr = start;
        curr.h = tieBreakingEuclidianHeuristic(start, curr, end);
        curr.g = 1;
        curr.setMaptile(map.width);
        ArrayList<AStarPoint> open = new ArrayList<AStarPoint>();
        ArrayList<AStarPoint> closed = new ArrayList<AStarPoint>();
        ArrayList<AStarPoint> path = new ArrayList<AStarPoint>();
        int tries = 0;
        open.add(start);
        while (!found && !open.isEmpty() && tries < 5000) {
            // find the best one:
            double small = Double.MAX_VALUE;
            double smaller = Double.MAX_VALUE;
            for (AStarPoint node : open) {
                small = node.h;
                if (small < smaller) {
                    curr = node;
                    smaller = small;
                }
            }

            closed.add(curr);

            if ((curr.x == end.x && curr.y == end.y) || curr.g > max) {
                found = true;
            }

            ArrayList<AStarPoint> expand = getNeighbours(curr, m);
            for (AStarPoint next : expand) {
                next.setMaptile(map.width);
                next.h = tieBreakingEuclidianHeuristic(start, next, end);
                next.g = curr.g + map.tiles[next.maptile].type.movement;
                next.f = next.g + next.h;
                next.setParent(curr);
                if (inArray(closed, next) > 0) {
                    closed.get(inArray(closed, next)).g = curr.g
                            + map.tiles[closed.get(inArray(closed, next)).maptile].type.movement;
                    closed.get(inArray(closed, next)).f = closed.get(inArray(closed, next)).g
                            + closed.get(inArray(closed, next)).h;
                } else if (inArray(open, next) > 0) {
                    open.get(inArray(open, next)).g = curr.g
                            + map.tiles[open.get(inArray(open, next)).maptile].type.movement;
                    open.get(inArray(open, next)).f = open.get(inArray(open, next)).g
                            + open.get(inArray(open, next)).h;

                } else {
                    open.add(next);
                }


            }
            open.remove(curr);
            tries++;
        }

        AStarPoint last = closed.get(closed.size() - 1);
        path.add(last);
        do {
            AStarPoint next = last.getParent();
            if (next == null) {
                break;
            } else {
                last = next;
                if (start.x == next.x && start.y == next.y) {
                    break;
                }
            }
            path.add(last);
        } while (true);
        return path;
    }

    private static int inArray(ArrayList<AStarPoint> array, AStarPoint n) {

        for (AStarPoint t : array) {
            if (t.x == n.x && t.y == n.y) {
                return array.indexOf(t);
            }
        }
        return 0;
    }

    private static double tieBreakingEuclidianHeuristic(AStarPoint start, AStarPoint curr, AStarPoint end) {
        double dx1 = curr.x - end.x;
        double dy1 = curr.y - end.y;
        double dx2 = start.x - end.x;
        double dy2 = start.y - end.y;
        double cross = dx1 * dy2 - dx2 * dy1;
        if (curr.x + curr.y * map.width < map.tiles.length) {
            double m = map.tiles[curr.x + curr.y * map.width].type.movement;
            if (cross < 0) {
                cross = -cross; // absolute value
            }
            return m * (Math.abs(dx1) + Math.abs(dy1) + cross * 0.0002);
        } else {
            return Double.MAX_VALUE;
        }
    }

    private static ArrayList<AStarPoint> getNeighbours(AStarPoint check, MovingEntity m) {
        ArrayList<AStarPoint> list = new ArrayList<AStarPoint>();
        int x = check.x;
        int y = check.y;
        int[] dir = new int[8];
        dir[0] = 1;
        dir[1] = 0;
        dir[2] = -1;
        dir[3] = 0;
        dir[4] = 0;
        dir[5] = 1;
        dir[6] = 0;
        dir[7] = -1;

        for (int i = 0; i < 8; i += 2) {
            int xx = x + dir[i];
            int yy = y + dir[i + 1];
            if (xx < 0) xx = map.width - xx;
            if (xx >= map.width) xx = xx - map.width;
            if (yy < 0) yy = map.height - yy;
            if (yy >= map.height) yy = yy - map.height;

            boolean test;
            if (m == null) {
                test = map.tiles[xx + yy * map.width].type.diggable;
            } else {
                test = (map.tiles[xx + yy * map.width].type.swimmable && m.cls.attributes.canSwim()
                        || map.tiles[xx + yy * map.width].type.walkable && m.cls.attributes.canWalk());
            }
            if (test) list.add(new AStarPoint(xx, yy));
        }
        return list;
    }
}