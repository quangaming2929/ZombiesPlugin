package io.github.zap.zombiesplugin.ai;

import io.github.zap.zombiesplugin.navmesh.Connection;
import io.github.zap.zombiesplugin.navmesh.Node;
import org.bukkit.util.Vector;

public class Path {
    public class Segment {
        public Vector start;
        public Vector end;

        public Segment next;
        public Segment previous;
    }

    private Vector start;
    private Vector goal;

    private Segment first;
    private Segment last;

    public Path(Node start, Node goal) {
        this.start = start.getCoordinates();
        this.goal = goal.getCoordinates();
    }

    public void prependConnection(Connection connection) {
        if(last == null) {
            last = new Segment();
            first = last;
            last.end = connection.getTo().getCoordinates();
            last.start = connection.getFrom().getCoordinates();
        }
        else {
            first.previous = new Segment();
            first.previous.next = first;
            first = first.previous;

            first.end = connection.getTo().getCoordinates();
            first.start = connection.getFrom().getCoordinates();
        }
    }

    public Segment getFirst() {
        return first;
    }

    public Segment getLast() {
        return last;
    }
}
