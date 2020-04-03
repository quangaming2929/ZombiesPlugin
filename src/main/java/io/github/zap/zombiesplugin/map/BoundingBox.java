package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class BoundingBox {
    private Location bound1;
    private Location bound2;

    private int width;
    private int height;
    private int depth;

    private int volume;

    private Location center;

    public BoundingBox(Location bound1, Location bound2) {
        int xMin = Math.min(bound1.getBlockX(), bound2.getBlockX());
        int yMin = Math.min(bound1.getBlockY(), bound2.getBlockY());
        int zMin = Math.min(bound1.getBlockZ(), bound2.getBlockZ());

        int xMax = Math.max(bound1.getBlockX(), bound2.getBlockX());
        int yMax = Math.max(bound1.getBlockY(), bound2.getBlockY());
        int zMax = Math.max(bound1.getBlockZ(), bound2.getBlockZ());

        this.bound1 = new Location(bound1.getWorld(), xMin, yMin, zMin);
        this.bound2 = new Location(bound2.getWorld(), xMax, yMax, zMax);

        width = bound2.getBlockX() - bound1.getBlockX() + 1;
        height = bound2.getBlockY() - bound1.getBlockY() + 1;
        depth =  bound2.getBlockZ() - bound1.getBlockZ() + 1;

        volume = width * height * depth;
        center = new Location(bound1.getWorld(), (bound1.getX() + bound2.getX()) / 2, (bound1.getY() + bound2.getY()) / 2, (bound1.getZ() + bound2.getZ()) / 2);
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

    public Block getBlockRelative(int x, int y, int z) {
        return bound1.getWorld().getBlockAt(bound1.getBlockX() + x, bound1.getBlockY() + y, bound1.getBlockZ() + z);
    }

    public World getWorld() {
        return bound1.getWorld();
    }

    public Location getBound1() { return bound1; }

    public Location getBound2() { return bound2; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getDepth() { return depth; }

    public int getVolume() { return volume; }

    public Location getCenter() { return center; }
}
