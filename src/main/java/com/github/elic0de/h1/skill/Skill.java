package com.github.elic0de.h1.skill;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.utils.enums.SkillType;
import github.scarsz.configuralize.DynamicConfig;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

@Getter
public class Skill<T> {
    public double violations;
    public int mana;
    public int point;

    private String skillName;
    private String skillDec;
    private String configName;

    public Skill() {
        final Class<?> skillClass = this.getClass();

        if (skillClass.isAnnotationPresent(SkillData.class)) {
            final SkillData skillData = skillClass.getAnnotation(SkillData.class);
            this.skillName = skillData.name();
            this.skillDec = skillData.desc();
            this.configName = skillData.configName();
            // Fall back to skill name
            if (this.configName.equals("DEFAULT")) this.configName = skillName;
            this.mana = skillData.mana();
            this.point = skillData.point();
        }

        reload();
    }

    public void execute(BlockBreakEvent event) {
    }

    public void reload() {
        mana = getConfig().getIntElse(configName + ".mana", mana);
        point = getConfig().getIntElse(configName + ".point", point);
    }

    public void sendBossBar(Player player, Skill skill) {
        final H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);

        if (getConfig().getBooleanElse("BOSS_BAR_ENABLED", true)) {
            // Check whether boss bar should update
            H1Plugin.INSTANCE.getBossBar().incrementAction(player, skill);
            int currentAction = H1Plugin.INSTANCE.getBossBar().getCurrentAction(player, skill);
            if (currentAction != -1 && currentAction % getConfig().getIntElse("BOSS_BAR_UPDATE_EVERY", 20) == 0) {
                int mana = h1Player.getMana();
                boolean notMaxed = false;
                if (notMaxed) {
                    //plugin.getBossBar().sendBossBar(player, skill, mana, h1Player.getMaxMana(), mana, false);
                } else {
                    H1Plugin.INSTANCE.getBossBar().sendBossBar(player, skill, h1Player.getMana(), h1Player.getMaxMana(), mana, false);
                }
            }
        }
    }

    public DynamicConfig getConfig() {
        return H1Plugin.INSTANCE.getConfigManager().getConfig();
    }

    public String formatOffset(double offset) {
        return offset > 0.001 ? String.format("%.5f", offset) : String.format("%.2E", offset);
    }
}
