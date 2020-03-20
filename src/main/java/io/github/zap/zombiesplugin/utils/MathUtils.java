package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.navmesh.Direction;
import org.bukkit.util.Vector;

public class MathUtils {
    public static Vector pushVectorAlong(Vector vector, Direction direction, int amount) {
        Vector copy = new Vector(vector.getX(), vector.getY(), vector.getZ());
        switch (direction) {
            case NORTH:
                copy.setZ(copy.getZ() - amount);
                return copy;
            case NORTHEAST:
                copy.setZ(copy.getZ() - amount);
                copy.setX(copy.getX() + amount);
                return copy;
            case EAST:
                copy.setX(copy.getX() + amount);
                return copy;
            case SOUTHEAST:
                copy.setZ(copy.getZ() + amount);
                copy.setX(copy.getX() + amount);
                return copy;
            case SOUTH:
                copy.setZ(copy.getZ() + amount);
                return copy;
            case SOUTHWEST:
                copy.setZ(copy.getZ() + amount);
                copy.setX(copy.getX() - amount);
                return copy;
            case WEST:
                copy.setX(copy.getX() - amount);
                return copy;
            case NORTHWEST:
                copy.setZ(copy.getZ() - amount);
                copy.setX(copy.getX() - amount);
                return copy;
            case UP:
                copy.setY(copy.getY() + amount);
                return copy;
            case DOWN:
                copy.setY(copy.getY() - amount);
                return copy;
            default:
                return new Vector(); //this can never happen
        }
    }

    public static double distanceSquared(Vector first, Vector second) {
        return Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2) + Math.pow(first.getZ() - second.getZ(), 2);
    }
}
