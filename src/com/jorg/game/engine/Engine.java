package com.jorg.game.engine;

import com.jorg.game.Constants;
import com.jorg.game.engine.input.KeyInput;
import com.jorg.game.engine.sfx.Color;
import com.jorg.game.engine.state.State;
import com.jorg.game.engine.state.StateInitial;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

final public class Engine extends JFrame {     // main class for the game as a Swing application

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Define constants for the game
    private State activeState;
    private GameCanvas canvas;
    private BufferedImage image = new BufferedImage(
            Constants.GAME_WIDTH, Constants.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
    public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    int frames = 0;
    int updates = 0;
    public KeyInput input = new KeyInput();

    // Constructor to initialize the UI components and game objects
    public Engine(String name) {
        Dimension d = new Dimension(
                Constants.GAME_WIDTH * Constants.SCALE,
                Constants.GAME_HEIGHT * Constants.SCALE);
        // Initialize the game objects

        // UI components
        canvas = new GameCanvas();
        canvas.setMinimumSize(d);
        canvas.setMaximumSize(d);
        canvas.setPreferredSize(d);
        this.add(canvas);

        this.setSize(d);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setTitle(name);
        this.setResizable(false);
        this.setVisible(true);

    }

    // All the game related codes here
    // Initialize all the game objects, run only once in the constructor of the main class.
    public void gameInit() {
        activeState = StateInitial.instance(this);
    }

    // Shutdown the game, clean up code that runs only once.
    public void gameShutdown() {
        System.out.println("Closing game");
        activeState.gameShutDown();
        this.dispose(); // this closes the JFrame
    }

    // To start and re-start the game.
    public void gameStart() {
        this.gameInit();
        // Create a new thread
        Thread gameThread = new Thread() {

            @Override
            public void run() {
                gameLoop();
            }
        };
        // Start the thread. start() calls run(), which in turn calls gameLoop().
        gameThread.start();
    }

    private void gameLoop() {

        double beginTime;	// the time when the cycle begun
        int timeDiff;		// the time it took for the cycle to execute
        double timeDiffD;
        int totalSleep = 0;
        double unprocessed = 0;
        double updateTime = System.nanoTime();
        double timeCount = System.nanoTime();

        while (isRunning()) {
            beginTime = System.nanoTime();
            frames++;

            this.gameDraw();
            if (!canvas.hasFocus()) {
                // stop updates the window has no focus, and stop it from 
                // mass updating when resuming. 
                updateTime = System.nanoTime();
            } else {
                long now = System.nanoTime();
                unprocessed += (now - updateTime) / Constants.UPD_PERIOD;
                updateTime = now;
                while (unprocessed >= 1) {
                    updates++;
                    this.gameUpdate();
                    unprocessed -= 1;
                }
            }
            
            timeDiffD = Constants.FPS_PERIOD - (System.nanoTime() - beginTime);  // in milliseconds
            if (timeDiffD > 0) {

                timeDiff = (int) timeDiffD / 1000000;
                totalSleep += timeDiff;

                try {
                    // Provides the necessary delay and also yields control so that other thread can do work.
                    Thread.sleep(timeDiff);
                } catch (Exception e) {
                }
            }

            if (System.nanoTime() - timeCount > 1000000000L) {
                System.out.println(
                        "updates: " + updates
                        + ", frames: " + frames
                        + ", total sleep time: " + totalSleep);
                timeCount = System.nanoTime();
                totalSleep = 0;
                frames = 0;
                updates = 0;
            }

        }

        this.gameShutdown();
    }

    public void setState(State state) {
        activeState = state;
    }

    public void resetState() {
        activeState.reset();
    }

    public boolean isRunning() {
        return activeState.isRunning();
    }

    // Update the state and position of all the game objects,
    // detect collisions and provide responses.
    public void gameUpdate() {
        input.update();
        activeState.gameInput();
        activeState.gameUpdate();
    }

    public void gameFocus() {
        activeState.gameFocus();
    }

    // Refresh the display. Called back via rapaint(), which invoke the paintComponent().
    private void gameDraw() {
        // resetting the pixels, then redrawing. otherwise, previous frames
        // will stay on the screen. 
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
        canvas.render();
    }

    private Graphics changeGraphic(Graphics g) {
        return activeState.changeGraphic(g);
    }

    // Custom drawing panel, written as an inner class.
    private class GameCanvas extends Canvas {

        private static final long serialVersionUID = 1187252386149230870L;

        // Constructor
        public GameCanvas() {
            setFocusable(true);  // so that can receive key-events
            requestFocus();
            this.addKeyListener(input);
        }

        public void render() {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
                requestFocus();
                return;
            }

            Graphics g = bs.getDrawGraphics();
            //g.fillRect(0, 0, getWidth(), getHeight());

            activeState.gameDraw();
            if (!canvas.hasFocus()) {
                for (int i = 0; i < pixels.length; i++) {
                    pixels[i] = Color.gray(pixels[i]);
                }
                gameFocus();
            }
            int ww = Constants.GAME_WIDTH * Constants.SCALE;
            int hh = Constants.GAME_HEIGHT * Constants.SCALE;
            int xo = (getWidth() - ww) / 2;
            int yo = (getHeight() - hh) / 2;
            g.drawImage(image, xo, yo, ww, hh, null);
            g = changeGraphic(g);
            g.dispose();
            bs.show();
        }
    }
}
