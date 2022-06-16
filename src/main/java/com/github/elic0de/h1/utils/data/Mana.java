package com.github.elic0de.h1.utils.data;

import com.github.elic0de.h1.H1Plugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

public class Mana {
    private final ConcurrentHashMap<Player, TickData> playerManaMap = new ConcurrentHashMap<>();

    public void useMana(Player player ,int mana) {
        final int delay = mana;

        if(!isHealing(player)) addCooldown(player, mana, mana);

        new BukkitRunnable() {
            @Override
            public void run() {
                TAGGED.put(playerID, false);
            }
        }.runTaskLater(H1Plugin.INSTANCE, delay * 20);
    }

    // all the same to us... having a cooldown or not having one
    public boolean isHealing(Player player) {
        return playerManaMap.containsKey(player);
    }

    // Yes, new cooldowns overwrite old ones, we don't have to check for an existing cooldown
    public void addCooldown(Player player, int cooldown, int transaction) {
        if (cooldown == 0) {
            removeCooldown(player);
            return;
        }

        playerManaMap.put(player, new TickData(cooldown, transaction));
    }

    public void removeCooldown(Player player) {
        playerManaMap.remove(player);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class TickData {
        int ticksRemaining;
        int transaction;

        public void tick() {
            ticksRemaining--;
        }
    }

}
