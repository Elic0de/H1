package com.github.elic0de.h1.utils.enums;

import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.skills.*;

public enum SkillType {
    NONE(null),
    EHOUMAKI(new Ehoumaki()),
    GUNKAN(new Gunkan()),
    INARIZUSHI(new Inarizushi()),
    KAISENDON(new Kaisendon()),
    MAKIZUSHI(new Makizushi()),
    OSHIMAKI(new Oshimaki()),
    TEMAKIZUSHI(new Temakizushi());

    public final Skill skill;

    SkillType(Skill skill) {
        this.skill = skill;
    }
}
