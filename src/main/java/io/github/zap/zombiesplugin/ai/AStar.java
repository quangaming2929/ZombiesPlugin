package io.github.zap.zombiesplugin.ai;

import io.github.zap.zombiesplugin.navmesh.Connection;
import io.github.zap.zombiesplugin.navmesh.Node;
import io.github.zap.zombiesplugin.utils.MathUtils;
import org.bukkit.World;

import java.util.*;

public class AStar {
    private class ConnectionWrapper implements Comparable<ConnectionWrapper> {
        public ConnectionWrapper previous;
        public Connection connection;

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
        HashSet<Node> closedSet = new HashSet<>();

        openSet.add(new ConnectionWrapper(start.getConnections().get(0)));

        while(openSet.size() != 0) {
            ConnectionWrapper currentConnection = openSet.remove();
            closedSet.add(currentConnection.connection.getTo());

            if(currentConnection.connection.getTo() == goal) {
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

                boolean setContains = setContainsNode(openSet, child.connection.getTo());
                ConnectionWrapper childCopy = null;

                if(setContains) {
                    childCopy = copy(child);
                }

                child.g = currentConnection.g + child.connection.getDistance();
                child.h = MathUtils.distanceSquared(child.connection.getTo().getCoordinates(), goal.getCoordinates());
                child.f = child.g + child.h;

                if(setContains) {
                    if(child.g > childCopy.g) {
                        continue;
                    }
                }

                openSet.add(child);
            }
        }
        return null;
    }

    private ConnectionWrapper copy(ConnectionWrapper input) {
        ConnectionWrapper wrapper = new ConnectionWrapper(input.connection);
        wrapper.g = input.g;
        wrapper.h = input.h;
        wrapper.f = input.f;
        return wrapper;
    }

    private boolean setContainsNode(PriorityQueue<ConnectionWrapper> queue, Node node) {
        for(ConnectionWrapper connection : queue) {
            if(connection.connection.getTo() == node) return true;
        }
        return false;
    }
}