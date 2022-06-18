package com.github.elic0de.h1.events;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        H1Player h1player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(event.getPlayer());

        h1player.giveBreakBlocks(1);

        // 1000ブロック掘ればプレイヤーに500円付与する
        if (h1player.getBreakBlocks() % 1000 == 0) {
            if (H1Plugin.INSTANCE.isVaultEnabled())
                H1Plugin.INSTANCE.getEconomy().depositPlayer(h1player.getPlayer(), 500);
        }

        // 5000ブロックで1レベル上げる
        if (h1player.getBreakBlocks() % 5000 == 0) {
            h1player.levelUP();
        }

        // スキルの処理
        if (h1player.hasSkill())
            h1player.getSkill().execute(event);
    }
}
