package com.github.elic0de.h1.player;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.skills.Ehoumaki;
import com.github.elic0de.h1.utils.enums.SkillType;
import org.bukkit.entity.Player;

public class H1Player {

    private int level;
    private int mana;
    private int maxMana;

    private int breakBlocks;

    private Skill skill;

    private int point;

    public H1Player(Player player) {
        this.level = 0;
        this.mana = 200;
        this.maxMana = 200;
        this.breakBlocks = 0;
        this.point = 0;
        this.skill = new Ehoumaki();

        H1Plugin.INSTANCE.getPlayerDataManager().addPlayer(player, this);
    }

    public void levelUP() {
        level = level + 1;
        point = point + 5;
        maxMana = maxMana + 100;
        resetMana();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean hasMana(int costMana) {
        return this.mana >= costMana;
    }

    public void resetMana() {
        this.mana = this.maxMana;
    }

    public void useMana(int costMana) {
        System.out.println(costMana + ":" + mana);
        this.mana = Math.max(0, this.mana - costMana);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
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

    public void setSkill(SkillType skillType) {
        this.skill = skillType.skill;
    }

    public boolean hasPoint(int point) {
        return point < this.point;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
