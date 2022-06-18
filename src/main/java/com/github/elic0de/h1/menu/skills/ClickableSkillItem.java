package com.github.elic0de.h1.menu.skills;

import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.common.AbstractSkillItem;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ClickableSkillItem extends AbstractSkillItem {

    public ClickableSkillItem(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, ItemStack item, SlotPos pos, ActiveMenu activeMenu, Skill skill) {
        H1Player playerData = plugin.getPlayerDataManager().getPlayer(player);
        if (playerData == null) {
            return;
        }
        // ここに選択処理
    }

    @Override
    public Set<Skill> getDefinedContexts(Player player, ActiveMenu activeMenu) {
        return new HashSet<>(H1Plugin.INSTANCE.getSkillRegistry().getSkills());
    }

    @Override
    public ItemStack onItemModify(ItemStack baseItem, Player player, ActiveMenu activeMenu, Skill skill) {
        return baseItem;
    }
}
