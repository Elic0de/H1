package com.github.elic0de.h1.events;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.utils.LogUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class HitEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow)) //Remove to check all projectiles
            return;

        Arrow arrow = (Arrow) event.getEntity();
        if(!(arrow.getShooter() instanceof Player)) //Making sure the shooter is a player
            return;

        final Player player = (Player) arrow.getShooter();
        final H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);
        if (h1Player.hasSkill())
            h1Player.getSkill().execute(event, player);
    }
}

