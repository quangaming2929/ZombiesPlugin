package io.github.zap.zombiesplugin.navmesh;

public class Connection {
    private Node from;
    private Node to;

    private double score;
    private double weight;

    public boolean traversable;

    /**
     * Represents a connection between two nodes.
     * @param from The source node
     * @param destination The destination node
     * @param distance The distance from the node holding this object
     * @param weight A value used to skew the likelihood of an enemy choosing this connection
     * @param traversable Whether or not to use this node at all
     */
    public Connection(Node from, Node destination, double distance, double weight, boolean traversable) {
        this.from = from;
        this.to = destination;
        this.score = distance * weight;
        this.weight = weight;
        this.traversable = traversable;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public int getHeadroom() { return to.headroom; }

    public double getScore() { return score; }
    public double getWeight() { return weight; }

    //larger value = worse path to travel along
    public double heuristic(Node goal) {
        return 5D/to.connections.size() + to.coordinates.distance(goal.coordinates);
    }
}
