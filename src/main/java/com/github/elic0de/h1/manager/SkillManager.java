package com.github.elic0de.h1.manager;

import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.skills.Ehoumaki;
import com.github.elic0de.h1.skill.type.BlockBreakSkill;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import org.bukkit.event.block.BlockBreakEvent;

public class SkillManager {

    ImmutableClassToInstanceMap<BlockBreakSkill> blockBreakSkill;

    public ClassToInstanceMap<Skill<?>> allSkills;

    public SkillManager() {
        blockBreakSkill = new ImmutableClassToInstanceMap.Builder<BlockBreakSkill>()
                .put(Ehoumaki.class, new Ehoumaki())
                .build();
    }

    public void onBreak(BlockBreakEvent event) {
        blockBreakSkill.values().forEach(skill -> skill.onBlockBreak(event));
    }
}
