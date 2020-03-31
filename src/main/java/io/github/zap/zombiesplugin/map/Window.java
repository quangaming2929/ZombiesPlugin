package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;
import org.bukkit.Material;

public class Window {
    private boolean isBreaking;
    private final Location bound1;
    private final Location bound2;
    private final int width;
    private final int area;

    private int breakCount = 0;
    private int lastBreakX = 0;
    private int lastBreakY = 0;

    private boolean northSouthFacing;

    private Material coverMaterial;

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
        }
        else {
            width = bound2.getBlockZ() - bound1.getBlockZ() + 1;
        }

        area = width * (bound2.getBlockY() - bound1.getBlockY() + 1);
        this.coverMaterial = coverMaterial;
    }

    public boolean isInBound(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        System.out.println("		    Running bounds check on window. Our values: " + x + ", " + y + ", " + z);
        System.out.println("		    Bound 1: " + bound1.toVector().toString());
        System.out.println("		    Bound 2: " + bound2.toVector().toString());

        return x >= bound1.getBlockX() &&
                y >= bound1.getBlockY() &&
                z >= bound1.getBlockZ() &&

                x <= bound2.getBlockX() &&
                y <= bound2.getBlockY() &&
                z <= bound2.getBlockZ();
    }

    public void breakWindow() {
        if(breakCount <= area) {
            if(northSouthFacing) {
                Location offset = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
                bound1.getWorld().getBlockAt(offset).setType(Material.AIR);
            }
            else {
                Location offset = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
                bound1.getWorld().getBlockAt(offset).setType(Material.AIR);
            }

            //TODO: sounds

            breakCount++;
            lastBreakX++;
            if(lastBreakX >= width) {
                lastBreakY++;
                lastBreakX = 0;
            }
        }
    }

    public void repairWindow() {
        if(breakCount > 0) {
            breakCount--;
            lastBreakX--;
            if(lastBreakX < width) {
                lastBreakY--;
                lastBreakX = width - 1;
            }

            if(northSouthFacing) {
                Location offset = new Location(bound1.getWorld(), bound1.getBlockX() + lastBreakX, bound1.getBlockY() + lastBreakY, bound1.getBlockZ());
                bound1.getWorld().getBlockAt(offset).setType(coverMaterial);
            }
            else {
                Location offset = new Location(bound1.getWorld(), bound1.getBlockX(), bound1.getBlockY() + lastBreakY, bound1.getBlockZ() + lastBreakX);
                bound1.getWorld().getBlockAt(offset).setType(coverMaterial);
            }
            //TODO: sounds
        }
    }

    public void setBreaking(boolean value) { isBreaking = value; }

    public boolean getBreaking() { return isBreaking; }
}