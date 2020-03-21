package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.memes.Direction;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class WorldUtils {
    private static final Direction[] NORTH_EAST = {Direction.NORTH, Direction.EAST};
    private static final Direction[] SOUTH_EAST = {Direction.SOUTH, Direction.EAST};
    private static final Direction[] SOUTH_WEST = {Direction.SOUTH, Direction.WEST};
    private static final Direction[] NORTH_WEST = {Direction.NORTH, Direction.WEST};

    public static int getHeadroom(World world, Vector origin, int limitInclusive) {
        Vector copy = new Vector(origin.getX(), origin.getY(), origin.getZ());

        int i = 0;
        while(world.getBlockAt(copy.getBlockX(), copy.getBlockY(), copy.getBlockZ()).isPassable() && copy.getBlockY() < 255) {
            copy.setY(copy.getY() + 1);
            i++;

            if(i == limitInclusive) return i;
        }

        return i;
    }

    public static Vector seekDown(World world, Vector origin) {
        Vector copy = new Vector(origin.getX(), origin.getY(), origin.getZ());

        while(world.getBlockAt(copy.getBlockX(), copy.getBlockY(), copy.getBlockZ()).isPassable() && copy.getBlockY() > 0) {
            copy.setY(copy.getY() - 1);
        }

        copy.setY(copy.getY() + 1);
        return copy;
    }

    public static Block getBlockAdjacent(World world, Vector origin, Direction direction, int amount) {
        Vector adjacent = MathUtils.pushVectorAlong(origin, direction, amount);
        return world.getBlockAt(adjacent.getBlockX(), adjacent.getBlockY(), adjacent.getBlockZ());
    }

    public static Block getBlockAdjacent(Block block, Direction direction, int amount) {
        Vector adjacent = MathUtils.pushVectorAlong(block.getLocation().toVector(), direction, amount);
        return block.getWorld().getBlockAt(adjacent.getBlockX(), adjacent.getBlockY(), adjacent.getBlockZ());
    }

    public static Block getBlockAt(World world, Vector vector) {
        return world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static Direction[] splitIntercardinal(Direction direction) {
        if(isIntercardinal(direction)) {
            switch(direction) {
                case NORTHEAST:
                    return NORTH_EAST;
                case SOUTHEAST:
                    return SOUTH_EAST;
                case SOUTHWEST:
                    return SOUTH_WEST;
                case NORTHWEST:
                default:
                    return NORTH_WEST;
            }
        }
        else return new Direction[] {direction};
    }

    public static Direction reverseDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case NORTHEAST:
                return Direction.SOUTHWEST;
            case EAST:
                return Direction.WEST;
            case SOUTHEAST:
                return Direction.NORTHWEST;
            case SOUTH:
                return Direction.NORTH;
            case SOUTHWEST:
                return Direction.NORTHEAST;
            case WEST:
                return Direction.EAST;
            case NORTHWEST:
                return Direction.SOUTHEAST;
            case UP:
                return Direction.DOWN;
            case DOWN:
            default:
                return Direction.UP;
        }
    }

    public static boolean isIntercardinal(Direction direction) {
        switch (direction) {
            case NORTHEAST:
            case SOUTHEAST:
            case SOUTHWEST:
            case NORTHWEST:
                return true;
            default:
                return false;
        }
    }
}
