package com.github.elic0de.h1;

import com.archyx.slate.Slate;
import com.github.elic0de.h1.database.Database;
import com.github.elic0de.h1.database.MySQL;
import com.github.elic0de.h1.database.SQLite;
import com.github.elic0de.h1.database.Saver;
import com.github.elic0de.h1.events.BreakEvent;
import com.github.elic0de.h1.events.HitEvent;
import com.github.elic0de.h1.events.InteractEvent;
import com.github.elic0de.h1.events.JoinAndQuitEvent;
import com.github.elic0de.h1.manager.ConfigManager;
import com.github.elic0de.h1.manager.ManaManager;
import com.github.elic0de.h1.manager.PlayerDataManager;
import com.github.elic0de.h1.menu.MenuRegistrar;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.SkillRegistry;
import com.github.elic0de.h1.ui.ManaBossBar;
import com.github.elic0de.h1.utils.LogUtil;
import com.github.elic0de.h1.utils.enums.SkillType;
import github.scarsz.configuralize.DynamicConfig;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public enum H1Plugin {
    INSTANCE;

    private final PlayerDataManager playerDataManager = new PlayerDataManager();

    private SkillRegistry skillRegistry;
    private ManaManager manaManager;
    private ConfigManager configManager;
    private ManaBossBar bossBar;
    private Economy economy;
    private Database database;
    private Saver saver;
    private Slate slate;
    private JavaPlugin plugin;

    private boolean vaultEnabled;
    private boolean isMySQLUsed;

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

        // Checks for Vault
        if (setupEconomy()) {
            vaultEnabled = true;
            LogUtil.info("Vault Support Enabled!");
        }

        setUpDatabase();

        bossBar = new ManaBossBar(this);
        bossBar.loadOptions();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new HitEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new InteractEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new BreakEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new JoinAndQuitEvent(), plugin);
    }

    private void registerAndLoadMenus() {
        new MenuRegistrar(this).register();
        configManager.loadMenus();
    }

    private void registerSkills() {
        Arrays.stream(SkillType.values()).skip(1).forEach(skillType -> skillRegistry.register(skillType.name(), skillType.skill));
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp =  plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private void setUpDatabase() {
        final DynamicConfig config = getConfigManager().getConfig();
        // try to connect to database
        final boolean mySQLEnabled = config.getBooleanElse("mysql.enabled", false);
        if (mySQLEnabled) {
            Bukkit.getLogger().info("Connecting to MySQL database");
            //log.debug("Connecting to MySQL database");
            this.database = new MySQL(this, config.getStringElse("mysql.host", "localhost"),
                    config.getStringElse("mysql.port", "3306"),
                    config.getStringElse("mysql.base", "h1"), config.getStringElse("mysql.user", "h1"),
                    config.getStringElse("mysql.pass", "h1"));
            if (database.getConnection() != null) {
                isMySQLUsed = true;
                Bukkit.getLogger().info("Successfully connected to MySQL database!");
            }
        }
        if (!mySQLEnabled || !isMySQLUsed) {
            this.database = new SQLite(this, "database.db");
        }

        // create tables in the database
        database.createTables(isMySQLUsed);

        // create and start the saver object, which handles correct asynchronous
        // saving to the database
        saver = new Saver();
        saver.start();
    }

    public void stop(final JavaPlugin plugin) {
        this.plugin = plugin;

        for (final H1Player h1Player : getPlayerDataManager().getEntries()) {
            h1Player.saveData();
        }

        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }

        // cancel database saver
        saver.end();
        database.closeConnection();
    }
}
