package io.github.zap.zombiesplugin.navmesh;

import io.github.zap.zombiesplugin.utils.MathUtils;

public class Connection {
    private Node from;
    private Node to;

    private double distance;

    private int headroom;

    public Connection(Node from, Node destination, int headroom, double distance, double weight) {
        this.from = from;
        this.to = destination;
        this.headroom = headroom;
        this.distance = distance * weight;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }

    public int getHeadroom() { return headroom; }
    public double getDistance() { return distance; }
}
