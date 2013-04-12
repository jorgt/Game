package com.jorg.game.objects;

import com.jorg.game.Constants;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.sfx.Screen;
import com.jorg.game.engine.sfx.Text;
import com.jorg.game.entities.Inventory;
import com.jorg.game.entities.Monster;
import com.jorg.game.entities.MonsterFactory;
import com.jorg.game.entities.Player;
import com.jorg.game.objects.items.Item;
import com.jorg.game.objects.tilecollection.CoreTile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jthuijls
 */
public class Map implements Serializable {

    public int height;
    public int width;
    public static int pixHeight;
    public static int pixWidth;
    public CoreTile[] tiles;
    protected int[] pix;
    public boolean affectedByTime = false;
    public boolean affectedByVision = false;
    protected Random r = new Random();
    // starting vars
    public int startx = 0, starty = 0;
    public int endx = 0, endy = 0;
    // entity stuff
    //public Player player;
    protected ArrayList<Integer> fov;
    protected static ArrayList<Monster> monsters = new ArrayList<Monster>();;
    protected static ArrayList<Item> items = new ArrayList<Item>();

    public Map(int width, int height) {
        monsters = new ArrayList<Monster>();
        fov = new ArrayList<Integer>();
        this.height = height;
        this.width = width;
        Map.pixHeight = height * Constants.BLOCKSIZE;
        Map.pixWidth = width * Constants.BLOCKSIZE;
        tiles = new CoreTile[width * height];
    }

    static public void addItem(Item i) {
        items.add(i);
    }

    public void draw(Screen screen) {
        drawBackground(screen);
        drawEntities(screen);
        Effects.drawEffects(screen);
        drawItems(screen);
    }

    protected void drawBackground(Screen screen) {
        int hue = (affectedByTime) ? TimeCycle.hue() : -100;
        for (int x = screen.xOff / 8; x < (screen.xOff + Constants.GAME_WIDTH) / 8 + 1; x++) {
            int ww = (x >= width) ? x - width : x;
            for (int y = screen.yOff / 8; y < (screen.yOff + Constants.GAME_HEIGHT) / 8 + 1; y++) {
                int hh = (y >= height) ? y - height : y;
                if (!tiles[ww + hh * width].visited && affectedByVision) {
                    continue;
                }
                if (tiles[ww + hh * width].visible && affectedByVision) {
                    tiles[ww + hh * width].render(screen, this, x * 8
                            - screen.xOff, y * 8 - screen.yOff, -20);
                } else {
                    tiles[ww + hh * width].render(screen, this, x * 8
                            - screen.xOff, y * 8 - screen.yOff, hue);
                }
            }
        }
    }

    protected void drawItems(Screen screen) {
        for (Item e : items) {
            e.sprite.render(screen);
        }
    }

    protected void drawEntities(Screen screen) {
        for (Monster m : monsters) {
            m.render(screen, this, Player.get().get());
        }
    }

    public void drawOverview(Screen screen) {
        boolean all = true;
        int pixSize;
        if (Constants.GAME_WIDTH / width < Constants.GAME_HEIGHT / height) {
            pixSize = Constants.GAME_WIDTH / width;
        } else {
            pixSize = Constants.GAME_HEIGHT / height;
        }
        pixSize = (pixSize < 1)? 1 : pixSize;
        int a = (Constants.GAME_WIDTH / 2) - ((width * pixSize) / 2);
        int b = (Constants.GAME_HEIGHT / 2) - ((height * pixSize) / 2);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int hue = (getTile(y, x).visible) ? 0 : -100;
                if ((getTile(y, x).visited || all) || !affectedByVision || getTile(y, x).visible) {
                    for (int xx = 0; xx <= pixSize; xx++) {
                        for (int yy = 0; yy <= pixSize; yy++) {
                            screen.renderPixel(xx + (pixSize * x) + a,
                                    yy + (pixSize * y) + b,
                                    getTile(y, x).type.mapColor,
                                    getTile(y, x).type.gray, hue);
                        }
                    }
                }
            }
        }

        Text.write(screen, "map", 4, 4, Color.get(0, 255, 255, 255));
        Text.write(screen, "map", 5, 5, Color.get(215, 255, 255, 255));
        for (int i = 0; i <= pixSize * 2; i++) {
            for (int j = 0; j <= pixSize * 2; j++) {
                screen.renderPixel(Player.get().getTileX() * pixSize + a + i,
                        Player.get().getTileY() * pixSize + b + j, 144, false, 0);
            }
        }
        for (Monster m : monsters) {
            if ((isVisited(m.getTileY(), m.getTileX()) || all) || !affectedByVision) {
                for (int i = 0; i <= pixSize; i++) {
                    for (int j = 0; j <= pixSize; j++) {
                        screen.renderPixel(m.getTileX() * pixSize + a + i,
                                m.getTileY() * pixSize + b + j, m.mapColor, false, -40);
                    }
                }
            }
        }
    }

    public void generateMonsters() {
        monsters.clear();
        double max = (height * width) * 0.005; // 0.005
        System.out.println("  Generating " + max + " monsters.");
        while (monsters.size() < max) {
            CoreTile set = getTile(r.nextInt((height * width) - 1));
            if (!set.type.blocking) {
                Monster m = MonsterFactory.getMonster(0);
                m.x = set.x * Constants.BLOCKSIZE;
                m.y = set.y * Constants.BLOCKSIZE;
                if ((set.type.swimmable && m.cls.attributes.canSwim())
                        || (set.type.walkable && m.cls.attributes.canWalk())) {
                    monsters.add(m);
                }
            }
        }
    }

    public CoreTile getTile(int i) {
        if (i < tiles.length && i >= 0) {
            return tiles[i];
        } else {
            return null;
        }
    }

    public CoreTile getTile(int x, int y) {
        if (y + x * width < tiles.length && y + x * width >= 0) {
            return tiles[y + x * width];
        } else {
            return null;
        }
    }

    protected void handleCollisions() {
        for (int i = 0; i < Effects.effects.size(); i++) {
            for (Monster m : monsters) {
                Effects.effects.get(i).collides(m, Constants.BLOCKSIZE, this);
            }
            Effects.effects.get(i).collides(Player.get(), Constants.BLOCKSIZE, this);
        }
    }

    public boolean inFov(int i) {
        return fov.contains(i);
    }

    public void initialize(Screen screen, Player p) {
        for (CoreTile t : tiles) {
            t.initialize(screen, this);
        }
        //this.player = p;
        Player.get().start(startx, starty);
        Player.get().setMap(this);
    }

    public boolean inRange(int x, int y) {
        if (y < 0 || y >= height || x < 0 || x >= width) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isBlocked(int x, int y) {
        int p = x + y * width;
        if (p < 0 || p >= height * width) {
            return false;
        }
        return tiles[p].type.blocking;
    }

    public boolean isVisible(int x, int y) {
        CoreTile tile = getTile(x, y);
        return (tile == null) ? false : tile.visible;
    }

    public boolean isVisited(int x, int y) {
        CoreTile tile = getTile(x, y);
        return (tile == null) ? false : tile.visited;
    }

    public void setAllTiles(CoreTile[] tls) {
        tiles = tls;
    }

    public void setFoV(ArrayList<Integer> pv) {
        int xx = (int) (Player.get().getTileX());
        int yy = (int) (Player.get().getTileY());
        if ((xx + yy * width < height * width || xx + yy * width < 0)) {
            for (int p : fov) {
                if (p >= height * width) {
                    continue;
                }
                tiles[p].visible = false;
            }

            fov = pv;
            for (int p : fov) {
                if (p >= height * width) {
                    continue;
                }
                tiles[p].visible = true;
                tiles[p].visited = true;
            }
        }
    }

    public void setTile(int i, CoreTile tile) {
        tiles[i] = tile;
    }

    public void tileToBlock(int block, int tile, int color, boolean m, boolean f) {
    }

    public void toPix(int[] pix, int x, int y) {
    }

    public void update(Screen screen) {
        this.updateMap(screen);
        this.updateEntities();
        Effects.updateEffects(this);
        this.updateItems();
        this.handleCollisions();
    }

    protected void updateItems() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).sprite.intersect(Player.get())) {
                items.get(i).sprite.removed = true;
                Inventory.add(items.get(i));
            }
            if (items.get(i).sprite.isRemoved()) {
                items.remove(i);
            }
            else {
                items.get(i).sprite.update();
            }
        }
    }

    protected void updateEntities() {
        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i).isRemoved()) {
                monsters.remove(i);
            }
            else if (true) {
                monsters.get(i).update(Player.get());
            }
        }
    }

    protected void updateMap(Screen screen) {
        for (int x = screen.xOff / 8; x < (screen.xOff + Constants.GAME_WIDTH) / 8 + 1; x++) {
            int ww = (x >= width) ? x - width : x;
            for (int y = screen.yOff / 8; y < (screen.yOff + Constants.GAME_HEIGHT) / 8 + 1; y++) {
                int hh = (y >= height) ? y - height : y;
                tiles[ww + hh * width].update();
            }
        }
    }
}
