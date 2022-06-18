package com.github.elic0de.h1.manager;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaManager {

    private final H1Plugin plugin = H1Plugin.INSTANCE;

    /**
     * Start regenerating Mana
     */
    public void startRegen() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    H1Player h1Player = plugin.getPlayerDataManager().getPlayer(player);
                    if (h1Player != null) {
                        int originalMana = h1Player.getMana();
                        int maxMana = h1Player.getMaxMana();
                        if (originalMana < maxMana) {
                            int regen = 1;
                            h1Player.setMana(Math.min(maxMana, originalMana + regen));
                            H1Plugin.INSTANCE.getBossBar().sendBossBar(player, h1Player.getSkill(), h1Player.getMana(), h1Player.getMaxMana(), h1Player.getMana(), false);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin.getPlugin(), 0L, 10 * 20L);
    }

}
