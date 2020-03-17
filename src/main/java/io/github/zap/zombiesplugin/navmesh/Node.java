package io.github.zap.zombiesplugin.navmesh;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Node {
    public Vector coordinates;
    public ArrayList<Connection> connections;
    public int headroom;

    public Node(Vector origin, int headroom) {
        coordinates = origin;
        connections = new ArrayList<>();
        this.headroom = headroom;
    }

    public void connectTo(Node node, double weight) {
        connections.add(new Connection(node, node.coordinates.distance(coordinates), weight, node.headroom,true));
    }

    public void merge(Node from) {
        connections.addAll(from.connections);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Node) {
            return coordinates.equals(((Node)other).coordinates);
        }
        else return false;
    }
}
