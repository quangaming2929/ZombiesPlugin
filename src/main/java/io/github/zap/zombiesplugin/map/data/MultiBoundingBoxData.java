package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.BoundingBox;
import io.github.zap.zombiesplugin.map.MultiBoundingBox;

import java.util.ArrayList;

public class MultiBoundingBoxData implements IMapData<MultiBoundingBox> {
    public ArrayList<BoundingBoxData> bounds;

    public MultiBoundingBoxData() { }

    public MultiBoundingBoxData(MultiBoundingBox from) {
        bounds = new ArrayList<>();
        for(BoundingBox bound : from.getBounds()) {
            bounds.add(new BoundingBoxData(bound));
        }
    }

    @Override
    public MultiBoundingBox load() {
        MultiBoundingBox result = new MultiBoundingBox();

        for(BoundingBoxData bound : bounds) {
            result.add(bound.load());
        }

        return result;
    }
}
