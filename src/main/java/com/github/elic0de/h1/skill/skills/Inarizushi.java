package com.github.elic0de.h1.skill.skills;

import com.github.elic0de.h1.H1Plugin;
import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.Skill;
import com.github.elic0de.h1.skill.SkillData;
import com.github.elic0de.h1.tasks.MineAnimationTask;
import com.github.elic0de.h1.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkillData(name = "inarizushi", displayName = "いなり寿司", desc = "10×10の範囲の水を氷に変更して、\n&7氷の部分を一括破壊出来る", mana = 100, point = 150)
public class Inarizushi extends Skill {

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

        new MineAnimationTask(event.getBlock(), 1, Material.ICE, Material.WATER).runTaskTimer(H1Plugin.INSTANCE.getPlugin(), 0L, 8L);

        h1Player.giveBreakBlocks(100);
        h1Player.useMana(mana);
    }
}
