package io.github.zap.zombiesplugin.utils;

import io.github.zap.zombiesplugin.navmesh.Direction;
import org.bukkit.util.Vector;

public class MathUtils {
    public static Vector getVectorAlong(Vector vector, Direction direction, int amount) {
        Vector copy = new Vector(vector.getX(), vector.getY(), vector.getZ());
        switch (direction) {
            case NORTH:
                copy.setZ(copy.getY() - amount);
                return copy;
            case EAST:
                copy.setX(copy.getX() + amount);
                return copy;
            case SOUTH:
                copy.setZ(copy.getZ() + amount);
                return copy;
            case WEST:
                copy.setX(copy.getX() - amount);
                return copy;
            case UP:
                copy.setY(copy.getY() + amount);
                return copy;
            case DOWN:
                copy.setX(copy.getY() - amount);
                return copy;
            default:
                return null; //this can never happen
        }
    }
}
