package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BoundingBox {
    private Location origin;
    private Location limit;

    private double width;
    private double height;
    private double depth;

    private double volume;

    private Location center;

    /**
     * Creates a bounding box that takes and origin and a limit.
     * @param origin The first corner of the bounds, in world coordinates.
     * @param limit The second corner of the bounds, in world coordinates.
     */
    public BoundingBox(Location origin, Location limit) {
        if(origin.getWorld() != limit.getWorld()) {
            throw new IllegalArgumentException("origin and limit cannot belong to different worlds");
        }

        double xMin = Math.min(origin.getX(), limit.getX());
        double yMin = Math.min(origin.getY(), limit.getY());
        double zMin = Math.min(origin.getZ(), limit.getZ());

        double xMax = Math.max(origin.getX(), limit.getX()) + 1;
        double yMax = Math.max(origin.getY(), limit.getY()) + 1;
        double zMax = Math.max(origin.getZ(), limit.getZ()) + 1;

        width = xMax - xMin;
        height = yMax - yMin;
        depth =  zMax - zMin;
        volume = width * height * depth;

        center = new Location(origin.getWorld(), (xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2);

        this.origin = new Location(origin.getWorld(), xMin, yMin, zMin);
        this.limit = new Location(limit.getWorld(), xMax, yMax, zMax);
    }

    public boolean isInBound(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= origin.getX() &&
                y >= origin.getY() &&
                z >= origin.getZ() &&

                x <= limit.getX() &&
                y <= limit.getY() &&
                z <= limit.getZ();
    }

    public Block getBlockRelative(int x, int y, int z) {
        return origin.getWorld().getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z);
    }

    public World getWorld() {
        return origin.getWorld();
    }

    public Location getOrigin() { return origin; }

    public Location getLimit() { return limit; }

    public double getWidth() { return width; }

    public double getHeight() { return height; }

    public double getDepth() { return depth; }

    public double getVolume() { return volume; }

    public Location getCenter() { return center; }

    /**
     * Scales the bounding box by the specified amount.
     * @param amount
     */
    public void expand(double amount) {
        //origin.subtract(amount, amount, amount);
        //limit.add(amount, amount, amount);
    }
}