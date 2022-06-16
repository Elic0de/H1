package com.github.elic0de.h1.events;

import com.github.elic0de.h1.player.H1Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SkillManagerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        H1Player player = event.getPlayer();
        player.skillManager.onBlockBreak(event);
    }
}
