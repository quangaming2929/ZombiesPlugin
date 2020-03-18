package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.Tuple;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import javafx.scene.shape.Mesh;
import org.bukkit.Material;
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
        this.world = world;
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
        if(WorldUtils.getBlockAdjacent(world, origin, Direction.DOWN, 1).getType() == Material.AIR && previousMesh == null) {
            generateNavmesh(origin.setY(origin.getY() - 1), null);
            return;
        }

        if(previousMesh == null) {
            int headroom = WorldUtils.getHeadroom(world, origin, headroomTestLimit);

            MeshBlock currentMesh = new MeshBlock(this);
            currentMesh.generateNodes(origin, headroom);
            generateNavmesh(origin, currentMesh);
            return;
        }

        Tuple<MeshBlock, Vector>[] meshBlocks = new Tuple[4];
        meshBlocks[0] = tryConnect(origin, Direction.NORTH, previousMesh);
        meshBlocks[1] = tryConnect(origin, Direction.EAST, previousMesh);
        meshBlocks[2] = tryConnect(origin, Direction.SOUTH, previousMesh);
        meshBlocks[3] = tryConnect(origin, Direction.WEST, previousMesh);

        for(Tuple<MeshBlock, Vector> meshBlock : meshBlocks) {
            if(meshBlock != null) generateNavmesh(meshBlock.y, meshBlock.x);
        }
    }

    private Tuple<MeshBlock, Vector> tryConnect(Vector origin, Direction direction, MeshBlock current) {
        Block sampleBlock = WorldUtils.getBlockAdjacent(world, origin, direction, 1);
        Vector sampleVector = sampleBlock.getLocation().toVector();

        //bounds check
        if(outOfBounds(sampleVector)) return null;

        //adjacent block is air
        if(sampleBlock.isPassable()) {
            Block downBlock = WorldUtils.getBlockAdjacent(sampleBlock, Direction.DOWN, 1);

            //block below adjacent block is air...
            if(downBlock.isPassable()) {
                Vector downVector = WorldUtils.seekDown(world, sampleVector);
                if(outOfBounds(downVector)) return null;

                //get the mesh at the downVector, if there is any
                int headroom = WorldUtils.getHeadroom(world, downVector, headroomTestLimit);
                MeshBlock targetMesh = meshBlocks.get(downVector);

                //there isn't a mesh there, so create one
                if(targetMesh == null) {
                    targetMesh = setupMesh(downVector, headroom);

                    //DEBUG
                    world.getBlockAt(downVector.getBlockX(), downVector.getBlockY() - 1, downVector.getBlockZ()).setType(Material.BLUE_WOOL);
                }
                //there is a mesh so see if we've already connected from this side
                else if(targetMesh.getConnectionAt(WorldUtils.reverseDirection(direction)) != null) return null;

                //connect the current mesh to the target mesh
                current.connectTo(targetMesh, direction, downVector.getBlockY() - sampleVector.getBlockY());
                return new Tuple<>(targetMesh, downVector);
            }
            else { //block below adjacent block is not air, simplest case
                int headroom = WorldUtils.getHeadroom(world, downBlock.getLocation().toVector(), headroomTestLimit);
                MeshBlock targetMesh = meshBlocks.get(sampleVector);

                if(targetMesh == null) {
                    targetMesh = setupMesh(sampleVector, headroom);

                    //DEBUG
                    world.getBlockAt(sampleVector.getBlockX(), sampleVector.getBlockY() - 1, sampleVector.getBlockZ()).setType(Material.GREEN_WOOL);
                }
                else if(targetMesh.getConnectionAt(WorldUtils.reverseDirection(direction)) != null) return null;

                current.connectTo(targetMesh, direction, 0);
                return new Tuple<>(targetMesh, sampleVector);
            }
        }
        else {
            int headroom = WorldUtils.getHeadroom(world, sampleVector, headroomTestLimit);
            if(headroom > 0) {
                Vector upVector = MathUtils.pushVectorAlong(sampleVector, Direction.UP, 1);
                if(outOfBounds(upVector)) return null;

                MeshBlock targetMesh = meshBlocks.get(upVector);

                if(targetMesh == null) {
                    targetMesh = setupMesh(upVector, headroom);

                    //DEBUG
                    world.getBlockAt(upVector.getBlockX(), upVector.getBlockY() - 1, upVector.getBlockZ()).setType(Material.RED_WOOL);
                }
                else if(targetMesh.getConnectionAt(WorldUtils.reverseDirection(direction)) != null) return null;

                current.connectTo(targetMesh, direction, 0);
                return new Tuple<>(targetMesh, upVector);
            }
        }
        return null;
    }

    private MeshBlock setupMesh(Vector vector, int headroom) {
        MeshBlock result = new MeshBlock(this);
        result.generateNodes(vector, headroom);
        meshBlocks.put(vector, result);

        return result;
    }

    //checks to see if the provided vector is in bounds or not
    private boolean outOfBounds(Vector vector) {
        return vector.getBlockX() > eastLimit || vector.getBlockX() < westLimit || vector.getBlockY() > upLimit ||
                vector.getBlockY() < downLimit || vector.getBlockZ() < northLimit || vector.getBlockZ() > southLimit;
    }

    public World getWorld() {
        return world;
    }

    public int getHeadroomTestLimit() {
        return headroomTestLimit;
    }
}