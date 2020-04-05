package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

public class SpawnPointData implements IMapData<SpawnPoint> {
    public LocationData spawn;
    public LocationData target;

    public SpawnPointData() {}

    public SpawnPointData(SpawnPoint from) {
        spawn = new LocationData(from.getSpawn());
        target = new LocationData(from.getTarget());
    }

    @Override
    public SpawnPoint load(Object args) {
        return new SpawnPoint(spawn.load(null), target.load(null));
    }
}
