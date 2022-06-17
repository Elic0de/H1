package com.github.elic0de.h1;

import org.bukkit.plugin.java.JavaPlugin;

public final class H1 extends JavaPlugin {

    @Override
    public void onLoad() {
        H1Plugin.INSTANCE.load(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        H1Plugin.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        H1Plugin.INSTANCE.stop(this);
    }
}
