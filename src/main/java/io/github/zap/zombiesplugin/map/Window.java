package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;
import io.github.zap.zombiesplugin.pathfind.PathfinderGoalEscapeWindow;
import org.bukkit.*;
import org.bukkit.block.Block;

public class Window {
    private SpawnPoint spawnPoint;
    private MultiBoundingBox interiorBounds;
    private BoundingBox windowBounds;

    private int brokenBlocks = 0;
    private int lastBreakX = 0;
    private int lastBreakY = 0;

    private int windowWidth;

    private boolean northSouthFacing;
    private Material coverMaterial;

    private PathfinderGoalEscapeWindow targetingAI;

    public Window(BoundingBox windowBounds, MultiBoundingBox interiorBounds, SpawnPoint spawnPoint, Material coverMaterial) {
        this.spawnPoint = spawnPoint;
        this.interiorBounds = interiorBounds;
        this.windowBounds = windowBounds;

        northSouthFacing = windowBounds.getBound1().getBlockZ() == windowBounds.getBound2().getBlockZ();
        if(northSouthFacing) windowWidth = windowBounds.getWidth();
        else windowWidth = windowBounds.getDepth();

        this.coverMaterial = coverMaterial;
    }

    public void breakWindow(PathfinderGoalEscapeWindow targettingAI) {
        if(brokenBlocks < windowBounds.getVolume()) {
            Block targetBlock;
            if(northSouthFacing) {
                targetBlock = windowBounds.getBlockRelative(lastBreakX, lastBreakY, 0);
            }
            else {
                targetBlock = windowBounds.getBlockRelative(0, lastBreakY, lastBreakX);
            }

            this.targetingAI = targettingAI;
            targetBlock.setType(Material.AIR);
            windowBounds.getWorld().playSound(windowBounds.getCenter(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 10, 1);

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

            Block targetBlock;
            if(northSouthFacing) {
                targetBlock = windowBounds.getBlockRelative(lastBreakX, lastBreakY, 0);
            }
            else {
                targetBlock = windowBounds.getBlockRelative(0, lastBreakY, lastBreakX);
            }

            targetBlock.setType(coverMaterial);

            if(brokenBlocks == 0) {
                windowBounds.getWorld().playSound(windowBounds.getCenter(), Sound.BLOCK_ANVIL_PLACE, 10, 1);
            }
            else {
                windowBounds.getWorld().playSound(windowBounds.getCenter(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 10, 1);
            }
        }
    }

    public boolean isFullyRepaired() {return brokenBlocks == 0;}

    public boolean isReparable() {
        return !isFullyRepaired() && (targetingAI == null || targetingAI.reachedGoal());
    }

    public MultiBoundingBox getInteriorBounds() { return interiorBounds; }

    public BoundingBox getWindowBounds() { return  windowBounds; }

    public SpawnPoint getSpawnPoint() { return spawnPoint; }
}