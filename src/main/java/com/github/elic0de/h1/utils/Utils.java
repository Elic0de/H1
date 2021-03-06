package com.github.elic0de.h1.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String SOUTH = "SOUTH";
    public static String EAST = "EAST";
    public static String NORTH = "NORTH";
    public static String WEST = "WEST";
    public static String UP = "UP";
    public static String DOWN = "DOWN";
    public static String UNKNOWN = "UNKNOWN";
    public static String getDirection(float yaw) {
        String returnString = "UNKNOWN";
        if (yaw > -45 && yaw <= 45) returnString = SOUTH;
        if (yaw > 45 && yaw <= 135) returnString = WEST;
        if (yaw > -45 && yaw <= -135) returnString = EAST;
        if (yaw > 135 && yaw <= -135) returnString = NORTH;

        return returnString;
    }
    public static String getUpOrDown(float pitch) {
        String returnString = "UNKNOWN";
        if (pitch > 70) returnString = DOWN;
        if (pitch < -70) returnString = UP;


        return returnString;
    }

    public static List<Block> getBlocks(Block block, Player player, int blocksUp, int blocksOut, int blocksAcross) {
        int minedX = 0, minedY = 0, minedZ = 0;
        String direction = Utils.getDirection(player.getLocation().getYaw());
        String upordown = Utils.getUpOrDown(player.getLocation().getPitch());

        List<Block> area = new ArrayList<>();

        if (upordown.equals("DOWN")){
            minedX = block.getX() - blocksAcross /2;
            minedY = block.getY() - blocksUp;
            minedZ = block.getZ() - blocksAcross / 2;
        }
        else if (player.getFacing().equals(BlockFace.EAST)) {
            minedX = block.getX();
            minedY = block.getY();
            minedZ = block.getZ() - (blocksAcross / 2);
        }
        else if (player.getFacing().equals(BlockFace.SOUTH)) {
            minedX = block.getX() - blocksAcross / 2;
            minedY = block.getY();
            minedZ = block.getZ();
        }
        else if (player.getFacing().equals(BlockFace.WEST)) {
            minedX = block.getX() - blocksOut;
            minedY = block.getY();
            minedZ = block.getZ() - blocksAcross / 2;
        }
        else if (player.getFacing().equals(BlockFace.NORTH)) {
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

    public static List<Block> getBlocks(Block block, int radius) {
        Block start = block.getLocation().getWorld().getBlockAt(block.getLocation());
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }
}
