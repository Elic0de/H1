package com.github.elic0de.h1.skill.skills;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.SkillData;
import com.github.elic0de.h1.tasks.MineAnimationTask;
import com.github.elic0de.h1.utils.LogUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

@SkillData(name = "gunkan", displayName = "軍艦", desc = "10×10の範囲の溶岩をマグマブロックに変更して、\n&7マグマブロックの部分を一括破壊出来る", mana = 100, point = 150)
public class Gunkan extends Skill {

    @Override
    public void execute(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);

        if (!h1Player.hasMana(mana)) return;

        if (player.getGameMode() == GameMode.CREATIVE) return;

        new MineAnimationTask(event.getBlock(), 1, Material.MAGMA_BLOCK, Material.LAVA).runTaskTimer(H1Plugin.INSTANCE.getPlugin(), 0L, 8L);

        h1Player.giveBreakBlocks(100);
        h1Player.useMana(mana);
    }
}
