package com.github.elic0de.h1.skill.skills;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.SkillData;
import com.github.elic0de.h1.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkillData(name = "oshimaki", displayName = "押し寿司", desc = "弓矢で打った6×6の範囲が破壊される", mana = 60, point = 100)
public class Oshimaki extends Skill {

    private static final Set<Material> IGNORED_BLOCKS = EnumSet.of(
            Material.BEDROCK,
            Material.LAVA,
            Material.WATER,
            Material.AIR
    );

    @Override
    public void execute(final ProjectileHitEvent event, Player player) {
        H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);

        if (!h1Player.hasMana(mana)) return;

        if (player.getGameMode() == GameMode.CREATIVE) return;

        List<Block> area = Utils.getBlocks(event.getHitBlock(), 2);
        if (area.size() <= 1) {
            return;
        }
        for (Block b : area) {
            if (!IGNORED_BLOCKS.contains(b.getType())) {
                b.breakNaturally();
            }
        }
        h1Player.giveBreakBlocks(36);
        h1Player.useMana(mana);
    }
}
