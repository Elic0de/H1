package com.github.elic0de.h1.skill;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SkillRegistry {

    public final Map<String, Skill> skills;

    public SkillRegistry() {
        this.skills = new HashMap<>();
    }

    public void register(String key, Skill skill) {
        this.skills.put(key.toLowerCase(Locale.ROOT), skill);
    }

    public Collection<Skill> getSkills() {
        return skills.values();
    }

    @Nullable
    public Skill getSkill(String key) {
        return this.skills.get(key.toLowerCase(Locale.ROOT));
    }

}
