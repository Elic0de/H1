package com.github.elic0de.h1.utils.enums;

public enum SkillType {
    GUNKAN(1, 0, 0);

    public final int up;
    public final int down;
    public final int across;

    SkillType(int up, int down, int across) {
        this.up = up;
        this.down = down;
        this.across = across;
    }
}
