package io.github.zap.zombiesplugin.memes;

import io.github.zap.zombiesplugin.utils.MathUtils;

import java.util.*;

public class AStar {
    private class ConnectionWrapper implements Comparable<ConnectionWrapper> {
        public ConnectionWrapper previous;
        public Connection connection;

        public boolean closed;

        public double g = 0;
        public double h = 0;
        public double f = 0;

        public ConnectionWrapper(Connection connection) {
            this.connection = connection;
        }

        @Override
        public int compareTo(ConnectionWrapper o) {
            if(f < o.f) return -1;
            else if(f == o.f) return 0;
            return 1;
        }

        public ArrayList<ConnectionWrapper> getChildren(){
            ArrayList<ConnectionWrapper> result = new ArrayList<>();
            for(Connection connection : connection.getTo().getConnections()) {
                ConnectionWrapper child = new ConnectionWrapper(connection);
                child.previous = this;
                result.add(child);
            }
            return result;
        }
    }

    public Path navigateTo(Node start, Node goal, int height) {
        if(start == null ||  goal == null || start.getConnections().size() == 0) return null;

        PriorityQueue<ConnectionWrapper> openSet = new PriorityQueue<>();
        HashSet<Node> closedSet = new HashSet<>(); //should have O(1) .contains() implementation

        openSet.add(new ConnectionWrapper(start.getConnections().get(0)));

        while(openSet.size() != 0) {
            ConnectionWrapper currentConnection = openSet.remove();
            closedSet.add(currentConnection.connection.getTo());

            if(currentConnection.connection.getTo() == goal) { //success condition
                Path result = new Path(start, goal);
                while(currentConnection != null) {
                    result.prependConnection(currentConnection.connection);
                    currentConnection = currentConnection.previous;
                }
                return result;
            }

            ArrayList<ConnectionWrapper> nodes = currentConnection.getChildren();
            for(ConnectionWrapper child : nodes) {
                if(closedSet.contains(child.connection.getTo())) continue;

                child.g = currentConnection.g + child.connection.getDistance();
                child.h = MathUtils.distanceSquared(child.connection.getTo().getCoordinates(), goal.getCoordinates());
                child.f = child.g + child.h;

                ConnectionWrapper setConnection = setContainsNode(openSet, child.connection.getTo());
                if(setConnection != null) {
                    if(child.g > setConnection.g) {
                        continue;
                    }
                }

                openSet.add(child);
            }
        }
        return null; //fail condition
    }

    private ConnectionWrapper setContainsNode(PriorityQueue<ConnectionWrapper> queue, Node node) {
        for(ConnectionWrapper wrapper : queue) {
            if(wrapper.connection.getTo() == node) return wrapper;
        }
        return null;
    }
}