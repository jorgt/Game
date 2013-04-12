/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jorg.game.entities;

import java.util.Random;

/**
 *
 * @author jorgthuijls
 */
abstract public class MonsterFactory {

    public static Monster getMonster(int lvl) {
        Random r = new Random();
        if (r.nextInt(4) == 0) 
            return new SwimmingMonster();
        else 
            return new Monster();
    }
}
