package com.github.elic0de.h1.player;

import com.github.elic0de.h1.skill.Skill;

public class H1Player {

    private int level;
    private int maxMana;

    private int breakBlocks;

    private Skill skill;

    private int point;

    public H1Player() {
        this.level = 0;
        this.maxMana = 200;
        this.breakBlocks = 0;
        this.point = 0;
    }

    public void levelUP() {
        level = level + 1;
        point = point + 5;
        maxMana = maxMana + 100;
        //resetMana();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void giveBreakBlocks(int blocks) {
        breakBlocks = breakBlocks + blocks;
    }

    public int getBreakBlocks() {
        return breakBlocks;
    }

    public void setBreakBlocks(int breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public boolean hasSkill() {
        return skill != null;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public boolean hasPoint(int point) {
        return point > this.point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
