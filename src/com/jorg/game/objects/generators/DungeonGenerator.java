package com.jorg.game.objects.generators;

import com.jorg.game.Constants;
import com.jorg.game.engine.pathfinding.AStar;
import com.jorg.game.engine.pathfinding.AStarPoint;
import com.jorg.game.objects.Map;
import com.jorg.game.objects.tilecollection.CoreTile;
import com.jorg.game.objects.tilecollection.TileType;
import com.jorg.game.objects.tilecollection.Tiles;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jorgthuijls
 */
abstract class DungeonGenerator {

    private static Random r = new Random();
    private static int height;
    private static int width;
    private static ArrayList<Integer> roomList1 = new ArrayList<Integer>();
    private static ArrayList<Integer> roomList2 = new ArrayList<Integer>();
    private static Map map;
    private static int rx, ry;
    private static int rooms = 0;

    public static Map create(int x, int y) {
        reset();
        width = uneven(x);
        height = uneven(y);
        int attempts = 0;
        int maxRooms = (int) ((width * height) * ((double) Constants.ROOMRATIO / 1000));
        map = new Map(width, height);
        map.affectedByVision = true;

        for (int i = 0; i < width * height; i++) {
            map.setTile(i, Tiles.rock(i % width, (i - i % width) / width));
        }

        System.out.println("-----------------------------------------------------------");
        System.out.println("  Starting dungeon builder, generating map " + x + " by " + y);
        System.out.print("  Max rooms: " + maxRooms + ". Rooms... ");

        while (attempts++ < 1000 && rooms < maxRooms) {
            boolean success = true;
            if (r.nextBoolean()) {
                success = squareRoom();
            } else {
                success = multipleSquareRoom(r.nextInt(7) + 3);
            }

            if (success) {
                // if creating the room succeeded
                if (rooms == 0) {
                    // first room: set start. 
                    map.startx = rx;
                    map.starty = ry;
                }
                rooms++;
                System.out.print(" " + rooms);
            }
        }
        // last room, set end. 
        map.endx = rx;
        map.endy = ry;

        System.out.println("\n  Connecting rooms");
        if (!connectRooms()) {
            System.err.println("  Creating map failed, retrying.");
            //create(x, y);
        }
        map.generateMonsters();
        System.out.println("-----------------------------------------------------------");
        return map;
    }

    public static void reset() {
        height = 0;
        width = 0;
        rx = 0;
        ry = 0;
        map = null;
        roomList1 = new ArrayList<Integer>();
        roomList2 = new ArrayList<Integer>();
        rooms = 0;
    }

    private static boolean connectRooms() {
        for (int i = 0; i < roomList1.size() - 1; i++) {
            int s = roomList1.get(i);
            int e = roomList2.get(i + 1);
            map.setTile(s, Tiles.door(0, 0));
            map.setTile(e, Tiles.door(0, 0));
            ArrayList<AStarPoint> path = AStar.travel(map, s, e, null);
            if (AStar.found == false) {
                System.err.println("  room " + i + " not connected!" + s + " to " + e);
                for (AStarPoint p : path) {
                    if (map.getTile(p.maptile).type != TileType.DOOR) {
                        map.setTile(p.maptile, Tiles.corridor(p.x, p.y));
                    }
                }
                return false;
            }
            if (!path.isEmpty()) {
                for (AStarPoint p : path) {
                    if (map.getTile(p.maptile).type != TileType.DOOR) {
                        map.setTile(p.maptile, Tiles.corridor(p.x, p.y));
                    }
                }
            }
        }
        return true;
    }

    private static boolean squareRoom() {
        int startW = uneven(r.nextInt(width - 2) + 1);
        int startH = uneven(r.nextInt(height - 2) + 1);

        // room variables
        int roomH = uneven(randomizer(15));
        int roomW = uneven(randomizer(19));

        // first pass, see if the room fits. 
        for (int x = startH; x < roomH + startH; x++) {
            if (x >= height) {
                return false;
            }
            for (int y = startW; y < roomW + startW; y++) {
                if (y >= width) {
                    return false;
                }
                if (!map.getTile(y + x * width).type.diggable) {
                    return false;
                }
            }
        }

        // second pass, build the room
        ArrayList<Integer> walls = new ArrayList<Integer>();
        for (int y = startH; y < roomH + startH; y++) {
            for (int x = startW; x < roomW + startW; x++) {
                if ((y == startH || y == roomH + startH - 1)
                        && (x == startW || x == roomW + startW - 1)) {

                    // corners
                    map.setTile(x + y * width, Tiles.wall(x, y));
                } else if ((y == startH || y == roomH + startH - 1)
                        || (x == startW || x == roomW + startW - 1)) {

                    // other walls. dont want doors in corners. 
                    walls.add(x + y * width);
                    map.setTile(x + y * width, Tiles.wall(x, y));
                } else {
                    ry = y * Constants.BLOCKSIZE - 1;
                    rx = x * Constants.BLOCKSIZE - 1;
                    map.setTile(x + y * width, Tiles.floor(x, y));
                }
            }
        }

        int door1 = walls.get(r.nextInt(walls.size() - 1));
        walls.remove(walls.indexOf(door1));
        int door2 = walls.get(r.nextInt(walls.size() - 1));
        roomList1.add(door1);
        roomList2.add(door2);
        map.setTile(door1, Tiles.door(door1 % width, (door1 - door1 % width) / width));
        map.setTile(door2, Tiles.door(door2 % width, (door2 - door2 % width) / width));
        return true;
    }

    private static boolean multipleSquareRoom(int repeats) {
        int startW = uneven(r.nextInt(width - 2) + 1);
        int startH = uneven(r.nextInt(height - 2) + 1);
        ArrayList<Integer> walls = new ArrayList<Integer>();

        // room variables
        int roomH = uneven(randomizer(15) + 9);
        int roomW = uneven(randomizer(15) + 9);
        // check availability
        for (int x = startH; x < roomH + startH; x++) {
            if (x >= height) {
                return false;
            }
            for (int y = startW; y < roomW + startW; y++) {
                if (y >= width) {
                    return false;
                }
                if (!map.getTile(y + x * width).type.diggable) {
                    return false;
                }
            }
        }

        // build room. 
        for (int i = 0; i < repeats; i++) {
            int h = uneven(r.nextInt(roomH - 5) + 3);
            int w = uneven(r.nextInt(roomW - 5) + 3);
            int sh = uneven(startH + ((roomH - h) / 2));
            int sw = uneven(startW + ((roomW - w) / 2));
            for (int y = sh; y < sh + h; y++) {
                for (int x = sw; x < sw + w; x++) {
                    map.setTile(x + y * width, Tiles.floor(x, y));
                    ry = y * Constants.BLOCKSIZE - 2;
                    rx = x * Constants.BLOCKSIZE - 2;
                }
            }
        }

        // put walls.
        for (int y = startH; y < startH + roomH; y++) {
            for (int x = startW; x < startW + roomW; x++) {
                CoreTile t = map.getTile(x + y * width);
                boolean currf = t.type == TileType.FLOOR;
                // most walls
                if ((currf) // up, down, left, right
                        && (isRock(x, y - 1) || isRock(x, y + 1)
                        || isRock(x - 1, y) || isRock(x + 1, y))) {
                    map.setTile(x + y * width, Tiles.wall(x, y));
                }
            }
        }

        // put odd wallcorners.
        for (int y = startH; y < startH + roomH; y++) {
            for (int x = startW; x < startW + roomW; x++) {
                boolean currf = map.getTile(x + y * width).type == TileType.FLOOR;
                if (currf && (isWall(x - 1, y) && isWall(x, y - 1) && isRock(x - 1, y - 1))) {
                    map.setTile(x + y * width, Tiles.wall(x, y));
                }
                if (currf && (isWall(x + 1, y) && isWall(x, y - 1) && isRock(x + 1, y - 1))) {
                    map.setTile(x + y * width, Tiles.wall(x, y));
                }
                if (currf && (isWall(x + 1, y) && isWall(x, y + 1) && isRock(x + 1, y + 1))) {
                    map.setTile(x + y * width, Tiles.wall(x, y));
                }
                if (currf && (isWall(x - 1, y) && isWall(x, y + 1) && isRock(x - 1, y + 1))) {
                    map.setTile(x + y * width, Tiles.wall(x, y));
                }
            }
        }

        // if the tile is a wall, and either both tiles above/below or 
        // left/right are also walls, add it to our door list. 
        for (int y = startH; y < startH + roomH; y++) {
            for (int x = startW; x < startW + roomW; x++) {
                if (isWall(x, y)
                        && ((isWall(x + 1, y) && isWall(x - 1, y))
                        || (isWall(x, y - 1) && isWall(x, y + 1)))) {
                    walls.add(x + y * width);
                }
            }
        }
        int door1 = walls.get(r.nextInt(walls.size() - 1));
        walls.remove(walls.indexOf(door1));
        int door2 = walls.get(r.nextInt(walls.size() - 1));
        roomList1.add(door1);
        roomList2.add(door2);
        map.setTile(door1, Tiles.door(door1 % width, (door1 - door1 % width) / width));
        map.setTile(door2, Tiles.door(door2 % width, (door2 - door2 % width) / width));
        return true;
    }

    private static boolean isRock(int x, int y) {
        return (map.getTile(x + y * width).type == TileType.ROCK);
    }

    private static boolean isWall(int x, int y) {
        return (map.getTile(x + y * width).type == TileType.WALL);
    }

    private static int uneven(int i) {
        int m = (i % 2 == 0) ? i - 1 : i;
        return (m <= 0) ? 1 : m;
    }

    private static int randomizer(int i) {
        double result = 0.0;
        double m = r.nextInt(100 + 40) + 30;
        result = i * (m / 100);
        return (result < 5) ? 5 : (int) result;
    }
}