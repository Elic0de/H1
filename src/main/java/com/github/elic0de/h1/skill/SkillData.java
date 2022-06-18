package com.github.elic0de.h1.skill;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SkillData {
    String name() default "UNKNOWN";

    String displayName() default  "UNKNOWN";

    String desc() default "NONE";

    String configName() default "DEFAULT";

    int mana() default 0;

    int point() default 0;
}
