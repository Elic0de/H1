package com.github.elic0de.h1.skill.skills;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.SkillData;
import com.github.elic0de.h1.utils.LogUtil;
import com.github.elic0de.h1.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkillData(name = "ehoumaki", displayName = "恵方巻",desc = "3×3の範囲を同時に掘れる", mana = 3, point = 3)
public class Ehoumaki extends Skill {

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

        if (!h1Player.hasMana(mana)) return;

        if (player.getGameMode() == GameMode.CREATIVE) return;

        List<Block> area = Utils.getBlocks(event.getBlock(), 1);
        if (area.size() <= 1) {
            return;
        }
        for (Block b : area) {
            if (!IGNORED_BLOCKS.contains(b.getType())) {
                b.breakNaturally();
            }
        }
        h1Player.giveBreakBlocks(9);
        h1Player.useMana(mana);
    }
}
