package io.github.zap.zombiesplugin.map;

import net.minecraft.server.v1_15_R1.Vec2F;
import org.bukkit.Location;
import org.bukkit.Material;

public class Window {
    private boolean isBreaking;
    private final Location bound1;
    private final Location bound2;
    private final int width;
    private final int height;
    private final int area;

    private int breakCount = 0;
    private int lastBreakX = 0;
    private int lastBreakY = 0;

    private boolean northSouthFacing;

    public Window(Location bound1, Location bound2) {
        int xMin = Math.min(bound1.getBlockX(), bound2.getBlockX());
        int yMin = Math.min(bound1.getBlockY(), bound2.getBlockY());
        int zMin = Math.min(bound1.getBlockZ(), bound2.getBlockZ());

        int xMax = Math.max(bound1.getBlockX(), bound2.getBlockX());
        int yMax = Math.max(bound1.getBlockY(), bound2.getBlockY());
        int zMax = Math.max(bound1.getBlockZ(), bound2.getBlockZ());

        this.bound1 = new Location(bound1.getWorld(), xMin, yMin, zMin);
        this.bound2 = new Location(bound2.getWorld(), xMax, yMax, zMax);

        width = bound2.getBlockX() - bound1.getBlockX() *
                bound2.getBlockZ() - bound1.getBlockZ();

        height = bound2.getBlockY() - bound1.getBlockY();

        area = width * height;

        northSouthFacing = bound1.getBlockZ() == bound2.getBlockZ();
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

    public void breakWindow() {
        if(northSouthFacing) {
            bound1.getWorld().getBlockAt(bound1.add(lastBreakX, lastBreakY, 0)).setType(Material.AIR);
        }
        else {
            bound1.getWorld().getBlockAt(bound1.add(0, lastBreakY, lastBreakX)).setType(Material.AIR);
        }

        //TODO: play sound

        breakCount++;
        if(breakCount > area) return;

        lastBreakX++;
        if(lastBreakX > bound2.getBlockX() || lastBreakX > bound2.getBlockZ()) {
            lastBreakY++;
            lastBreakX = 0;
        }
    }

    public void repairWindow() {

    }

    public void setBreaking(boolean value) { isBreaking = value; }

    public boolean getBreaking() { return isBreaking; }
}