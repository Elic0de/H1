package com.github.elic0de.h1.utils;

import com.github.elic0de.h1.H1Plugin;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class MessageUtil {
    // & to paragraph symbol
    public String format(String string) {
        string = string.replace("%prefix%", H1Plugin.INSTANCE.getConfigManager().getConfig().getStringElse("prefix", "&bH1 &8»"));
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
