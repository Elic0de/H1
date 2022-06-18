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

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkillData(name = "押し寿司", desc = "10×10の範囲の溶岩をマグマブロックに変更して、マグマブロックの部分を一括破壊出来る", mana = 30, point = 60)
public class Temakizushi extends Skill {

    private static final Set<Material> IGNORED_BLOCKS = EnumSet.of(
            Material.BEDROCK,
            Material.LAVA,
            Material.WATER,
            Material.AIR
    );

    @Override
    public void execute(final BlockBreakEvent event) {
        Player player = event.getPlayer();
        H1Player h1Player = H1Plugin.INSTANCE.getPlayerDataManager().getPlayer(player);

        if (h1Player.hasMana(mana)) return;

        if (player.getGameMode() == GameMode.CREATIVE) return;
        int blocksUp = 2;
        int blocksOut = 1;
        int blocksAcross = 0;

        List<Block> area = Utils.getBlocks(event, blocksUp, blocksOut, blocksAcross);
        if (area.size() <= 1) {
            return;
        }

        for (Block b : area) {
            if (!IGNORED_BLOCKS.contains(b.getType())) {
                b.breakNaturally();
            }
        }
        h1Player.useMana(mana);
    }
}
