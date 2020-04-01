package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.player.User;
import net.minecraft.server.v1_15_R1.BlockBarrier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Window {
    private boolean isBreaking;
    private final Location bound1;
    private final Location bound2;
    private final int width;
    private final int area;

    private int brokenBlocks = 0;
    private int lastBreakX = 0;
    private int lastBreakY = 0;

    private boolean northSouthFacing;
    private Material coverMaterial;
    private Location origin;
    private boolean repairedFlag;

    public Window(Location bound1, Location bound2, Material coverMaterial) {
        int xMin = Math.min(bound1.getBlockX(), bound2.getBlockX());
        int yMin = Math.min(bound1.getBlockY(), bound2.getBlockY());
        int zMin = Math.min(bound1.getBlockZ(), bound2.getBlockZ());

        int xMax = Math.max(bound1.getBlockX(), bound2.getBlockX());
        int yMax = Math.max(bound1.getBlockY(), bound2.getBlockY());
        int zMax = Math.max(bound1.getBlockZ(), bound2.getBlockZ());

        this.bound1 = new Location(bound1.getWorld(), xMin, yMin, zMin);
        this.bound2 = new Location(bound2.getWorld(), xMax, yMax, zMax);

        northSouthFacing = bound1.getBlockZ() == bound2.getBlockZ();
        if(northSouthFacing) {
            width = bound2.getBlockX() - bound1.getBlockX() + 1;

            double average = (bound1.getX() + bound2.getX()) / 2;
            origin = new Location(bound1.getWorld(), average, bound1.getBlockY(), bound1.getBlockZ());
        }
        else {
            width = bound2.getBlockZ() - bound1.getBlockZ() + 1;

            double average = (bound1.getZ() + bound2.getZ()) / 2;
            origin = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY(), average);
        }

        area = width * (bound2.getBlockY() - bound1.getBlockY() + 1);
        this.coverMaterial = coverMaterial;
    }

    public boolean isInBound(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= bound1.getBlockX() &&
                y >= bound1.getBlockY() &&
                z >= bound1.getBlockZ() &&

                x <= bound2.getBlockX() &&
                y <= bound2.getBlockY() &&
                z <= bound2.getBlockZ();
    }

    public void breakWindow(GameManager manager) {
        if(brokenBlocks < area) {
            Location targetBlock;
            if(northSouthFacing) {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
            }
            else {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
            }
            bound1.getWorld().getBlockAt(targetBlock).setType(Material.AIR);
            origin.getWorld().playSound(origin, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10, 1);

            for(User user : manager.getPlayerManager().getPlayers()) {
                user.getPlayer().sendBlockChange(targetBlock, Material.BARRIER.createBlockData());
            }

            brokenBlocks++;
            lastBreakX++;
            if(lastBreakX >= width) {
                lastBreakY++;
                lastBreakX = 0;
            }
        }
    }

    public void repairWindow(GameManager manager) {
        if(brokenBlocks > 0) {
            brokenBlocks--;
            lastBreakX--;
            if(lastBreakX < 0) {
                lastBreakY--;
                lastBreakX = width - 1;
            }

            Location targetBlock;
            if(northSouthFacing) {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
            }
            else {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
            }

            repairedFlag = true;
            bound1.getWorld().getBlockAt(targetBlock).setType(coverMaterial);

            if(brokenBlocks == 0) {
                origin.getWorld().playSound(origin, Sound.BLOCK_ANVIL_PLACE, 10, 1);
            }
            else {
                origin.getWorld().playSound(origin, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 10, 1);
            }
        }
    }

    public Location getOrigin() { return origin; }

    public void setBreaking(boolean value) { isBreaking = value; }

    public boolean getBreaking() { return isBreaking; }

    public void setJustRepaired(boolean value) { repairedFlag = value; }

    public boolean getJustRepaired() { return repairedFlag; }
}