package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;
import io.github.zap.zombiesplugin.utils.WorldUtils;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class MeshBlock {
    private NavmeshGenerator generator;
    private HashMap<Direction, MeshBlock> connections = new HashMap<>();
    private Node[] nodes;

    public MeshBlock(NavmeshGenerator generator) {
        this.generator = generator;
    }

    public void connectTo(MeshBlock block, Direction heading, double weight) {
        if(block == null || block.getNodes() == null || nodes == null) return;

        for(Node node1 : nodes) {
            for(Node node2 : block.getNodes()) {
                node1.connectTo(node2, weight);
            }
        }

        setConnection(heading, block);
        block.setConnection(WorldUtils.reverseDirection(heading), this);
    }

    public boolean isConnectedTo(MeshBlock other) {
        return connections.containsValue(other);
    }

    public Node[] getNodes() {
        return nodes;
    }

    public MeshBlock getConnectionAt(Direction direction) {
        return connections.get(direction);
    }

    public void setConnection(Direction side, MeshBlock block) {
        connections.put(side, block);
    }

    /**
     * Creates an array of block nodes at the specified origin. Currently, this spawns 5 Node objects in an X shape, like
     * the 5 pattern on a dice. More nodes can be added, but they must not overlap the bounds of a single block (right
     * on the edge is fine). Nodes will not be placed on top of each other as this function checks whether or not there
     * is a neighboring BlockMesh.
     * @param origin The block coordinates to spawn the nodes at
     */
    public void generateNodes(Vector origin, int headroom) {
        nodes = new Node[5];

        if(getConnectionAt(Direction.NORTH) == null) {
            if(getConnectionAt(Direction.WEST) == null) {
                nodes[0] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ()), headroom);
            }
            if(getConnectionAt(Direction.EAST) == null) {
                nodes[1] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ()), headroom);
            }
        }

        nodes[2] = new Node(new Vector(origin.getX() + 0.5, origin.getY() - 1, origin.getZ() + 0.5), headroom);

        if(getConnectionAt(Direction.SOUTH) == null) {
            if(getConnectionAt(Direction.WEST) == null) {
                nodes[3] = new Node(new Vector(origin.getX(), origin.getY() - 1, origin.getZ() + 1), headroom);
            }
            if(getConnectionAt(Direction.EAST) == null) {
                nodes[4] = new Node(new Vector(origin.getX() + 1, origin.getY() - 1, origin.getZ() + 1), headroom);
            }
        }
    }
}