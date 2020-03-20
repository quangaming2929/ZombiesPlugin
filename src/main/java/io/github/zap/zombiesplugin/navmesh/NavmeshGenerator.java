package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.Tuple;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class NavmeshGenerator {
    private class ConnectionInformation {
        public int targetHeadroom;
        public int connectionHeadroom;
        public double distance;
        public double weight;
        public Vector target;
    }

    private World world;

    private int headroomTestLimit;

    private int northLimit;
    private int southLimit;

    private int eastLimit;
    private int westLimit;

    private int upLimit;
    private int downLimit;

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

    public void debugNavmesh(Navmesh target) {
        for(Vector vector : target.getMeshBlocks().keySet()) {
            MeshBlock mesh = target.getMeshBlocks().get(vector);
            Vector below = MathUtils.pushVectorAlong(vector, Direction.DOWN, 1);

            switch(mesh.getConnections().size()) {
                case 8:
                    WorldUtils.getBlockAt(world, below).setType(Material.GREEN_WOOL);
                    break;
                case 7:
                    WorldUtils.getBlockAt(world, below).setType(Material.LIME_WOOL);
                    break;
                case 6:
                    WorldUtils.getBlockAt(world, below).setType(Material.BLUE_WOOL);
                    break;
                case 5:
                    WorldUtils.getBlockAt(world, below).setType(Material.LIGHT_BLUE_WOOL);
                    break;
                case 4:
                    WorldUtils.getBlockAt(world, below).setType(Material.YELLOW_WOOL);
                    break;
                case 3:
                    WorldUtils.getBlockAt(world, below).setType(Material.ORANGE_WOOL);
                    break;
                case 2:
                    WorldUtils.getBlockAt(world, below).setType(Material.RED_WOOL);
                    break;
                case 1:
                    WorldUtils.getBlockAt(world, below).setType(Material.GRAY_WOOL);
                    break;
                case 0:
                    WorldUtils.getBlockAt(world, below).setType(Material.LIGHT_GRAY_WOOL);
                    break;
                default:
                    WorldUtils.getBlockAt(world, below).setType(Material.BLACK_WOOL);
                    break;
            }
        }
    }

    public Navmesh generateNavmesh(Vector origin) {
        Navmesh navmesh = new Navmesh();
        //if the origin is in the air, go down first
        if(WorldUtils.getBlockAdjacent(world, origin, Direction.DOWN, 1).getType() == Material.AIR) {
            origin = WorldUtils.seekDown(world, origin);
        }

        int headroom = WorldUtils.getHeadroom(world, origin, headroomTestLimit);
        MeshBlock currentMesh = setupMesh(navmesh, origin, headroom);

        ArrayList<Tuple<MeshBlock,Vector>> newMeshes = new ArrayList<>();
        newMeshes.add(new Tuple<>(currentMesh, origin));

        int size;
        do {
            size = newMeshes.size();
            for(int i = size - 1; i >= 0; i--) { //reverse iteration so we can remove and add items as needed
                Tuple<MeshBlock,Vector> result = newMeshes.get(i);
                newMeshes.remove(i);

                Tuple<MeshBlock, Vector> newNorth = tryConnect(navmesh, result.y, Direction.NORTH, result.x);
                Tuple<MeshBlock, Vector> newNortheast = tryConnect(navmesh, result.y, Direction.NORTHEAST, result.x);

                Tuple<MeshBlock, Vector> newEast = tryConnect(navmesh, result.y, Direction.EAST, result.x);
                Tuple<MeshBlock, Vector> newSoutheast = tryConnect(navmesh, result.y, Direction.SOUTHEAST, result.x);

                Tuple<MeshBlock, Vector> newSouth = tryConnect(navmesh, result.y, Direction.SOUTH, result.x);
                Tuple<MeshBlock, Vector> newSouthwest = tryConnect(navmesh, result.y, Direction.SOUTHWEST, result.x);

                Tuple<MeshBlock, Vector> newWest = tryConnect(navmesh, result.y, Direction.WEST, result.x);
                Tuple<MeshBlock, Vector> newNorthwest = tryConnect(navmesh, result.y, Direction.NORTHWEST, result.x);

                if(newNorth != null) newMeshes.add(newNorth);
                if(newNortheast != null) newMeshes.add(newNortheast);
                if(newEast != null) newMeshes.add(newEast);
                if(newSoutheast != null) newMeshes.add(newSoutheast);

                if(newSouth != null) newMeshes.add(newSouth);
                if(newSouthwest != null) newMeshes.add(newSouthwest);
                if(newWest != null) newMeshes.add(newWest);
                if(newNorthwest != null) newMeshes.add(newNorthwest);
            }
        }
        while(size != 0);

        debugNavmesh(navmesh);
        return navmesh;
    }

    private Tuple<MeshBlock, Vector> tryConnect(Navmesh navmesh, Vector origin, Direction direction, MeshBlock currentMesh) {
        Vector sample = MathUtils.pushVectorAlong(origin, direction, 1);

        if(outOfBounds(sample)) return null;
        if(currentMesh.hasConnectionAt(direction)) return null;

        ConnectionInformation attempt = getTarget(origin, sample, direction);
        if(attempt == null) return null;

        MeshBlock targetMesh = navmesh.getMeshBlocks().get(attempt.target);
        if(targetMesh == null) { //create new mesh
            targetMesh = setupMesh(navmesh, attempt.target, attempt.targetHeadroom);
        }

        currentMesh.connectTo(targetMesh, direction, attempt.connectionHeadroom, attempt.distance, attempt.weight);
        return new Tuple<>(targetMesh, attempt.target);
    }

    //attempts to move to the target vector 'sample' from origin, returning null if impossible
    private ConnectionInformation getTarget(Vector origin, Vector sample, Direction direction) {
        Vector sampleUp = MathUtils.pushVectorAlong(sample, Direction.UP, 1);
        Vector sampleDown = MathUtils.pushVectorAlong(sample, Direction.DOWN, 1);

        boolean samplePassable = WorldUtils.getBlockAt(world, sample).isPassable();
        boolean sampleUpPassable = WorldUtils.getBlockAt(world, sampleUp).isPassable();
        boolean sampleDownPassable = WorldUtils.getBlockAt(world, sampleDown).isPassable();

        boolean diagonal = WorldUtils.isIntercardinal(direction);

        ConnectionInformation result = new ConnectionInformation();

        if(!samplePassable) {
            if(!sampleUpPassable) {
                return null;
            }
            else { //moving up
                if(outOfBounds(sampleUp)) return null;

                if(diagonal) { //moving up diagonally
                    Tuple<Vector,Vector> cardinalSamples = getCardinalSamples(MathUtils.pushVectorAlong(origin, Direction.UP, 1), direction);

                    int firstHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.x, headroomTestLimit);
                    int secondHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.y, headroomTestLimit);
                    int originHeadroom = WorldUtils.getHeadroom(world, origin, headroomTestLimit);

                    int limiter = Math.min(firstHeadroom, Math.min(originHeadroom, secondHeadroom));
                    if(limiter <= 1) return null;

                    int targetHeadroom = WorldUtils.getHeadroom(world, sampleUp, headroomTestLimit);

                    result.connectionHeadroom = Math.min(limiter - 1, targetHeadroom);
                    result.targetHeadroom = targetHeadroom;
                    result.target = sampleUp;
                    result.weight = 1;
                    result.distance = 5.8284;
                    return result;
                }
                else { //moving up cardinally
                    int originHeadroom = WorldUtils.getHeadroom(world, origin, headroomTestLimit);
                    if(originHeadroom == 1) return null;

                    int targetHeadroom = WorldUtils.getHeadroom(world, sampleUp, headroomTestLimit);

                    result.connectionHeadroom = Math.min(originHeadroom - 1, targetHeadroom);
                    result.targetHeadroom = targetHeadroom;
                    result.target = sampleUp;
                    result.weight = 1;
                    result.distance = 4;
                    return result;
                }
            }
        }
        else if(sampleDownPassable) { //moving down
            if(outOfBounds(sampleDown)) return null;

            if(diagonal) {
                Tuple<Vector,Vector> cardinalSamples = getCardinalSamples(origin, direction);

                int firstHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.x, headroomTestLimit);
                int secondHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.y, headroomTestLimit);

                int limiter = Math.min(firstHeadroom, secondHeadroom);
                if(limiter == 0) return null;

                Vector target = WorldUtils.seekDown(world, sampleDown);
                if(outOfBounds(target)) return null;

                int targetHeadroom = WorldUtils.getHeadroom(world, target, headroomTestLimit);
                int verticalFall = origin.getBlockY() - target.getBlockY();
                boolean willDamage = verticalFall <= 4;

                result.connectionHeadroom = Math.min(limiter, targetHeadroom);
                result.targetHeadroom = targetHeadroom;
                result.target = target;
                result.weight = willDamage ? verticalFall - 2 : 1;
                result.distance = Math.pow(0.7071 + verticalFall, 2);
                return result;
            }
            else {
                int originHeadroom = WorldUtils.getHeadroom(world, sample, headroomTestLimit);

                Vector target = WorldUtils.seekDown(world, sampleDown);
                if(outOfBounds(target)) return null;

                int targetHeadroom = WorldUtils.getHeadroom(world, target, headroomTestLimit);
                int verticalFall = origin.getBlockY() - target.getBlockY();
                boolean willDamage = verticalFall <= 4;

                result.connectionHeadroom = Math.min(originHeadroom, targetHeadroom);
                result.targetHeadroom = targetHeadroom;
                result.target = target;
                result.weight = willDamage ? verticalFall - 2 : 1;
                result.distance = Math.pow(0.5 + verticalFall, 2);
                return result;
            }
        }
        else { //constant elevation
            if(diagonal) {
                Tuple<Vector,Vector> cardinalSamples = getCardinalSamples(origin, direction);

                int firstHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.x, headroomTestLimit);
                int secondHeadroom = WorldUtils.getHeadroom(world, cardinalSamples.y, headroomTestLimit);

                int limiter = Math.min(firstHeadroom, secondHeadroom);
                if(limiter == 0) return null;

                int targetHeadroom = WorldUtils.getHeadroom(world, sample, headroomTestLimit);

                result.connectionHeadroom = Math.min(limiter, targetHeadroom);
                result.targetHeadroom = targetHeadroom;
                result.target = sample;
                result.weight = 1;
                result.distance = 2;
                return result;
            }
            else {
                int originHeadroom = WorldUtils.getHeadroom(world, origin, headroomTestLimit);
                int targetHeadroom = WorldUtils.getHeadroom(world, sample, headroomTestLimit);

                result.connectionHeadroom = Math.min(originHeadroom, targetHeadroom);
                result.targetHeadroom = targetHeadroom;
                result.target = sample;
                result.weight = 1;
                return result;
            }
        }
    }

    private Tuple<Vector,Vector> getCardinalSamples(Vector vector, Direction direction) {
        Direction[] directions = WorldUtils.splitIntercardinal(direction);
        Vector firstCardinalSample = MathUtils.pushVectorAlong(vector, directions[0], 1);
        Vector secondCardinalSample = MathUtils.pushVectorAlong(vector, directions[1], 1);

        return new Tuple<>(firstCardinalSample, secondCardinalSample);
    }

    private MeshBlock setupMesh(Navmesh navmesh, Vector vector, int headroom) {
        MeshBlock result = new MeshBlock();
        result.generateNode(vector, headroom);
        navmesh.getMeshBlocks().put(vector, result);

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