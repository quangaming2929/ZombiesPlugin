package io.github.zap.zombiesplugin.navmesh;

public class Connection {
    private Node source;
    private Node destination;

    private double score;
    private double weight;

    public boolean traversable;

    /**
     * Represents a connection between two nodes.
     * @param source The source node
     * @param destination The destination node
     * @param distance The distance from the node holding this object
     * @param weight A value used to skew the likelihood of an enemy choosing this connection
     * @param traversable Whether or not to use this node at all
     */
    public Connection(Node source, Node destination, double distance, double weight, boolean traversable) {
        this.source = source;
        this.destination = destination;
        this.score = distance * weight;
        this.weight = weight;
        this.traversable = traversable;
    }

    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public int getHeadroom() { return destination.headroom; }
    public double getScore() { return score; }
    public double getWeight() { return weight; }
}
