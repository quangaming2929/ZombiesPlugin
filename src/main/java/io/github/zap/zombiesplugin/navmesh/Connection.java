package io.github.zap.zombiesplugin.navmesh;

public class Connection {
    private Node node;
    private double distance;
    private double weight;

    public boolean traversable;

    public Connection(Node node, double distance, double weight, boolean traversable) {
        this.node = node;
        this.distance = distance;
        this.weight = weight;
        this.traversable = traversable;
    }

    public Node getNode() { return node; }
    public double getDistance() { return distance; }
    public double getWeight() { return weight; }
}
