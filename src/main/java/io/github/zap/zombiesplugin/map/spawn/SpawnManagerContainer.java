package io.github.zap.zombiesplugin.map.spawn;

import io.github.zap.zombiesplugin.map.data.SpawnManagerData;

import java.util.ArrayList;

//TODO: get rid of this
public class SpawnManagerContainer {
    public ArrayList<SpawnManagerData> managers;

    public SpawnManagerContainer() {}

    public void load(ArrayList<SpawnManager> spawnManagers) {
        managers = new ArrayList<>();
        for(SpawnManager manager : spawnManagers) {
            managers.add(new SpawnManagerData(manager));
        }
    }
}
