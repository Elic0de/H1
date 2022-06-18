package com.github.elic0de.h1;

import com.archyx.slate.Slate;
import com.github.elic0de.h1.events.BreakEvent;
import com.github.elic0de.h1.events.InteractEvent;
import com.github.elic0de.h1.events.JoinAndQuitEvent;
import com.github.elic0de.h1.manager.ConfigManager;
import com.github.elic0de.h1.manager.ManaManager;
import com.github.elic0de.h1.manager.PlayerDataManager;
import com.github.elic0de.h1.menu.MenuRegistrar;
import com.github.elic0de.h1.skill.SkillRegistry;
import com.github.elic0de.h1.support.TheNewEconomy;
import com.github.elic0de.h1.ui.ManaBossBar;
import com.github.elic0de.h1.utils.enums.SkillType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum H1Plugin {
    INSTANCE;

    private final PlayerDataManager playerDataManager = new PlayerDataManager();

    private SkillRegistry skillRegistry;
    private ManaManager manaManager;
    private ConfigManager configManager;
    private ManaBossBar bossBar;
    private Slate slate;
    private JavaPlugin plugin;


    public void load(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager();
        this.manaManager = new ManaManager();
        this.skillRegistry = new SkillRegistry();
        this.slate = new Slate(plugin);
        this.manaManager.startRegen();

        registerSkills();
        registerAndLoadMenus();
        registerEvents();

        new TheNewEconomy().start();

        bossBar = new ManaBossBar(this);
        bossBar.loadOptions();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new BreakEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new JoinAndQuitEvent(), plugin);
    }

    private void registerAndLoadMenus() {
        new MenuRegistrar(this).register();
        configManager.loadMenus();
    }

    private void registerSkills() {
        skillRegistry.register("ehoumaki", SkillType.EHOUMAKI.skill);
    }

    public void stop(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
