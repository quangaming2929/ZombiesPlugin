package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BoundingBox {
    private Location origin;
    private Location limit;

    private int width;
    private int height;
    private int depth;

    private int volume;

    private Location center;

    public BoundingBox(Location origin, Location limit) {
        int xMin = Math.min(origin.getBlockX(), limit.getBlockX());
        int yMin = Math.min(origin.getBlockY(), limit.getBlockY());
        int zMin = Math.min(origin.getBlockZ(), limit.getBlockZ());

        int xMax = Math.max(origin.getBlockX(), limit.getBlockX());
        int yMax = Math.max(origin.getBlockY(), limit.getBlockY());
        int zMax = Math.max(origin.getBlockZ(), limit.getBlockZ());

        this.origin = new Location(origin.getWorld(), xMin, yMin, zMin);
        this.limit = new Location(limit.getWorld(), xMax, yMax, zMax);

        width = limit.getBlockX() - origin.getBlockX() + 1;
        height = limit.getBlockY() - origin.getBlockY() + 1;
        depth =  limit.getBlockZ() - origin.getBlockZ() + 1;

        volume = width * height * depth;
        center = new Location(origin.getWorld(), (origin.getX() + limit.getX()) / 2, (origin.getY() + limit.getY()) / 2, (origin.getZ() + limit.getZ()) / 2);
    }

    public boolean isInBound(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= origin.getBlockX() &&
                y >= origin.getBlockY() &&
                z >= origin.getBlockZ() &&

                x <= limit.getBlockX() &&
                y <= limit.getBlockY() &&
                z <= limit.getBlockZ();
    }

    public Block getBlockRelative(int x, int y, int z) {
        return origin.getWorld().getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z);
    }

    public World getWorld() {
        return origin.getWorld();
    }

    public Location getOrigin() { return origin; }

    public Location getLimit() { return limit; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getDepth() { return depth; }

    public int getVolume() { return volume; }

    public Location getCenter() { return center; }
}
