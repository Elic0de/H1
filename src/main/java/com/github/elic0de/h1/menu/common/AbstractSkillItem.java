package com.github.elic0de.h1.menu.common;

import com.archyx.slate.item.provider.PlaceholderType;
import com.archyx.slate.item.provider.TemplateItemProvider;
import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
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
        H1Player playerData = plugin.getPlayerDataManager().getPlayer(player);
        if (playerData == null) return placeholder;

        switch (placeholder) {
            case "skill":
                return skill.getSkillName();
            case "skill_desc":
                return skill.getSkillDec();
            case "point":
                return String.valueOf(skill.getPoint());
            case "mana":
                return String.valueOf(skill.getMana());
            case "skill_click":
                return "クリックして選択";
        }
        return placeholder;
    }
}
