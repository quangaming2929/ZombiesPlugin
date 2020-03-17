package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class NavmeshGenerator {
    private World world;

    private int headroomTest;

    private int northLimit;
    private int southLimit;

    private int eastLimit;
    private int westLimit;

    private int upLimit;
    private int downLimit;

    private ArrayList<MeshBlock> meshBlocks = new ArrayList<>();

    public NavmeshGenerator(World world, int maxHeadroomTest, Vector corner1, Vector corner2) {
        headroomTest = maxHeadroomTest;

        northLimit = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        southLimit = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        eastLimit = Math.max(corner1.getBlockX(), corner2.getBlockX());
        westLimit = Math.min(corner1.getBlockX(), corner2.getBlockX());

        upLimit = Math.max(corner1.getBlockY(), corner2.getBlockY());
        downLimit = Math.min(corner1.getBlockY(), corner2.getBlockY());
    }

    public void generateNavmesh(Vector origin, MeshBlock previousMesh) {
        //if the origin is in the air, go down first
        if(getBlockAdjacent(origin, Direction.DOWN, 1).isEmpty() && previousMesh == null) {
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

        Block block = getBlockAdjacent(origin, direction, 1);
        Vector vector = block.getLocation().toVector();

        if(block.isEmpty()) {
            if(getBlockAdjacent(block, Direction.DOWN, 1).isEmpty()) {
                int dropDistance = seekDown(vector);

                MeshBlock currentMesh = new MeshBlock(generateNodes(vector));
                meshBlocks.add(currentMesh);

                previous.connectTo(currentMesh, reverse(direction), -dropDistance);
                generateNavmesh(vector, currentMesh);
            }
            else {
                MeshBlock currentMesh = new MeshBlock(generateNodes(vector));
                meshBlocks.add(currentMesh);

                previous.connectTo(currentMesh, reverse(direction), 0);
                generateNavmesh(vector, currentMesh);
            }
        }
        else if(getHeadroom(vector, 2) > 0) {
            MathUtils.getVectorAlong(vector, Direction.UP, 1);

            MeshBlock currentMesh = new MeshBlock(generateNodes(vector));
            meshBlocks.add(currentMesh);

            previous.connectTo(currentMesh, reverse(direction), 0);
            generateNavmesh(block.getLocation().toVector(), currentMesh);
        }
    }

    private Direction reverse(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return  Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            default:
                return null; //this can never happen
        }
    }

    private Block getBlockAdjacent(Vector origin, Direction direction, int amount) {
        Vector adjacent = MathUtils.getVectorAlong(origin, direction, amount);
        return world.getBlockAt(adjacent.getBlockX(), adjacent.getBlockY(), adjacent.getBlockZ());
    }

    private Block getBlockAdjacent(Block block, Direction direction, int amount) {
        Vector adjacent = MathUtils.getVectorAlong(block.getLocation().toVector(), direction, amount);
        return world.getBlockAt(adjacent.getBlockX(), adjacent.getBlockY(), adjacent.getBlockZ());
    }

    private int getHeadroom(Vector origin, int limitInclusive) {
        int i = 0;
        while(world.getBlockAt(origin.getBlockX(), origin.getBlockY() + i + 1, origin.getBlockZ()).isEmpty() &&
                i < limitInclusive) {
            i++;
        }
        return i;
    }

    private int seekDown(Vector origin) {
        int i = 0;
        while(world.getBlockAt(origin.getBlockX(), origin.getBlockY() - 1, origin.getBlockZ()).isEmpty()) {
            origin.setY(origin.getY() - 1);
            i++;
        }
        return i;
    }

    //currently creates 5 nodes per block but could be easily changed to create more
    private Node[] generateNodes(Vector origin) {
        int headroom = getHeadroom(MathUtils.getVectorAlong(origin, Direction.DOWN, 1), headroomTest);
        Node[] nodes = new Node[5];
        nodes[0] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ()), headroom);
        nodes[1] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ()), headroom);
        nodes[2] = new Node(new Vector(origin.getX() + 0.5, origin.getY() - 1, origin.getZ() + 0.5), headroom);
        nodes[3] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ() + 1), headroom);
        nodes[4] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ() + 1), headroom);
        return nodes;
    }
}