package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;

import java.util.ArrayList;

public class MultiBoundingBox {
    private ArrayList<BoundingBox> bounds;

    public MultiBoundingBox() {
        bounds = new ArrayList<>();
    }

    public MultiBoundingBox(ArrayList<BoundingBox> bounds) {
        this.bounds = bounds;
    }

    public void add(BoundingBox bound) {
        bounds.add(bound);
    }

    public boolean isInBound(Location location) {
        for(BoundingBox bound : bounds) {
            if(bound.isInBound(location)) return true;
        }
        return false;
    }

    public ArrayList<BoundingBox> getBounds() { return bounds; }
}
