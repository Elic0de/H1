package com.github.elic0de.h1.skill.skills;

import com.github.elic0de.h1.player.H1Player;
import com.github.elic0de.h1.skill.SkillData;
import com.github.elic0de.h1.skill.type.BlockBreakSkill;
import com.github.elic0de.h1.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@SkillData(name = "海鮮丼", desc = " 2×1の縦方向を同時に掘れる", mana = 5, point = 5)
public class Ehoumaki extends BlockBreakSkill {

    private static final Set<Material> IGNORED_BLOCKS = EnumSet.of(
            Material.BEDROCK,
            Material.LAVA,
            Material.WATER,
            Material.AIR
    );

    @Override
    public void onBlockBreak(final BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        int blocksUp = 2;
        int blocksOut = 1;
        int blocksAcross = 0;

        List<Block> area = getBlocks(event, blocksUp, blocksOut, blocksAcross);
        if (area.size() <= 1) {
            return;
        }

        for (Block b : area) {
            if (!IGNORED_BLOCKS.contains(b.getType())) {
                b.breakNaturally();
            }
        }
    }

    public List<Block> getBlocks(BlockBreakEvent e, int blocksUp, int blocksOut, int blocksAcross) {
        int minedX = 0, minedY = 0, minedZ = 0;
        String direction = Utils.getDirection(e.getPlayer().getLocation().getYaw());
        String upordown = Utils.getUpOrDown(e.getPlayer().getLocation().getPitch());

        Block block = e.getBlock();
        List<Block> area = new ArrayList<>();

        if (upordown.equals("DOWN")){
            minedX = block.getX() - blocksAcross /2;
            minedY = block.getY() - blocksUp;
            minedZ = block.getZ() - blocksAcross / 2;
        }
        else if (e.getPlayer().getFacing().equals(BlockFace.EAST)) {
            minedX = block.getX();
            minedY = block.getY();
            minedZ = block.getZ() - (blocksAcross / 2);
        }
        else if (e.getPlayer().getFacing().equals(BlockFace.SOUTH)) {
            minedX = block.getX() - blocksAcross / 2;
            minedY = block.getY();
            minedZ = block.getZ();
        }
        else if (e.getPlayer().getFacing().equals(BlockFace.WEST)) {
            minedX = block.getX() - blocksOut;
            minedY = block.getY();
            minedZ = block.getZ() - blocksAcross / 2;
        }
        else if (e.getPlayer().getFacing().equals(BlockFace.NORTH)) {
            minedX = block.getX() - blocksAcross / 2;
            minedY = block.getY();
            minedZ = block.getZ() - blocksOut;
        }

        for (int x = 0; x <= blocksAcross; x++) {
            for (int y = 0; y <= blocksUp && minedY + y <= 256; y++) {
                for (int z = 0; z <= blocksOut; z++) {
                    Block b = block.getWorld().getBlockAt(minedX + x, minedY + y, minedZ + z);
                    area.add(b);
                }
            }
        }

        return area;
    }
}
