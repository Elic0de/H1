package com.github.elic0de.h1.manager;

import com.github.elic0de.h1.H1;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.LogUtil;
import github.scarsz.configuralize.DynamicConfig;
import github.scarsz.configuralize.Language;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {
    @Getter
    private final DynamicConfig config;
    @Getter
    private final File configFile = new File(H1Plugin.INSTANCE.getPlugin().getDataFolder(), "config.yml");
    @Getter
    private final File menuFile = new File(H1Plugin.INSTANCE.getPlugin().getDataFolder(), "skills.yml");
    @Getter
    private final File messagesFile = new File(H1Plugin.INSTANCE.getPlugin().getDataFolder(), "messages.yml");

    public ConfigManager() {
        upgrade();

        // load config
        H1Plugin.INSTANCE.getPlugin().getDataFolder().mkdirs();
        config = new DynamicConfig();
        config.addSource(H1.class, "config", getConfigFile());
        config.addSource(H1.class, "menus", getMenuFile());
        config.addSource(H1.class, "messages", getMessagesFile());

        reload();
    }

    public void reload() {
        String languageCode = System.getProperty("user.language").toUpperCase();

        try {
            config.setLanguage(Language.valueOf(languageCode));
        } catch (IllegalArgumentException ignored) { // not a valid language code
        }

        // Logic for system language
        if (!config.isLanguageAvailable(config.getLanguage())) {
            String lang = languageCode.toUpperCase();
            LogUtil.info("Unknown user language " + lang + ".");
            LogUtil.info("If you fluently speak " + lang + " as well as English, see the GitHub repo to translate it!");
            config.setLanguage(Language.JA);
        }

        try {
            config.saveAllDefaults(false);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save default config files", e);
        }

        try {
            config.loadAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    private void upgrade() {
        File config = new File(H1Plugin.INSTANCE.getPlugin().getDataFolder(), "config.yml");
        if (config.exists()) {
            try {
                String configString = new String(Files.readAllBytes(config.toPath()));

                int configVersion = configString.indexOf("config-version: ");

                if (configVersion != -1) {
                    String configStringVersion = configString.substring(configVersion + "config-version: ".length());
                    configStringVersion = configStringVersion.substring(0, !configStringVersion.contains("\n") ? configStringVersion.length() : configStringVersion.indexOf("\n"));
                    configStringVersion = configStringVersion.replaceAll("\\D", "");

                    // TODO: Do we have to hardcode this?
                    configString = configString.replaceAll("config-version: " + configStringVersion, "config-version: 3");
                    Files.write(config.toPath(), configString.getBytes());
                } else {
                    removeLegacyTwoPointOne(config);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeLegacyTwoPointOne(File config) throws IOException {
        // If config doesn't have config-version, it's a legacy config
        Files.move(config.toPath(), new File(H1Plugin.INSTANCE.getPlugin().getDataFolder(), "config-2.1.old.yml").toPath());
    }

    public void loadMenus() {
        int menusLoaded = 0;
        try {
            H1Plugin.INSTANCE.getSlate().getMenuManager().loadMenu(getMenuFile());
            menusLoaded++;
        } catch (Exception e) {
            LogUtil.warn("Error loading menu " + "skills");
            e.printStackTrace();
        }
        LogUtil.info("Loaded " + menusLoaded + " menus");
    }
}
