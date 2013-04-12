/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jthuijls
 *
 * To add a new function: create a function as a public final member.
 * Instantiate with at least one key attached to it (constructor). Add more keys
 * to a function with addKey. Attaching the same key to multiple functions is
 * possible, in some sort of "macro" like behaviour ie, add the same button to
 * up and left to go diagonally with 1 button push.
 */
public final class KeyInput implements KeyListener, Serializable {

    public boolean[] status = new boolean[256];
    public List<Function> functions = new ArrayList<Function>();
    public HashMap<Integer, Integer> replacement = new HashMap<Integer, Integer>();
    public final Function up = new Function(KeyEvent.VK_UP);
    public final Function down = new Function(KeyEvent.VK_DOWN);
    public final Function left = new Function(KeyEvent.VK_LEFT);
    public final Function right = new Function(KeyEvent.VK_RIGHT);
    public final Function escape = new Function(KeyEvent.VK_ESCAPE);
    public final Function enter = new Function(KeyEvent.VK_ENTER);
    public final Function attack1 = new Function(KeyEvent.VK_E);
    public final Function one = new Function(KeyEvent.VK_1);
    public final Function two = new Function(KeyEvent.VK_2);
    public final Function three = new Function(KeyEvent.VK_3);
    public final Function four = new Function(KeyEvent.VK_4);
    public final Function nine = new Function(KeyEvent.VK_9);
    public final Function zero = new Function(KeyEvent.VK_0);
    public final Function map = new Function(KeyEvent.VK_M);
    public final Function pause = new Function(KeyEvent.VK_SPACE);
    public final Function log = new Function(KeyEvent.VK_L);
    public final Function inventory = new Function(KeyEvent.VK_I);

    public KeyInput() {
        // additional keys to a function
        replacement.put(KeyEvent.VK_W, KeyEvent.VK_UP);
        replacement.put(KeyEvent.VK_S, KeyEvent.VK_DOWN);
        replacement.put(KeyEvent.VK_A, KeyEvent.VK_LEFT);
        replacement.put(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
        replacement.put(KeyEvent.VK_NUMPAD1, KeyEvent.VK_1);
        replacement.put(KeyEvent.VK_NUMPAD2, KeyEvent.VK_2);
        replacement.put(KeyEvent.VK_NUMPAD3, KeyEvent.VK_3);
        replacement.put(KeyEvent.VK_NUMPAD4, KeyEvent.VK_4);

    }

    public synchronized void update() {
        for (Function f : functions) {
            f.press(f.update());
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        status[getCode(ke)] = true;
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        status[getCode(ke)] = false;
    }

    private int getCode(KeyEvent ke) {
        int i;
        try {
            i = replacement.get(ke.getKeyCode());
        } catch (NullPointerException e) {
            i = ke.getKeyCode();
        }
        return i;
    }

    public final class Function implements Serializable {

        private boolean pressed;
        private boolean once;
        private int registered;

        public Function(int i) {
            registered = i;
            functions.add(this);
        }

        public boolean update() {
            return status[registered];
        }

        public void press(boolean p) {
            if (p) {
                if (once && !pressed) {
                    once = false;
                    pressed = true;
                } else {
                    once = !pressed;
                }
            } else {
                once = pressed = false;
            }
        }

        public boolean pressed() {
            return once || pressed;
        }

        public boolean once() {
            return once;
        }
    }
}
