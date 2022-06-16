package com.github.elic0de.h1.skill;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import github.scarsz.configuralize.DynamicConfig;
import lombok.Getter;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

@Getter
public class Skill<T> {
    public double violations;
    public int mana;
    public int point;

    private String skillName;
    private String skillDec;
    private String configName;

    private static final Set<Material> IGNORED_BLOCKS = EnumSet.of(
            Material.BEDROCK,
            Material.LAVA,
            Material.WATER,
            Material.AIR
    );

    public Skill() {
        final Class<?> skillClass = this.getClass();

        if (skillClass.isAnnotationPresent(SkillData.class)) {
            final SkillData skillData = skillClass.getAnnotation(SkillData.class);
            this.skillName = skillData.name();
            this.skillDec = skillData.desc();
            this.configName = skillData.configName();
            // Fall back to skill name
            if (this.configName.equals("DEFAULT")) this.configName = this.skillName;
            this.mana = skillData.mana();
            this.point = skillData.point();
        }

        reload();
    }

    public void reload() {
        mana = getConfig().getIntElse(configName + ".mana", mana);
        point = getConfig().getIntElse(configName + ".point", point);
    }

    public DynamicConfig getConfig() {
        return H1Plugin.INSTANCE.getConfigManager().getConfig();
    }

    public String formatOffset(double offset) {
        return offset > 0.001 ? String.format("%.5f", offset) : String.format("%.2E", offset);
    }
}
