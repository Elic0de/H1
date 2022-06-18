package com.github.elic0de.h1.menu.skills;

import com.archyx.slate.menu.ActiveMenu;
import com.archyx.slate.menu.MenuProvider;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.AbstractMenu;
import org.bukkit.entity.Player;

public class SkillsMenu extends AbstractMenu implements MenuProvider {

    public SkillsMenu(H1Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onOpen(Player player, ActiveMenu menu) {

    }

    @Override
    public String onPlaceholderReplace(String placeholder, Player player, ActiveMenu menu) {
        if (placeholder.equals("skills_menu_title")) {
            return "スキル";
        }
        return placeholder;
    }

}
