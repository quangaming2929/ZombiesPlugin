package io.github.zap.zombiesplugin.map;

import io.github.zap.zombiesplugin.map.spawn.SpawnPoint;

import java.util.ArrayList;

public interface ISpawnpointContainer {
    boolean canSpawn();
    int size();
    SpawnPoint getSpawnpoint(int index);
    ArrayList<SpawnPoint> getSpawnpoints();
}
