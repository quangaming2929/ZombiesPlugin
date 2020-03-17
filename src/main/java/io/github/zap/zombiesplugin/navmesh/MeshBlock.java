package io.github.zap.zombiesplugin.navmesh;

import java.util.HashMap;

public class MeshBlock {
    private HashMap<Direction, Boolean> connectedSides = new HashMap<>();
    private Node[] nodes;

    public MeshBlock(Node[] nodes) {
        this.nodes = nodes;
    }

    public void connectTo(MeshBlock block, Direction targetSide, double weight) {
        if(block == null) return;
        for(Node node1 : nodes) {
            for(Node node2 : block.getNodes()) {
                node1.connectTo(node2, weight);
            }
        }

        setConnectionAtSide(targetSide, true);
    }

    public boolean hasConnectionAtSide(Direction direction) {
        return connectedSides.get(direction);
    }

    public void setConnectionAtSide(Direction direction, boolean value) {
        connectedSides.put(direction, value);
    }

    public Node[] getNodes() {
        return nodes;
    }
}
