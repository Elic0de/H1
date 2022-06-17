package com.github.elic0de.h1.support;

import com.github.elic0de.h1.utils.LogUtil;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.TNEAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TheNewEconomy {

    private static TNEAPI economy = TNE.instance().api();

    public static boolean isEnableTheNewEconomy = false;

    public void start() {
        LogUtil.info("Checking TheNewEconomy Compatibility...");

        try {
            Plugin viaBackwards = Bukkit.getPluginManager().getPlugin("TheNewEconomy");
            if (viaBackwards != null) isEnableTheNewEconomy = true;
        } catch (Exception ignored) {}
    }

    public static TNEAccount getEconomy(Player player) {
        return economy.getAccount(player.getUniqueId());
    }
}
