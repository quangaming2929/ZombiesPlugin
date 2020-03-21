package io.github.zap.zombiesplugin.memes;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Node {
    private Vector coordinates;
    private ArrayList<Connection> connections;
    private int headroom;
    private boolean enabled;

    public Node(Vector origin, int headroom) {
        coordinates = origin;
        connections = new ArrayList<>();
        this.headroom = headroom;
        enabled = true;
    }

    public void connectTo(Node node, int headroom, double distance, double weight) {
        connections.add(new Connection(this, node, headroom, distance, weight));
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Node) {
            return coordinates.equals(((Node)other).coordinates);
        }
        else return false;
    }

    public Vector getCoordinates() { return coordinates; }
    public ArrayList<Connection> getConnections() { return connections; }
    public int getHeadroom() {return headroom; }
    public boolean getEnabled() { return enabled; }
    public void toggle() { enabled = !enabled; }
}
