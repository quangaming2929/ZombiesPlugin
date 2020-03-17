package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class MeshBlock {
    private NavmeshGenerator generator;
    private HashMap<Direction, Boolean> connectedHeading = new HashMap<>();
    private HashMap<Direction, MeshBlock> neighbors = new HashMap<>();
    private Node[] nodes;

    public MeshBlock(NavmeshGenerator generator) {
        this.generator = generator;
    }

    /**
     * Connects every node in the invoking MeshBlock to the node in the target MeshBlock.
     * @param block The target MeshBlock
     * @param connectionHeading Which direction the connection is heading (A MeshBlock on the west side of this one
     *                          would pass Direction.EAST)
     * @param weight Used by A* to change the likelihood of enemies pathfinding through any created connections between
     *               this MeshBlock and the target
     */
    public void connectTo(MeshBlock block, Direction connectionHeading, double weight) {
        if(block == null) return;
        for(Node node1 : nodes) {
            for(Node node2 : block.getNodes()) {
                node1.connectTo(node2, weight);
            }
        }

        block.setConnectedHeading(connectionHeading, true);
        block.setNeighbor(WorldUtils.reverseDirection(connectionHeading), this);
        setNeighbor(connectionHeading, block);
    }

    /**
     * Used by the NavmeshGenerator to determine if this side has already been connected to.
     * @param direction The direction the connection is going
     * @return
     */
    public boolean hasConnectedHeading(Direction direction) {
        return connectedHeading.getOrDefault(direction, false);
    }

    /**
     * Used by the NavmeshGenerator to set the connected side.
     * @param direction The direction the connection is heading
     * @param value Whether or not to set the heading as connected or not
     */
    public void setConnectedHeading(Direction direction, boolean value) {
        connectedHeading.put(direction, value);
    }

    public void setNeighbor(Direction direction, MeshBlock neighbor) {
        neighbors.put(direction, neighbor);
    }

    public MeshBlock getNeighbor(Direction to) {
        return neighbors.getOrDefault(to, null);
    }

    public Node[] getNodes() {
        return nodes;
    }

    /**
     * Creates an array of block nodes at the specified origin. Currently, this spawns 5 Node objects in an X shape, like
     * the 5 pattern on a dice. More nodes can be added, but they must not overlap the bounds of a single block (right
     * on the edge is fine). Nodes will not be placed on top of each other as this function checks whether or not there
     * is a neighboring BlockMesh.
     * @param origin The block coordinates to spawn the nodes at
     */
    public void generateNodes(Vector origin) {
        int headroom = WorldUtils.getHeadroom(generator.getWorld(), MathUtils.pushVectorAlong(origin, Direction.DOWN, 1), generator.getHeadroomTestLimit());

        nodes = new Node[5];

        if(getNeighbor(Direction.NORTH) == null) {
            if(getNeighbor(Direction.WEST) == null) {
                nodes[0] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ()), headroom);
            }
            if(getNeighbor(Direction.EAST) == null) {
                nodes[1] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ()), headroom);
            }
        }

        nodes[2] = new Node(new Vector(origin.getX() + 0.5, origin.getY() - 1, origin.getZ() + 0.5), headroom);

        if(getNeighbor(Direction.SOUTH) == null) {
            if(getNeighbor(Direction.WEST) == null) {
                nodes[3] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ() + 1), headroom);
            }
            if(getNeighbor(Direction.EAST) == null) {
                nodes[4] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ() + 1), headroom);
            }
        }
    }
}