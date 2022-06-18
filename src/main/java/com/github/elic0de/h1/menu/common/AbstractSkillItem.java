package com.github.elic0de.h1.menu.common;

import com.archyx.slate.item.provider.PlaceholderType;
import com.archyx.slate.item.provider.TemplateItemProvider;
import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.utils.LogUtil;
import org.bukkit.entity.Player;

public abstract class AbstractSkillItem extends AbstractItem implements TemplateItemProvider<Skill> {

    public AbstractSkillItem(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public Class<Skill> getContext() {
        return Skill.class;
    }

    @Override
    public String onPlaceholderReplace(String placeholder, Player player, ActiveMenu activeMenu, PlaceholderType type, Skill skill) {
        final H1Player playerData = plugin.getPlayerDataManager().getPlayer(player);
        if (playerData == null) return placeholder;

        switch (placeholder) {
            case "skill":
                return skill.getDisplayName();
            case "skill_desc":
                return skill.getSkillDec();
            case "point":
                return String.valueOf(skill.getPoint());
            case "mana":
                return String.valueOf(skill.getMana());
            case "skill_click":
                if (playerData.getPlayerData().hasSkill(skill.getSkillName()) && playerData.getSkill() == skill) {
                    return "&a選択済み";
                } else if (playerData.getPlayerData().hasSkill(skill.getSkillName())){
                    return "&eクリックして選択";
                } else {
                    return "&c購入する";
            }

        }
        return placeholder;
    }
}
