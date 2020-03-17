package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class NavmeshGenerator {
    private World world;

    private int headroomTestLimit;

    private int northLimit;
    private int southLimit;

    private int eastLimit;
    private int westLimit;

    private int upLimit;
    private int downLimit;

    private HashMap<Vector,MeshBlock> meshBlocks = new HashMap<>();

    public NavmeshGenerator(World world, int headroomTestLimit, Vector corner1, Vector corner2) {
        this.headroomTestLimit = headroomTestLimit;

        northLimit = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        southLimit = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        eastLimit = Math.max(corner1.getBlockX(), corner2.getBlockX());
        westLimit = Math.min(corner1.getBlockX(), corner2.getBlockX());

        upLimit = Math.max(corner1.getBlockY(), corner2.getBlockY());
        downLimit = Math.min(corner1.getBlockY(), corner2.getBlockY());
    }

    public void generateNavmesh(Vector origin, MeshBlock previousMesh) {
        //if the origin is in the air, go down first
        if(WorldUtils.getBlockAdjacent(world, origin, Direction.DOWN, 1).isEmpty() && previousMesh == null) {
            generateNavmesh(origin.setY(origin.getY() - 1), null);
            return;
        }

        tryConnect(origin, Direction.NORTH, previousMesh);
        tryConnect(origin, Direction.EAST, previousMesh);
        tryConnect(origin, Direction.SOUTH, previousMesh);
        tryConnect(origin, Direction.WEST, previousMesh);
    }

    private void tryConnect(Vector origin, Direction direction, MeshBlock previous) {
        if(previous == null) return;

        Block block = WorldUtils.getBlockAdjacent(world, origin, direction, 1);
        Vector vector = block.getLocation().toVector();

        //bounds check
        if(vector.getBlockX() > eastLimit || vector.getBlockX() < westLimit || vector.getBlockY() > upLimit ||
                vector.getBlockY() < downLimit || vector.getBlockZ() < northLimit || vector.getBlockZ() > southLimit) return;

        if(block.isEmpty()) {
            if(WorldUtils.getBlockAdjacent(block, Direction.DOWN, 1).isEmpty()) {
                Vector downVector = WorldUtils.seekDown(world, vector);
                if(meshBlocks.get(downVector).hasConnectedHeading(direction)) return;

                MeshBlock currentMesh = new MeshBlock(this);
                meshBlocks.put(downVector, currentMesh);

                previous.connectTo(currentMesh, direction, downVector.getBlockY() - vector.getBlockY());
                generateNavmesh(vector, currentMesh);
            }
            else {
                if(meshBlocks.get(vector).hasConnectedHeading(direction)) return;

                MeshBlock currentMesh = new MeshBlock(this);
                currentMesh.generateNodes(vector);

                meshBlocks.put(vector, currentMesh);

                previous.connectTo(currentMesh, direction, 0);
                generateNavmesh(vector, currentMesh);
            }
        }
        else if(WorldUtils.getHeadroom(world, vector, 2) > 0) {
            vector = MathUtils.pushVectorAlong(vector, Direction.UP, 1);
            if(meshBlocks.get(vector).hasConnectedHeading(direction)) return;

            MeshBlock currentMesh = new MeshBlock(this);
            currentMesh.generateNodes(vector);

            meshBlocks.put(vector, currentMesh);

            previous.connectTo(currentMesh, direction, 0);
            generateNavmesh(vector, currentMesh);
        }
    }

    public World getWorld() {
        return world;
    }

    public int getHeadroomTestLimit() {
        return headroomTestLimit;
    }
}