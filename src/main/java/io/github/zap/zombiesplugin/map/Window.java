package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.manager.GameManager;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalEscapeWindow;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class Window extends BoundingBox {
    private BoundingBox interior;

    private int brokenBlocks = 0;
    private int lastBreakX = 0;
    private int lastBreakY = 0;

    private int windowWidth;

    private boolean northSouthFacing;
    private Material coverMaterial;

    private PathfinderGoalEscapeWindow ai;
    private Location exit;

    public Window(Location bound1, Location bound2, Material coverMaterial, BoundingBox interior, Location exit) {
        super(bound1, bound2);

        northSouthFacing = bound1.getBlockZ() == bound2.getBlockZ();
        if(northSouthFacing) windowWidth = width;
        else windowWidth = depth;

        this.coverMaterial = coverMaterial;
        this.interior = interior;
        this.exit = exit;
    }

    public void breakWindow(PathfinderGoalEscapeWindow ai) {
        if(brokenBlocks < volume) {
            Location targetBlock;
            if(northSouthFacing) {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
            }
            else {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
            }

            this.ai = ai;
            bound1.getWorld().getBlockAt(targetBlock).setType(Material.AIR);
            origin.getWorld().playSound(origin, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10, 1);

            brokenBlocks++;
            lastBreakX++;
            if(lastBreakX >= windowWidth) {
                lastBreakY++;
                lastBreakX = 0;
            }
        }
    }

    public void repairWindow() {
        if(brokenBlocks > 0 && isReparable()) {
            brokenBlocks--;
            lastBreakX--;
            if(lastBreakX < 0) {
                lastBreakY--;
                lastBreakX = windowWidth - 1;
            }

            Location targetBlock;
            if(northSouthFacing) {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
            }
            else {
                targetBlock = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
            }

            bound1.getWorld().getBlockAt(targetBlock).setType(coverMaterial);

            if(brokenBlocks == 0) {
                origin.getWorld().playSound(origin, Sound.BLOCK_ANVIL_PLACE, 10, 1);
            }
            else {
                origin.getWorld().playSound(origin, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 10, 1);
            }
        }
    }

    public boolean isFullyRepaired() {return brokenBlocks == 0;}

    public boolean isReparable() {
        return !isFullyRepaired() && (ai == null || ai.reachedGoal());
    }

    public boolean locationInside(Location location) {
        return interior.isInBound(location);
    }

    public Location getExit() { return exit; }
}