package com.github.elic0de.h1.ui;

import com.github.elic0de.h1.H1;
import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.utils.LogUtil;
import com.github.elic0de.h1.utils.MessageUtil;
import com.github.elic0de.h1.utils.enums.SkillType;
import com.github.elic0de.h1.utils.math.BigNumber;
import com.github.elic0de.h1.utils.math.RomanNumber;
import com.github.elic0de.h1.utils.text.TextUtil;
import github.scarsz.configuralize.DynamicConfig;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ManaBossBar implements Listener {

    private final Map<Player, Map<Skill, BossBar>> bossBars;
    private final Map<Player, Map<Skill, Integer>> currentActions;
    private final Map<Player, Map<Skill, Integer>> checkCurrentActions;
    private final Map<Player, BossBar> singleBossBars;
    private final Map<Player, Integer> singleCurrentActions;
    private final Map<Player, Integer> singleCheckCurrentActions;
    private String mode;
    private int stayTime;
    private Map<Skill, BarColor> colors;
    private Map<Skill, BarStyle> styles;
    private final NumberFormat nf = new DecimalFormat("#.#");
    private final H1Plugin plugin;

    public ManaBossBar(H1Plugin plugin) {
        this.bossBars = new HashMap<>();
        this.currentActions = new HashMap<>();
        this.plugin = plugin;
        this.singleBossBars = new HashMap<>();
        this.singleCurrentActions = new HashMap<>();
        this.checkCurrentActions = new HashMap<>();
        this.singleCheckCurrentActions = new HashMap<>();
    }

    public void loadOptions() {
        mode = getConfig().getStringElse("BOSS_BAR_MODE", "multi");
        stayTime = getConfig().getIntElse("BOSS_BAR_STAY_TIME", 60);
        colors = new HashMap<>();
        styles = new HashMap<>();
        for (String entry : getConfig().getStringListElse("BOSS_BAR_FORMAT", Arrays.asList("EHOUMAKI GREEN SOLID"))) {
            String[] splitEntry = entry.split(" ");
            Skill skill;
            BarColor color = BarColor.GREEN;
            BarStyle style = BarStyle.SOLID;
            skill = plugin.getSkillRegistry().getSkill(splitEntry[0].toUpperCase());
            if (skill == null) {
                LogUtil.warn("Error loading boss bar format in config.yml: " + splitEntry[0] + " is not a valid Skill");
                skill = SkillType.EHOUMAKI.skill;
            }
            if (splitEntry.length > 1) {
                try {
                    color = BarColor.valueOf(splitEntry[1].toUpperCase());
                }
                catch (IllegalArgumentException e) {
                    LogUtil.warn("Error loading boss bar format in config.yml: " + splitEntry[0] + " is not a valid BarColor");
                }
                if (splitEntry.length > 2) {
                    try {
                        style = BarStyle.valueOf(splitEntry[2].toUpperCase());
                    }
                    catch (IllegalArgumentException e) {
                        LogUtil.warn("Error loading boss bar format in config.yml: " + splitEntry[0] + " is not a valid BarStyle");
                    }
                }
            }
            colors.put(skill, color);
            styles.put(skill, style);
        }
        for (Map.Entry<Player, BossBar> entry : singleBossBars.entrySet()) {
            entry.getValue().setVisible(false);
            entry.getValue().removeAll();
        }
        for (Map.Entry<Player, Map<Skill, BossBar>> entry : bossBars.entrySet()) {
            Map<Skill, BossBar> bossBars = entry.getValue();
            for (Map.Entry<Skill, BossBar> bossBarEntry : bossBars.entrySet()) {
                bossBarEntry.getValue().setVisible(false);
                bossBarEntry.getValue().removeAll();
            }
        }
        bossBars.clear();
        singleBossBars.clear();
    }

    public void sendBossBar(Player player, Skill skill, double currentXp, double levelXp, int mana, boolean maxed) {
        if (maxed && !getConfig().getBooleanElse("BOSS_BAR_DISPLAY_MAXED", true)) { // display-maxed option
            return;
        }
        BarColor color = getColor(skill);
        BarStyle style = getStyle(skill);
        BossBar bossBar;
        // Single Mode
        if (mode.equals("single")) {
            bossBar = singleBossBars.get(player);
        }
        else {
            if (!bossBars.containsKey(player)) bossBars.put(player, new HashMap<>());
            bossBar = bossBars.get(player).get(skill);
        }
        // If player does not have a boss bar in that skill
        if (bossBar == null) {
            if (!maxed) {
                if (!getConfig().getBooleanElse("BOSS_BAR_ROUND_XP", false)) {
                    bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_xp", "&6{skill} &7({mana}/{max_mana})")),
                            "{skill}", skill.getSkillName(),
                            "{mana}", String.valueOf(currentXp),
                            "{max_mana}", String.valueOf(levelXp)), color, style);
                }
                else {
                    bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_xp", "&6{skill} &7({mana}/{max_mana})")),
                            "{skill}", skill.getSkillName(),
                            "{mana}", String.valueOf(currentXp),
                            "{max_mana}", String.valueOf(levelXp)), color, style);
                }
            }
            else {
                bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_maxed", "&6{skill} {mana} &7(MAXED)")),
                        "{skill}", skill.getSkillName(),
                        "{mana}", RomanNumber.toRoman(mana)), color, style);
            }
            double progress = currentXp / levelXp;
            if (progress <= 1 && progress >= 0) {
                bossBar.setProgress(currentXp / levelXp);
            }
            else {
                bossBar.setProgress(1.0);
            }
            bossBar.addPlayer(player);
            // Add to maps
            if (mode.equals("single")) {
                singleBossBars.put(player, bossBar);
            }
            else {
                bossBars.get(player).put(skill, bossBar);
            }
        }
        // Use existing one
        else {
            if (!maxed) {
                if (!getConfig().getBooleanElse("BOSS_BAR_ROUND_XP", false)) {
                    bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_xp", "&6{skill} &7({mana}/{max_mana})")),
                            "{skill}", skill.getSkillName(),
                            "{mana}", String.valueOf(currentXp),
                            "{max_mana}", String.valueOf(levelXp)), color, style);
                }
                else {
                    bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_xp", "&6{skill} &7({mana}/{max_mana})")),
                            "{skill}", skill.getSkillName(),
                            "{mana}", String.valueOf(currentXp),
                            "{max_mana}", String.valueOf(levelXp)), color, style);
                }
            }
            else {
                bossBar.setTitle(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("boss_bar_maxed", "&6{skill} {mana} &7(MAXED)")),
                        "{mana}", RomanNumber.toRoman(mana),
                        "{skill}", skill.getSkillName()));
            }
            double progress = currentXp / levelXp;
            if (progress <= 1 && progress >= 0) {
                bossBar.setProgress(currentXp / levelXp);
            }
            else {
                bossBar.setProgress(1.0);
            }
            bossBar.setVisible(true);
        }
        // Increment current action
        if (mode.equals("single")) {
            Integer currentAction = singleCurrentActions.get(player);
            if (currentAction != null) {
                singleCurrentActions.put(player, currentAction + 1);
            }
            else {
                singleCurrentActions.put(player, 0);
            }
        }
        else {
            if (!currentActions.containsKey(player)) currentActions.put(player, new HashMap<>());
            Integer currentAction = currentActions.get(player).get(skill);
            if (currentAction != null) {
                currentActions.get(player).put(skill, currentAction + 1);
            } else {
                currentActions.get(player).put(skill, 0);
            }
        }
        scheduleHide(player, skill, bossBar);
    }

    public void incrementAction(Player player, Skill skill) {
        if (!singleCheckCurrentActions.containsKey(player)) singleCheckCurrentActions.put(player, 0);
        if (!checkCurrentActions.containsKey(player)) checkCurrentActions.put(player, new HashMap<>());
        // Increment current action
        if (mode.equals("single")) {
            singleCheckCurrentActions.put(player, singleCheckCurrentActions.get(player) + 1);
        }
        else {
            Integer currentAction = checkCurrentActions.get(player).get(skill);
            if (currentAction != null) {
                checkCurrentActions.get(player).put(skill, currentAction + 1);
            } else {
                checkCurrentActions.get(player).put(skill, 0);
            }
        }
    }

    private void scheduleHide(Player player, Skill skill, BossBar bossBar) {
        if (mode.equals("single")) {
            final int currentAction = singleCurrentActions.get(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (mode.equals("single")) {
                        if (currentAction == singleCurrentActions.get(player)) {
                            bossBar.setVisible(false);
                            singleCheckCurrentActions.remove(player);
                        }
                    }
                }
            }.runTaskLater(plugin.getPlugin(), stayTime);
        }
        else {
            Map<Skill, Integer> multiCurrentActions = currentActions.get(player);
            if (multiCurrentActions != null) {
                final int currentAction = multiCurrentActions.get(skill);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!mode.equals("single")) {
                            Map<Skill, Integer> multiCurrentActions = currentActions.get(player);
                            if (multiCurrentActions != null) {
                                if (currentAction == multiCurrentActions.get(skill)) {
                                    bossBar.setVisible(false);
                                    checkCurrentActions.remove(player);
                                }
                            }
                        }
                    }
                }.runTaskLater(plugin.getPlugin(), stayTime);
            }
        }
    }

    private BarColor getColor(Skill skill) {
        BarColor color = colors.get(skill);
        if (color == null) color = BarColor.GREEN;
        return color;
    }

    private BarStyle getStyle(Skill skill) {
        BarStyle style = styles.get(skill);
        if (style == null) style = BarStyle.SOLID;
        return style;
    }

    public int getCurrentAction(Player player, Skill skill) {
        if (mode.equals("single")) {
            return singleCheckCurrentActions.get(player);
        }
        else {
            Map<Skill, Integer> multiCurrentActions = checkCurrentActions.get(player);
            if (multiCurrentActions != null) {
                return multiCurrentActions.get(skill);
            }
        }
        return -1;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        bossBars.remove(player);
        currentActions.remove(player);
        singleBossBars.remove(player);
        singleCurrentActions.remove(player);
        checkCurrentActions.remove(player);
        singleCheckCurrentActions.remove(player);
    }

    public DynamicConfig getConfig() {
        return H1Plugin.INSTANCE.getConfigManager().getConfig();
    }
}
