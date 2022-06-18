package com.github.elic0de.h1.tasks;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class MineAnimationTask extends BukkitRunnable {

    private final Block block;
    private final Material replace;
    private final Material filter;
    private final Location loc;

    private int radius;
    private int pitch = 17;

    public MineAnimationTask(Block block, int radius, Material replace, Material filter) {
        this.block = block;
        this.radius = radius;
        this.replace = replace;
        this.filter = filter;
        this.loc = block.getLocation();
    }

    @Override
    public void run() {
        if (radius > 4) {
            cancel();
            getParticles(loc, replace,5).forEach(loc -> loc.getBlock().setType(Material.AIR));
            loc.getWorld().playSound(loc, Sound.ENTITY_SPLASH_POTION_BREAK, 1, 1);
            return;
        }
        getParticles(loc, filter, radius).forEach(loc -> loc.getBlock().setType(replace));
        loc.getWorld().playSound(loc, Sound.BLOCK_WOOD_BREAK, 1.0F, pitch/10F);
        radius++;
        pitch++;
    }

    public List<Location> getParticles(Location loc, Material material, int radius) {
        List<Location> result = new ArrayList<>();
        Block start = loc.getWorld().getBlockAt(loc);
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        blocks.stream().filter(b -> b.getType().equals(material)).forEach(b -> result.add(b.getLocation()));
        return result;
    }

}
