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
        connections.add(new Connection(this, node, node.coordinates.distance(coordinates), weight, true));
    }

    public Connection lowestCost(Connection exclude, int minHeadroom, Node goal) {
        double previousCost = -1;
        Connection bestConnection = null;
        for(Connection connection : connections) {
            if(connection == exclude || connection.getHeadroom() < minHeadroom) continue;

            double cost = connection.getScore() + connection.heuristic(goal);
            if(previousCost == -1 || cost < previousCost) {
                bestConnection = connection;
            }
        }

        return bestConnection;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Node) {
            return coordinates.equals(((Node)other).coordinates);
        }
        else return false;
    }
}
