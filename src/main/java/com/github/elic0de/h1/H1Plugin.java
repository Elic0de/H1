package com.github.elic0de.h1;

import com.github.elic0de.h1.manager.ConfigManager;
import com.github.elic0de.h1.manager.ManaManager;
import com.github.elic0de.h1.manager.PlayerDataManager;
import com.github.elic0de.h1.support.TheNewEconomy;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum H1Plugin {
    INSTANCE;

    private final PlayerDataManager playerDataManager = new PlayerDataManager();

    private ManaManager manaManager;
    private ConfigManager configManager;
    private JavaPlugin plugin;

    public void load(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager();
    }

    public void start(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.manaManager = new ManaManager();

        new TheNewEconomy().start();
        manaManager.startRegen();
    }

    public void stop(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
