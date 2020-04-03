package io.github.zap.zombiesplugin.map.data;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

public class SpawnPointData {
    public LocationData spawn;
    public LocationData target;

    public SpawnPointData() {}

    public SpawnPointData(SpawnPoint from) {
        spawn = new LocationData(from.getSpawn());
        target = new LocationData(from.getTarget());
    }
}
