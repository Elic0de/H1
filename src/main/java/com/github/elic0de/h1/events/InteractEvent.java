package com.github.elic0de.h1.events;

import com.github.elic0de.h1.H1Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack != null)
            if (stack.getType() == Material.STICK) {
                H1Plugin.INSTANCE.getSlate().getMenuManager().openMenu(event.getPlayer(), "skills");
                event.setCancelled(true);
        }
    }
}
