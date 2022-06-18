package com.github.elic0de.h1.menu.skills;

import com.archyx.slate.item.provider.PlaceholderType;
import com.archyx.slate.item.provider.SingleItemProvider;
import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.common.AbstractItem;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.utils.enums.SkillType;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RestoreSkillItem extends AbstractItem implements SingleItemProvider {

    public RestoreSkillItem(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public String onPlaceholderReplace(String placeholder, Player player, ActiveMenu menu, PlaceholderType type) {
        return null;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, ItemStack item, SlotPos pos, ActiveMenu activeMenu) {
        H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);
        h1Player.setSkill(SkillType.NONE);
        activeMenu.reload();
    }
}
