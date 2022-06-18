package com.github.elic0de.h1.menu.contexts;

import com.archyx.slate.context.ContextProvider;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.skill.Skill;

import javax.annotation.Nullable;

public class SkillContext implements ContextProvider<Skill> {

    private final H1Plugin plugin;

    public SkillContext(H1Plugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public Skill parse(String input) {
        return plugin.getSkillRegistry().getSkill(input);
    }
}
