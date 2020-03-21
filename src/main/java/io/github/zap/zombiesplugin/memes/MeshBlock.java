package io.github.zap.zombiesplugin.memes;

import org.bukkit.util.Vector;

import java.util.HashMap;

public class MeshBlock {
    private HashMap<Direction, MeshBlock> connections = new HashMap<>();
    private Node node;

    public void connectTo(MeshBlock block, Direction heading, int headroom, double distance, double weight) {
        node.connectTo(block.getNode(), headroom, distance, weight);
        connections.put(heading, block);
    }

    public boolean isConnectedTo(MeshBlock other) {
        return connections.containsValue(other);
    }

    public Node getNode() {
        return node;
    }

    public boolean hasConnectionAt(Direction dir) {
        return connections.get(dir) != null;
    }

    public HashMap<Direction,MeshBlock> getConnections() {return connections;}

    public void generateNode(Vector origin, int headroom) {
        node = new Node(new Vector(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5), headroom);
    }
}