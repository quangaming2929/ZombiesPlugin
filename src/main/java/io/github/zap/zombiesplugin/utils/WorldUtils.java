package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.navmesh.Direction;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class WorldUtils {
    public static int getHeadroom(World world, Vector origin, int limitInclusive) {
        int i = 0;
        while(world.getBlockAt(origin.getBlockX(), origin.getBlockY() + i + 1, origin.getBlockZ()).isEmpty() &&
                i < limitInclusive) {
            i++;
        }
        return i;
    }

    public static int getHeadroom(World world, Block block, int limitInclusive) {
        int i = 0;
        while(world.getBlockAt(block.getX(), block.getY() + i + 1, block.getZ()).isEmpty() && i < limitInclusive) {
            i++;
        }
        return i;
    }

    public static Vector seekDown(World world, Vector origin) {
        int i = 0;
        Vector copy = new Vector(origin.getX(), origin.getY(), origin.getZ());
        while(world.getBlockAt(copy.getBlockX(), copy.getBlockY() - 1, copy.getBlockZ()).isEmpty()) {
            copy.setY(origin.getY() - 1);
            i++;
        }
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

    public static Direction reverseDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            default:
                return direction.UP;
        }
    }
}
