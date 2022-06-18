package com.github.elic0de.h1.menu;

import com.archyx.slate.Slate;
import com.archyx.slate.context.ContextManager;
import com.archyx.slate.item.provider.ProviderManager;
import com.archyx.slate.menu.MenuManager;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.menu.common.CloseItem;
import com.github.elic0de.h1.menu.contexts.SkillContext;
import com.github.elic0de.h1.menu.skills.ClickableSkillItem;
import com.github.elic0de.h1.menu.skills.RestoreSkillItem;
import com.github.elic0de.h1.menu.skills.SkillsMenu;
import com.github.elic0de.h1.skill.Skill;

public class MenuRegistrar {

    private final H1Plugin plugin;
    private final Slate slate;

    public MenuRegistrar(H1Plugin plugin) {
        this.plugin = plugin;
        this.slate = plugin.getSlate();
    }

    public void register() {
        ContextManager contextManager = slate.getContextManager();
        // Register contexts
        contextManager.registerContext("Skill", Skill.class, new SkillContext(plugin));

        MenuManager manager = plugin.getSlate().getMenuManager();
        // Register menus
        manager.registerMenuProvider("skills", new SkillsMenu(plugin));
        // Global items
        manager.registerSingleItem("close", new CloseItem(plugin));
        // Register menu specific items and templates
        ProviderManager skills = manager.getProviderManager("skills");
        skills.registerSingleItem("restore", new RestoreSkillItem(plugin));
        skills.registerTemplateItem("skill", new ClickableSkillItem(plugin));
    }
}
