package com.github.elic0de.h1.menu.skills;

import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.common.AbstractSkillItem;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.utils.enums.SkillType;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ClickableSkillItem extends AbstractSkillItem {

    public ClickableSkillItem(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, ItemStack item, SlotPos pos, ActiveMenu activeMenu, Skill skill) {
        final H1Player playerData = plugin.getPlayerDataManager().getPlayer(player);
        if (playerData == null) {
            return;
        }

        if (playerData.getPlayerData().hasSkill(skill.getSkillName())) {
            // 選択処理
            playerData.setSkill(SkillType.valueOf(skill.getSkillName().toUpperCase(Locale.ROOT)));
        } else {
            // 購入処理
            if (playerData.canBuy(skill)) {
                playerData.setPoint(Math.max(0, playerData.getPoint() - skill.getPoint()));
                playerData.getPlayerData().addSkill(skill.getSkillName());
            }
        }
        activeMenu.reload();
    }

    @Override
    public Set<Skill> getDefinedContexts(Player player, ActiveMenu activeMenu) {
        return new HashSet<>(plugin.getSkillRegistry().getSkills());
    }
}
