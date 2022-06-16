package com.github.elic0de.h1;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum H1Plugin {
    INSTANCE;

    private JavaPlugin plugin;

    public void load(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void stop(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
