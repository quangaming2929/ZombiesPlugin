package io.github.zap.zombiesplugin.map;

import org.bukkit.Location;

import java.util.ArrayList;

public class MultiBoundingBox {
    private ArrayList<BoundingBox> boxes;

    public MultiBoundingBox() {
        boxes = new ArrayList<>();
    }

    public MultiBoundingBox(ArrayList<BoundingBox> boxes) {
        this.boxes = boxes;
    }

    public void add(BoundingBox box) {
        boxes.add(box);
    }

    public boolean isInBound(Location location) {
        for(BoundingBox box : boxes) {
            if(box.isInBound(location)) return true;
        }
        return false;
    }
}
