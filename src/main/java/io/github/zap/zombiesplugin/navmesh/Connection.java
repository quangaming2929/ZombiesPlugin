package io.github.zap.zombiesplugin.navmesh;

public class Connection {
    private Node node;
    private double distance;
    private double weight;
    private int headroom;

    public boolean traversable;

    /**
     * Represents a connection between two nodes.
     * @param node The endpoint node
     * @param distance The distance from the node holding this object
     * @param weight A value used to skew the likelihood of an enemy choosing this connection
     * @param traversable Whether or not to use this node at all
     */
    public Connection(Node node, double distance, double weight, int headroom, boolean traversable) {
        this.node = node;
        this.distance = distance;
        this.weight = weight;
        this.headroom = headroom;
        this.traversable = traversable;
    }

    public Node getNode() { return node; }
    public double getDistance() { return distance; }
    public double getWeight() { return weight; }
}
