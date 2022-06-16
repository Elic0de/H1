package com.github.elic0de.h1.support;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.TNEAPI;
import org.bukkit.entity.Player;

public class TheNewEconomy {

    private static TNEAPI economy = TNE.instance().api();

    public static TNEAccount getEconomy(Player player) {
        return economy.getAccount(player.getUniqueId());
    }
}
