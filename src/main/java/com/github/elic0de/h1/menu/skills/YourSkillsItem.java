package com.github.elic0de.h1.menu.skills;

import com.archyx.slate.item.provider.PlaceholderType;
import com.archyx.slate.item.provider.SingleItemProvider;
import com.archyx.slate.menu.ActiveMenu;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.common.AbstractItem;
import com.github.elic0de.h1.utils.text.TextUtil;
import org.bukkit.entity.Player;

public class YourSkillsItem extends AbstractItem implements SingleItemProvider {

    public YourSkillsItem(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public String onPlaceholderReplace(String placeholder, Player player, ActiveMenu activeMenu, PlaceholderType type) {
        switch (placeholder) {
            case "your_skills":
                return TextUtil.replace("",
                        "{player}", player.getName());
            case "desc":
                return "説明";
            case "hover":
                return "かざす";
            case "click":
                return "クリック";
        }
        return placeholder;
    }
}
