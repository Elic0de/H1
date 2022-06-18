package com.github.elic0de.h1.ui;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.utils.MessageUtil;
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
import java.util.HashMap;
import java.util.Map;

public class ManaBossBar implements Listener {

    private final Map<Player, Map<Player, BossBar>> bossBars;
    private final Map<Player, Map<Player, Integer>> currentActions;
    private final Map<Player, Map<Player, Integer>> checkCurrentActions;
    private final Map<Player, BossBar> singleBossBars;
    private final Map<Player, Integer> singleCurrentActions;
    private final Map<Player, Integer> singleCheckCurrentActions;
    private String mode;
    private int stayTime;
    private Map<String, BarColor> colors;
    private Map<String, BarStyle> styles;
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
        mode = getConfig().getStringElse("boss-bar.mode", "multi");
        stayTime = getConfig().getIntElse("boss-bar.stay-time", 60);
        colors = new HashMap<>();
        styles = new HashMap<>();


        colors.put("default", BarColor.valueOf("PURPLE"));
        styles.put("default", BarStyle.valueOf("SOLID"));
        for (Map.Entry<Player, BossBar> entry : singleBossBars.entrySet()) {
            entry.getValue().setVisible(false);
            entry.getValue().removeAll();
        }
        for (Map.Entry<Player, Map<Player, BossBar>> entry : bossBars.entrySet()) {
            Map<Player, BossBar> bossBars = entry.getValue();
            for (Map.Entry<Player, BossBar> bossBarEntry : bossBars.entrySet()) {
                bossBarEntry.getValue().setVisible(false);
                bossBarEntry.getValue().removeAll();
            }
        }
        bossBars.clear();
        singleBossBars.clear();
    }

    public void sendBossBar(Player player, double currentMana, double maxMana) {
        if (!getConfig().getBooleanElse("boss-bar.display-maxed", true)) { // display-maxed option
            return;
        }
        BarColor color = getColor("default");
        BarStyle style = getStyle("default");
        BossBar bossBar;
        // Single Mode
        if (mode.equals("single")) {
            bossBar = singleBossBars.get(player);
        }
        else {
            if (!bossBars.containsKey(player)) bossBars.put(player, new HashMap<>());
            bossBar = bossBars.get(player).get(player);
        }
        // If player does not have a boss bar in that skill
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("bossbar.context", "&6マナ &7({mana}/{max_mana})")),
                    "{mana}", String.valueOf((int) currentMana),
                    "{max_mana}", String.valueOf((int) maxMana)), color, style);
            double progress = currentMana / maxMana;
            if (progress <= 1 && progress >= 0) {
                bossBar.setProgress(currentMana / maxMana);
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
                bossBars.get(player).put(player, bossBar);
            }
        }
        // Use existing one
        else {
            bossBar = Bukkit.createBossBar(TextUtil.replace(MessageUtil.format(getConfig().getStringElse("bossbar.context", "&6マナ &7({mana}/{max_mana})")),
                    "{mana}", String.valueOf((int) currentMana),
                    "{max_mana}", String.valueOf((int) maxMana)), color, style);
            double progress = currentMana / maxMana;
            if (progress <= 1 && progress >= 0) {
                bossBar.setProgress(currentMana / maxMana);
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
            Integer currentAction = currentActions.get(player).get(player);
            if (currentAction != null) {
                currentActions.get(player).put(player, currentAction + 1);
            } else {
                currentActions.get(player).put(player, 0);
            }
        }
        scheduleHide(player, bossBar);
    }

    public void incrementAction(Player player) {
        if (!singleCheckCurrentActions.containsKey(player)) singleCheckCurrentActions.put(player, 0);
        if (!checkCurrentActions.containsKey(player)) checkCurrentActions.put(player, new HashMap<>());
        // Increment current action
        if (mode.equals("single")) {
            singleCheckCurrentActions.put(player, singleCheckCurrentActions.get(player) + 1);
        }
        else {
            Integer currentAction = checkCurrentActions.get(player).get(player);
            if (currentAction != null) {
                checkCurrentActions.get(player).put(player, currentAction + 1);
            } else {
                checkCurrentActions.get(player).put(player, 0);
            }
        }
    }

    private void scheduleHide(Player player, BossBar bossBar) {
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
            Map<Player, Integer> multiCurrentActions = currentActions.get(player);
            if (multiCurrentActions != null) {
                final int currentAction = multiCurrentActions.get(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!mode.equals("single")) {
                            Map<Player, Integer> multiCurrentActions = currentActions.get(player);
                            if (multiCurrentActions != null) {
                                if (currentAction == multiCurrentActions.get(player)) {
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

    private BarColor getColor(String skill) {
        BarColor color = colors.get(skill);
        if (color == null) color = BarColor.GREEN;
        return color;
    }

    private BarStyle getStyle(String skill) {
        BarStyle style = styles.get(skill);
        if (style == null) style = BarStyle.SOLID;
        return style;
    }

    public int getCurrentAction(Player player) {
        if (mode.equals("single")) {
            return singleCheckCurrentActions.get(player);
        }
        else {
            Map<Player, Integer> multiCurrentActions = checkCurrentActions.get(player);
            if (multiCurrentActions != null) {
                return multiCurrentActions.get(player);
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
