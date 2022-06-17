package com.github.elic0de.h1.utils.enums;

import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.skills.Ehoumaki;
import com.github.elic0de.h1.skill.skills.Gunkan;

public enum SkillType {
    EHOUMAKI(new Ehoumaki()),
    GUNKAN(new Gunkan());

    public final Skill skill;

    SkillType(Skill skill) {
        this.skill = skill;
    }
}
