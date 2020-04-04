package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.BoundingBox;

public class BoundingBoxData implements IMapData<BoundingBox> {
    public LocationData origin;
    public LocationData limit;

    public BoundingBoxData() {}

    public BoundingBoxData(BoundingBox from) {
        origin = new LocationData(from.getOrigin());
        limit = new LocationData(from.getLimit());
    }

    @Override
    public BoundingBox load() {
        return new BoundingBox(origin.load(), limit.load());
    }
}
