package com.github.elic0de.h1.events;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinAndQuitEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new H1Player(event.getPlayer()); // Player takes care of adding to hashmap
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        H1Plugin.INSTANCE.getPlayerDataManager().remove(event.getPlayer());
    }
}
