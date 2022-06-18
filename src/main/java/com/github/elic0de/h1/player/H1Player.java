package com.github.elic0de.h1.player;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.database.PlayerData;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.utils.LogUtil;
import com.github.elic0de.h1.utils.PlayerConverter;
import com.github.elic0de.h1.utils.enums.SkillType;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class H1Player {

    private PlayerData playerData;
    private Player player;

    private int level;
    private int mana;
    private int maxMana;

    private int breakBlocks;

    private Skill skill;
    private SkillType type;

    private int point;

    public H1Player(Player player) {
        this.player = player;
        type = SkillType.NONE;
        playerData = new PlayerData(PlayerConverter.getID(player), this);
        H1Plugin.INSTANCE.getPlayerDataManager().addPlayer(player, this);
        resetMana();
    }

    public void levelUP() {
        level = level + 1;
        point = point + 5;
        maxMana = maxMana + 100;
        resetMana();
    }

    public boolean canBuy(Skill skill) {
        return point >= skill.getPoint();
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
        this.mana = Math.max(0, this.mana - costMana);
        sendBossBar(player);
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
        this.type = skillType;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void sendBossBar(Player player) {
        if (H1Plugin.INSTANCE.getConfigManager().getConfig().getBooleanElse("BOSS_BAR_ENABLED", true)) {
            // Check whether boss bar should update
            H1Plugin.INSTANCE.getBossBar().incrementAction(player);
            int currentAction = H1Plugin.INSTANCE.getBossBar().getCurrentAction(player);
            if (currentAction != -1 && currentAction % H1Plugin.INSTANCE.getConfigManager().getConfig().getIntElse("BOSS_BAR_UPDATE_EVERY", 20) == 0) {
                H1Plugin.INSTANCE.getBossBar().sendBossBar(player, mana, maxMana);
            }
        }
    }

    public void saveData() {
        playerData.saveData();
    }
}
