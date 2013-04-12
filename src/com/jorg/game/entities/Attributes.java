package com.jorg.game.entities;

import com.jorg.game.Constants;
import java.io.Serializable;

public class Attributes implements Serializable {

    /*
     * health
     */
    private static final long serialVersionUID = 8407545876304436455L;
    protected int health = 0; // current
    protected int maxHealth = 0; // max
    /* 
     * conventional attributes
     */
    protected int intelligence = 0;
    protected int wisdom = 0; // monster: track player
    protected int agility = 0;
    protected int strength = 0;
    /*
     * experience
     */
    protected int experience = 0; // current
    protected int yieldsExperience; // when killed, gives this much exp
    protected int level = 1; // current level
    /*
     * vision
     */
    protected int lightRadius = 5;
    /*
     * travel
     */
    protected double speed = 1.0; // speed multiplier
    protected boolean swim = false; // when moving, can it swim?
    protected boolean walk = true; // when moving, can it walk?

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int light) {
        this.lightRadius = light;
    }

    public int getExp() {
        return experience;
    }

    public int getYExp() {
        return yieldsExperience;
    }

    public void setYExp(int xp) {
        this.yieldsExperience = xp;
    }

    public boolean addExperience(int exp) {
        this.experience += exp;
        if (this.experience >= getExpLevel(level)) {
            level++;
            return true;
        }
        return false;
    }

    public int getLevel() {
        return level;
    }

    public int getExpLevel(int lvl) {
        return (((lvl * (lvl - 1)) / 2) + (lvl)) * 1000;
    }

    public int getLevelPercent() {
        double l = getExp() - getExpLevel(level - 1);
        double m = getExpLevel(level) - getExpLevel(level - 1);
        double p = (l / m) * 100;
        return (int) p;
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int health) {
        this.health += health;
        if (this.health > this.maxHealth) this.health = this.maxHealth;
    }

    public void removeHealth(int health) {
        this.health -= health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > maxHealth) this.maxHealth = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getRange() {
        // range in pixels is the light radius * blocksize. 
        return lightRadius * Constants.BLOCKSIZE;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean canSwim() {
        return swim;
    }

    public void setSwim(boolean swim) {
        this.swim = swim;
    }

    public boolean canWalk() {
        return walk;
    }

    public void setWalk(boolean walk) {
        this.walk = walk;
    }

    public int getBonus(int i) {
        return (int) (i - 10) / 2;
    }
}
