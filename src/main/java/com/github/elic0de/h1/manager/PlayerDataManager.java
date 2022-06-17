package com.github.elic0de.h1.manager;

import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.utils.PlayerConverter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final ConcurrentHashMap<String, H1Player> playerDataMap = new ConcurrentHashMap<>();

    @Nullable
    public H1Player getPlayer(final Player player) {
        final String playerID = PlayerConverter.getID(player);
        return playerDataMap.get(playerID);
    }

    public void addPlayer(final Player player, H1Player h1Player) {
        final String playerID = PlayerConverter.getID(player);
        playerDataMap.put(playerID, h1Player);
    }

    public void remove(final Player player) {
        final String playerID = PlayerConverter.getID(player);
        playerDataMap.remove(playerID);
    }

    public Collection<H1Player> getEntries() {
        return playerDataMap.values();
    }

    public int size() {
        return playerDataMap.size();
    }
}
